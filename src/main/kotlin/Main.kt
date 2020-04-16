import csp.CSPSolver
import csp.ValueHeuristic
import csp.VariableHeuristic
import csp.heuristics.*
import kotlinx.coroutines.*
import model.Sudoku
import model.SudokuField
import model.SudokuProblem
import utils.SudokuReader
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread

fun solveSudoku(
    index: Int, valueHeuristic: ValueHeuristic<Int>,
    variableHeuristic: VariableHeuristic<Int>,
    file: File,
    checkForward: Boolean = false,
    printSolutions: Boolean = false)
{
    val sudoku = SudokuReader.getSudoku(index)
//    println("Before")
//    sudoku.printPlatform()
    val problem = SudokuProblem(sudoku, valueHeuristic, variableHeuristic.copy())

    val solutions = CSPSolver.solveCSP(problem, checkForward)

    FileOutputStream(file, true).bufferedWriter().use { out ->
        out.append("Found ${solutions.size} solutions for $problem; with FC $checkForward\n")
        out.append("To first solution: recurrences: ${CSPSolver.recurrencesToFirstSolution}, assignments: ${CSPSolver.assignmentsToFirstSolution} in ${CSPSolver.timeToFirstSolution/1000f}s\n")
        out.append("Total: recurrences: ${CSPSolver.totalRecurrencesOfLastRun}, assignments: ${CSPSolver.totalAssignmentsOfLastRun} in ${CSPSolver.totalTimeOfLastRun/1000f}s\n\n")
    }

    println()
    println("Found ${solutions.size} solutions for $problem; with FC $checkForward")
    println("To first solution: recurrences: ${CSPSolver.recurrencesToFirstSolution}, assignments: ${CSPSolver.assignmentsToFirstSolution} in ${CSPSolver.timeToFirstSolution/1000f}s")
    println("Total: recurrences: ${CSPSolver.totalRecurrencesOfLastRun}, assignments: ${CSPSolver.totalAssignmentsOfLastRun} in ${CSPSolver.totalTimeOfLastRun/1000f}s")


    if (printSolutions)
    {
        for (i in solutions.indices)
        {
            println("Solution ${i+1}")
            solutions[i].printPlatform()
            println()
        }
    }
}

val sudokusToSolve = listOf(10, 24, 34, 42, 45)

fun testHeuristics(valueHeuristic: ValueHeuristic<Int>, variableHeuristic: VariableHeuristic<Int>, file: File)
{

    for (sudoku in sudokusToSolve)
    {
        solveSudoku(sudoku, valueHeuristic, variableHeuristic, file,false)
        solveSudoku(sudoku, valueHeuristic, variableHeuristic, file, true)
    }

}

fun main()
{

    var valueHeuristic: ValueHeuristic<Int> = RandomValueHeuristic()
    var variableHeuristic: VariableHeuristic<Int> = RandomVariableHeuristic()

    GlobalScope.launch {
        println("Random val, random var")
        val f = File("results/random_val_random_var.txt")
        testHeuristics(valueHeuristic, variableHeuristic, f)
        println()
        println("--------------------------------------------------------------------------------")
        println()
    }


    GlobalScope.launch {
        valueHeuristic = BaselineValueHeuristic()
        variableHeuristic = RandomVariableHeuristic()

        val f = File("results/baseline_val_random_var.txt")
        println("Baseline val, random var")
        testHeuristics(valueHeuristic, variableHeuristic, f)
        println()
        println("--------------------------------------------------------------------------------")
        println()
    }


    GlobalScope.launch {
        valueHeuristic = BaselineValueHeuristic()
        variableHeuristic = BaselineVariableHeuristic()

        val f = File("results/baseline_val_baseline_var.txt")
        println("Baseline val, baseline var")
        testHeuristics(valueHeuristic, variableHeuristic, f)
        println()
        println("--------------------------------------------------------------------------------")
        println()
    }



    GlobalScope.launch {
        valueHeuristic = RandomValueHeuristic()
        variableHeuristic = BaselineVariableHeuristic()

        val f = File("results/random_val_baseline_var.txt")
        println("Random val, baseline var")
        testHeuristics(valueHeuristic, variableHeuristic, f)
        println()
        println("--------------------------------------------------------------------------------")
        println()
    }



    GlobalScope.launch {
        valueHeuristic = RandomValueHeuristic()
        variableHeuristic = LeastLimitingVariableHeuristic()

        val f = File("results/random_val_least_var.txt")
        println("Random val, least lim var")
        testHeuristics(valueHeuristic, variableHeuristic, f)
        println()
        println("--------------------------------------------------------------------------------")
        println()
    }


    GlobalScope.launch {
        val f = File("results/baseline_val_least_var.txt")
        valueHeuristic = BaselineValueHeuristic()
        variableHeuristic = LeastLimitingVariableHeuristic()
        println("Baseline val, least lim var")
        testHeuristics(valueHeuristic, variableHeuristic, f)
        println()
        println("--------------------------------------------------------------------------------")
        println()
    }

    runBlocking {
        delay(Long.MAX_VALUE)
    }


    //solveSudoku(0, valueHeuristic, variableHeuristic, false)
}
