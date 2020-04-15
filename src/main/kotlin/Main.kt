import csp.CSPSolver
import csp.ValueHeuristic
import csp.VariableHeuristic
import csp.heuristics.*
import model.SudokuProblem
import utils.SudokuReader

fun solveSudoku(
    index: Int, valueHeuristic: ValueHeuristic<Int>,
    variableHeuristic: VariableHeuristic<Int>,
    checkForward: Boolean = false,
    printSolutions: Boolean = false)
{
    val sudoku = SudokuReader.getSudoku(index)
    println("Before")
    println(sudoku)
    sudoku.printPlatform()
    val problem = SudokuProblem(sudoku, valueHeuristic, variableHeuristic.copy())

    val solutions = CSPSolver.solveCSP(problem, checkForward)

    if (solutions.isEmpty())
    {
        println("This problem has no solutions")
        return
    }

    println()
    println("Found ${solutions.size} solutions")
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

fun main()
{
    val valueHeuristic = RandomValueHeuristic()
    val variableHeuristic = LeastLimitingVariableHeuristic()


    solveSudoku(42, valueHeuristic, variableHeuristic, true)
    //solveSudoku(0, valueHeuristic, variableHeuristic, false)
}
