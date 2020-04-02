package unittests

import csp.Variable
import csp.heuristics.BaselineValueHeuristic
import csp.heuristics.BaselineVariableHeuristic
import model.Sudoku
import model.SudokuField
import model.SudokuProblem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.Reader

class CSPUnitTests
{
    lateinit var sudokuProblem: SudokuProblem
    lateinit var sudoku: Sudoku
    private val baselineVariableHeuristic: BaselineVariableHeuristic = BaselineVariableHeuristic()
    private val baselineValueHeuristic: BaselineValueHeuristic = BaselineValueHeuristic()

    @BeforeEach
    fun configureVariables()
    {
        sudoku = Reader.getSudoku(0)
        sudokuProblem = SudokuProblem(sudoku, baselineValueHeuristic, baselineVariableHeuristic)
    }

    @Test
    fun testBaselineValHeuristic()
    {
        val domain = Sudoku.getDomainAsChar()
        val returnedVariables = mutableListOf<Char>()

        while (baselineValueHeuristic.hasNextValue(domain))
        {
            val value = baselineValueHeuristic.getNextValue(domain)
            returnedVariables.add(value)
        }

        assert(domain == returnedVariables.toList())

    }

    @Test
    fun testBaselineVarHeuristic()
    {
        val returnedVars = mutableListOf<Variable<Char>>()

        while (sudokuProblem.hasNextVariable())
        {
            val retVar = sudokuProblem.getNextVariable()
            returnedVars.add(retVar)
        }

        val values: List<Char> = sudoku.platform.flatten()
        val variableValues = returnedVars.map { v -> v.getValue() }


        assert(values == variableValues)
        assert(returnedVars.size == Sudoku.GRID_SIZE * Sudoku.GRID_SIZE)
    }

    @Test
    fun testGettingPreviousVariable()
    {
        val prevVar = sudokuProblem.getNextVariable()
        sudokuProblem.getNextVariable()

        assert(prevVar == sudokuProblem.getPreviousVariable())
    }
}