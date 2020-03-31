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
    private lateinit var variables: List<List<SudokuField>>
    private lateinit var remainingVariables: MutableList<SudokuField>
    private val variableHistory: MutableList<SudokuField> = mutableListOf()

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
            variables.add(vRow)
        }

        this.variables = variables.toList()
        remainingVariables = variables.flatten().toMutableList()
        remainingVariables.forEach { v -> v.calculateDomain(variables) }
    }

    override fun getSolution(): Sudoku
    {
        val solution = mutableListOf<List<Char>>()

        for (row in variables)
        {
            val sRow = row.map { v -> v.value }
            solution.add(sRow)
        }

        return Sudoku(sudokuData.index, sudokuData.difficultyLevel, solution.toList())
    }

    override fun hasPreviousVariable(): Boolean = variableHistory.isNotEmpty()

    @ExperimentalStdlibApi
    override fun getPreviousVariable(): Variable<Char> = variableHistory.removeLast()


    override fun areConstraintsSatisfied(): Boolean
    {
        TODO("Not yet implemented")
    }

    override fun getNextVariable(): Variable<Char>
    {
        val takenVariable = variableHeuristic.getNextVariable(remainingVariables)
        variableHistory.add(takenVariable as SudokuField)
        return takenVariable
    }

    override fun hasNextVariable(): Boolean = variableHeuristic.hasNextVariable(remainingVariables)

    override fun assignValueForVariable(value: Char, variable: Variable<Char>)
    {
        variable.assignValue(value)
        variable.calculateDomain(variables)
    }

    override fun toString() = "Sudoku problem nr ${sudokuData.index}, diff: ${sudokuData.difficultyLevel}"
}
