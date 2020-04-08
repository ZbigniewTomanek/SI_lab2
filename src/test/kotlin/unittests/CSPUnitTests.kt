package unittests

import csp.CSPSolver
import csp.ValueHeuristic
import csp.Variable
import csp.VariableHeuristic
import csp.heuristics.*
import model.Sudoku
import model.SudokuField
import model.SudokuProblem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.SudokuReader

class CSPUnitTests
{
    lateinit var sudokuProblem: SudokuProblem
    lateinit var sudoku: Sudoku
    private val baselineVariableHeuristic: VariableHeuristic<Int> = LeastLimitingVariableHeuristic()
    private val baselineValueHeuristic: ValueHeuristic<Int> = RandomValueHeuristic()

    @BeforeEach
    fun configureVariables()
    {
        sudoku = SudokuReader.getSudoku(0)
        sudokuProblem = SudokuProblem(sudoku, baselineValueHeuristic, baselineVariableHeuristic)
    }

    @Test
    fun testBaselineValHeuristic()
    {
        sudokuProblem.getNextVariable()
        sudokuProblem.getNextVariable()
        sudokuProblem.getNextVariable()
        val variable = sudokuProblem.getNextVariable()
        val returnedValues = mutableListOf<Int>()

        while (variable.hasNextValue())
        {

            val value = variable.getNextValue()
            returnedValues.add(value)
        }

        assert(Sudoku.getDomain().toSet() == returnedValues.toSet())
    }

    @Test
    fun testBaselineVarHeuristic()
    {
        val sp = SudokuProblem(sudoku, BaselineValueHeuristic(), BaselineVariableHeuristic())
        val returnedVars = mutableListOf<Variable<Int>>()

        while (sp.hasNextVariable())
        {
            val retVar = sp.getNextVariable()
            returnedVars.add(retVar)
        }

        val values: List<Int> = sudoku.platform.flatten()
        val variableValues = returnedVars.map { v -> v.getValue() }


        assert(values == variableValues)
        assert(returnedVars.size == Sudoku.GRID_SIZE * Sudoku.GRID_SIZE)
    }

    @Test
    fun testGettingPreviousVariable()
    {

        val firstVar = sudokuProblem.getNextVariable()
        var variable: Variable<Int>? = null

        while (sudokuProblem.hasNextVariable())
        {
            variable = sudokuProblem.getNextVariable()
            println(variable)
        }

        println()

        while (sudokuProblem.hasPreviousVariable())
        {
            variable = sudokuProblem.getPreviousVariable() as SudokuField
            println(variable)
        }

        assert(firstVar == variable!!)
    }

    @Test
    fun testGettingNextVar()
    {
        for (i in 0 until 81)
        {
            sudokuProblem.getNextVariable()
        }

        assert(!sudokuProblem.hasNextVariable())
    }


    @Test
    fun testConstraintsSatisfactionWithDots()
    {
        assert(sudokuProblem.areConstraintsSatisfied())


    }

    @Test
    fun testConstraintsColumn()
    {
        val np = SudokuReader.getSudoku(46)
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

        variable.getNextValue()
        assert(!variable.hasNextValue())
    }

    @Test
    fun testBackTracking()
    {
        val variable = sudokuProblem.getNextVariable()
        val firstValue = variable.getValue()

        sudokuProblem.assignValueForVariable(1, variable)
        sudokuProblem.backTrack()

        assert(firstValue == variable.getValue())
    }

    fun testEasySudokuSolving()
    {
        val answer = "625371948473985216819462753231794685547618329968523174196857432352146897784239561"
        val answerList = answer.map { v -> v }

        val flatSolution = CSPSolver.solveCSPNaive(sudokuProblem)[0].platform.flatten()

        assert(answerList == flatSolution)
    }

//    @Test
//    fun testGettingCorrelatedFields()
//    {
//        val sudoku = sudokuProblem.getSolution()
//        sudoku.printPlatform()
//        val field = sudokuProblem.getNextVariable() as SudokuField
//        println(field)
//        val fields = sudokuProblem.getCorrelatedFields(field).map { v -> v.getValue()}
//        println(fields)
//    }

    @Test
    fun testMakingCorrectMove()
    {
        var variable = sudokuProblem.getNextVariable() as SudokuField

        while (variable.posX != 0 || variable.posY != 5)
            variable = sudokuProblem.getNextVariable() as SudokuField
        variable.assignValue(3)
        assert(sudokuProblem.areConstraintsSatisfied())


        while (variable.posX != 2 || variable.posY != 5)
            variable = sudokuProblem.getNextVariable() as SudokuField
        variable.assignValue(2)
        assert(!sudokuProblem.areConstraintsSatisfied())
    }

//    @Test
//    fun testForwardChecking()
//    {
//        var variable = sudokuProblem.getNextVariable()
//        while (sudokuProblem.hasNextVariable() && variable.getValue() == Sudoku.EMPTY_FIELD_REPR)
//        {
//            variable = sudokuProblem.getNextVariable()
//        }
//        val correlatedFields = sudokuProblem.getCorrelatedFields(variable as SudokuField)
//        val value = variable.getValue()
//
//        sudokuProblem.checkForward()
//
//        correlatedFields.forEach { f -> assert(value !in f.getDomain()) }
//        sudokuProblem.fcBackTrack()
//        correlatedFields.forEach { f -> assert(value in f.getDomain()) }
//    }

}