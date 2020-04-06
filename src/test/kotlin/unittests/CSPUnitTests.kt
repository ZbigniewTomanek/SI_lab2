package unittests

import csp.CSPSolver
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
        val variable = sudokuProblem.getNextVariable()
        val returnedValues = mutableListOf<Char>()


        while (variable.hasNextValue())
        {

            val value = variable.getNextValue()
            returnedValues.add(value)
        }

        assert(Sudoku.getDomainAsChar() == returnedValues.toList())

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
        println(prevVar)
        println(sudokuProblem.getNextVariable())
        val prevVar2 = sudokuProblem.getPreviousVariable()
        println(prevVar2)

        assert(prevVar == prevVar2)
    }

    @Test
    fun testConstraintsSatisfactionWithDots()
    {
        assert(sudokuProblem.areConstraintsSatisfied())


    }

    @Test
    fun testConstraintsColumn()
    {
        val np = Reader.getSudoku(46)
        val sp = SudokuProblem(np, baselineValueHeuristic, baselineVariableHeuristic)
        assert(!sp.areConstraintsSatisfied())
    }



    @Test
    fun testOneElementDomainVariable()
    {
        var variable = sudokuProblem.getNextVariable()

        while (sudokuProblem.hasNextVariable() && variable.getValue() == Sudoku.EMPTY_FIELD_REPR)
        {
            variable = sudokuProblem.getNextVariable()
        }

        assert(!variable.hasNextValue())
    }

    @Test
    fun testBackTracking()
    {
        val variable = sudokuProblem.getNextVariable()
        val firstValue = variable.getValue()

        sudokuProblem.assignValueForVariable('1', variable)
        sudokuProblem.backTrack()

        assert(firstValue == variable.getValue())
    }

    @Test
    fun testEasySudokuSolving()
    {
        val answer = "625371948473985216819462753231794685547618329968523174196857432352146897784239561"
        val answerList = answer.map { v -> v }

        val flatSolution = CSPSolver.solveCSPNaive(sudokuProblem)[0].platform.flatten()

        assert(answerList == flatSolution)
    }

    @Test
    fun testGettingCorrelatedFields()
    {
        val sudoku = sudokuProblem.getSolution()
        sudoku.printPlatform()
        val field = sudokuProblem.getNextVariable() as SudokuField
        println(field)
        val fields = sudokuProblem.getCorrelatedFields(field).map { v -> "(${v.posX}, ${v.posY})"}
        println(fields)
    }

}