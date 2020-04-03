package model

import csp.CSPProblem
import csp.ValueHeuristic
import csp.Variable
import csp.VariableHeuristic

class SudokuProblem(private val sudokuData: Sudoku,
                    private val valueHeuristic: ValueHeuristic<Char>,
                    variableHeuristic: VariableHeuristic<Char>)
    : CSPProblem<Char, Sudoku>(variableHeuristic)
{
    private lateinit var fields: List<List<SudokuField>>
    private lateinit var flattenFields: List<SudokuField>
    private lateinit var currentVariable: SudokuField

    init
    {
        initVariables(sudokuData)
    }

    override fun initVariables(data: Sudoku)
    {
        val platform = sudokuData.platform
        val variables = mutableListOf<List<SudokuField>>()

        for (row in platform)
        {
            val vRow: List<SudokuField> = row.map { v -> SudokuField(v, valueHeuristic) }
            vRow.forEach { v -> designateVariableDomain(v) }
            variables.add(vRow)
        }

        this.fields = variables.toList()
        flattenFields = variables.flatten()
        println(flattenFields)
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
        for (i in 0 until Sudoku.GRID_SIZE)
        {
            val column = mutableListOf<SudokuField>()
            val square = mutableListOf<SudokuField>()
            val row = fields[i]

            for (j in 0 until Sudoku.GRID_SIZE)
            {
                column.add(fields[i][j])
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
}
