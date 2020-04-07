package model

import csp.CSPProblem
import csp.ValueHeuristic
import csp.Variable
import csp.VariableHeuristic
import java.lang.reflect.Field
import java.util.*

class SudokuProblem(private val sudokuData: Sudoku,
                    private val valueHeuristic: ValueHeuristic<Char>,
                    variableHeuristic: VariableHeuristic<Char>)
    : CSPProblem<Char, Sudoku>(variableHeuristic)
{
    private lateinit var fields: List<List<SudokuField>>
    private lateinit var flattenFields: List<SudokuField>
    private lateinit var currentVariable: SudokuField
    private val correlatedFieldsHistory = Stack<List<SudokuField>>()

    init
    {
        initVariables(sudokuData)
    }

    override fun initVariables(data: Sudoku)
    {
        val platform = sudokuData.platform
        val variables = mutableListOf<List<SudokuField>>()

        for (i in platform.indices)
        {   val vRow = mutableListOf<SudokuField>()

            for (j in platform[i].indices)
            {
                val sf = SudokuField(platform[i][j], i, j, valueHeuristic)
                vRow.add(sf)
            }

            vRow.forEach { v -> designateVariableDomain(v) }
            variables.add(vRow.toList())
        }

        this.fields = variables.toList()
        flattenFields = variables.flatten()
    }

    private fun setVariableDefaultDomain(variable: Variable<Char>)
    {
        val vValue = variable.getValue()
        val vDomain = if (vValue != Sudoku.EMPTY_FIELD_REPR)
            listOf(vValue)
        else
            Sudoku.getDomainAsChar()

        variable.setDomain(vDomain)
    }
    override fun designateVariableDomain(variable: Variable<Char>)
    {
        setVariableDefaultDomain(variable)
    }

    override fun getSolution(): Sudoku
    {
        val solution = mutableListOf<List<Char>>()

        for (row in fields)
        {
            val sRow = row.map { v -> v.getValue() }
            solution.add(sRow)
        }

        return Sudoku(sudokuData.index, sudokuData.difficultyLevel, solution.toList())
    }

    override fun getNextVariable(): Variable<Char> {
        currentVariable = variableHeuristic.getNextVariable(flattenFields) as SudokuField
        currentVariable.memorizeState()
        return currentVariable
    }

    override fun hasNextVariable(): Boolean = variableHeuristic.hasNextVariable(flattenFields)
    override fun hasPreviousVariable(): Boolean = variableHeuristic.hasPreviousVariable(flattenFields)
    override fun getPreviousVariable(): Variable<Char>
    {
        currentVariable = variableHeuristic.getPreviousVariable(flattenFields) as SudokuField
        return currentVariable
    }

    fun getCorrelatedFields(field: SudokuField): List<SudokuField>
    {
        val fieldX = field.posX
        val fieldY = field.posY

        val relatedFields = mutableSetOf<SudokuField>()

        for (i in fields[fieldX].indices)
        {
            relatedFields.add(fields[i][fieldY])
            relatedFields.add(fields
                    [(fieldX / Sudoku.SUBGRID_SIZE) * Sudoku.SUBGRID_SIZE + i / Sudoku.SUBGRID_SIZE]
                    [fieldX * Sudoku.SUBGRID_SIZE % Sudoku.GRID_SIZE + i % Sudoku.SUBGRID_SIZE])
        }

        fields[fieldX].forEach { f -> relatedFields.add(f) }

        return relatedFields.filter { f -> f.getValue() == Sudoku.EMPTY_FIELD_REPR }
    }


    private fun validatePlatformPart(part: List<SudokuField>): Boolean
    {
        val repetitionCheckSet = mutableSetOf<Char>()

        for (field in part)
        {
            val fieldValue = field.getValue()
            if (fieldValue != Sudoku.EMPTY_FIELD_REPR)
            {
                if (fieldValue in repetitionCheckSet)
                {
                    return false
                }
                else
                {
                    repetitionCheckSet.add(fieldValue)
                }
            }
        }

        return true
    }

    override fun areConstraintsSatisfied(): Boolean
    {
        for (i in fields.indices)
        {
            val column = mutableListOf<SudokuField>()
            val square = mutableListOf<SudokuField>()
            val row = fields[i]

            for (j in fields[i].indices)
            {
                column.add(fields[j][i])
                square.add(fields
                        [(i / Sudoku.SUBGRID_SIZE) * Sudoku.SUBGRID_SIZE + j / Sudoku.SUBGRID_SIZE]
                        [i * Sudoku.SUBGRID_SIZE % Sudoku.GRID_SIZE + j % Sudoku.SUBGRID_SIZE])
            }

            if (!validatePlatformPart(column) || !validatePlatformPart(row) || !validatePlatformPart(square))
            {
                return false
            }

        }

        return true
    }

    override fun assignValueForVariable(value: Char, variable: Variable<Char>)
    {
        variable.assignValue(value)
    }

    override fun toString() = "Sudoku problem nr ${sudokuData.index}, diff: ${sudokuData.difficultyLevel}"
    override fun assignValueForVariable(variable: Variable<Char>, value: Char)
    {
        variable.assignValue(value)
    }

    override fun backTrack()
    {
        currentVariable.backTrack()
    }

    override fun checkForward()
    {
        val value = currentVariable.getValue()
        val correlatedFields = getCorrelatedFields(currentVariable)

        correlatedFieldsHistory.push(correlatedFields)

        for (field in correlatedFields)
        {
            field.filterDomain(value)
        }
    }

    override fun fcBackTrack()
    {
        val correlatedFields = correlatedFieldsHistory.pop()
        correlatedFields.forEach { f -> f.backTrackDomain() }
        currentVariable.backTrack()
    }

    override fun isAnyFilteredDomainEmpty(): Boolean {
        val correlatedFields = correlatedFieldsHistory.peek()
        return correlatedFields.any { f -> f.hasEmptyDomain() }
    }
}
