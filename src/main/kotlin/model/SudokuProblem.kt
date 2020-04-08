package model

import csp.CSPProblem
import csp.ValueHeuristic
import csp.Variable
import csp.VariableHeuristic
import java.util.*

class SudokuProblem(private val sudokuData: Sudoku,
                    private val valueHeuristic: ValueHeuristic<Int>,
                    variableHeuristic: VariableHeuristic<Int>)
    : CSPProblem<Int, Sudoku>(variableHeuristic)
{
    private lateinit var fields: List<List<SudokuField>>
    private lateinit var flattenFields: List<SudokuField>
    private lateinit var currentVariable: SudokuField
    private val correlatedFieldsHistory = Stack<List<SudokuField>>()

    init
    {
        initVariables(sudokuData)
        variableHeuristic.init(fields)
    }

    override fun initVariables(data: Sudoku)
    {
        val platform = sudokuData.platform
        val variables = mutableListOf<List<SudokuField>>()

        for (i in platform.indices)
        {   val vRow = mutableListOf<SudokuField>()

            for (j in platform[i].indices)
            {
                val sf = SudokuField(platform[i][j], j, i, valueHeuristic)
                vRow.add(sf)
            }

            vRow.forEach { v -> setVariableDefaultDomain(v) }
            variables.add(vRow.toList())
        }

        this.fields = variables.toList()
        flattenFields = variables.flatten()
    }

    private fun setVariableDefaultDomain(variable: Variable<Int>)
    {
        val vValue = variable.getValue()
        val vDomain = if (vValue != Sudoku.EMPTY_FIELD_REPR)
            listOf(vValue)
        else
            Sudoku.getDomain()

        variable.setDomain(vDomain)
    }

    override fun getSolution(): Sudoku
    {
        val solution = mutableListOf<List<Int>>()

        for (row in fields)
        {
            val sRow = row.map { v -> v.getValue() }
            solution.add(sRow)
        }

        return Sudoku(sudokuData.index, sudokuData.difficultyLevel, solution.toList())
    }

    override fun getNextVariable(): Variable<Int> {
        currentVariable = variableHeuristic.getNextVariable() as SudokuField
        currentVariable.memorizeState()
        return currentVariable
    }

    override fun hasNextVariable(): Boolean = variableHeuristic.hasNextVariable()
    override fun hasPreviousVariable(): Boolean = variableHeuristic.hasPreviousVariable()
    override fun getPreviousVariable(): Variable<Int>
    {
        currentVariable = variableHeuristic.getPreviousVariable() as SudokuField
        return currentVariable
    }

    fun getCorrelatedFields(field: SudokuField): List<SudokuField>
    {
        val posX = field.posX
        val posY = field.posY
        val relatedFields = mutableSetOf<SudokuField>()

        for (i in fields[posX].indices)
        {
            relatedFields.add(fields[i][posY])
            relatedFields.add(fields
                    [(posY / Sudoku.SUBGRID_SIZE) * Sudoku.SUBGRID_SIZE + i % Sudoku.SUBGRID_SIZE]
                    [(posX / Sudoku.SUBGRID_SIZE) * Sudoku.SUBGRID_SIZE + i / Sudoku.SUBGRID_SIZE])
        }

        fields[posX].forEach { f -> relatedFields.add(f) }

        return relatedFields.filter { f -> f.getValue() == Sudoku.EMPTY_FIELD_REPR }
    }


    private fun validatePlatformPart(part: List<SudokuField>): Boolean
    {
        val repetitionCheckSet = mutableSetOf<Int>()

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

    private fun wasMoveCorrect(): Boolean
    {
        val posX = currentVariable.posX
        val posY = currentVariable.posY

        val column = mutableListOf<SudokuField>()
        val square = mutableListOf<SudokuField>()
        val row = fields[posY]

        for (i in fields[posX].indices)
        {
            column.add(fields[i][posX])
            square.add(fields
                    [(posY / Sudoku.SUBGRID_SIZE) * Sudoku.SUBGRID_SIZE + i % Sudoku.SUBGRID_SIZE]
                    [(posX / Sudoku.SUBGRID_SIZE) * Sudoku.SUBGRID_SIZE + i / Sudoku.SUBGRID_SIZE])
        }


        if (!validatePlatformPart(column) || !validatePlatformPart(row) || !validatePlatformPart(square))
        {
            return false
        }

        return true
    }

    private fun validateWholePlatform(): Boolean
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

    override fun areConstraintsSatisfied(): Boolean = wasMoveCorrect()

    override fun assignValueForVariable(value: Int, variable: Variable<Int>)
    {
        variable.assignValue(value)
    }

    override fun toString() = "Sudoku problem nr ${sudokuData.index}, diff: ${sudokuData.difficultyLevel}"
    override fun assignValueForVariable(variable: Variable<Int>, value: Int)
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
