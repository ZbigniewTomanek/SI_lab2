import csp.CSPSolver
import csp.ValueHeuristic
import csp.VariableHeuristic
import csp.heuristics.*
import model.SudokuProblem
import utils.SudokuReader

fun solveSudoku
            (index: Int, valueHeuristic:
            ValueHeuristic<Int>,
             variableHeuristic: VariableHeuristic<Int>,
             checkForward: Boolean = false)
{
    val sudoku = SudokuReader.getSudoku(index)
    println("Before")
    println(sudoku)
    sudoku.printPlatform()
    val problem = SudokuProblem(sudoku, valueHeuristic, variableHeuristic)

    val t1 = System.currentTimeMillis()
    val solutions = if (checkForward) CSPSolver.solveCSPForwardChecking(problem) else CSPSolver.solveCSPNaive(problem)
    val t2 = System.currentTimeMillis()

    println("Recurrences: ${CSPSolver.recurrencesOfLastRun}, Assignments: ${CSPSolver.assignmentsOfLastRun}")

    if (solutions.isEmpty())
    {
        println("This problem has no solutions")
        return
    }

    println("${solutions.size} solutions found in ${(t2 - t1)/1000f}s\n")

    for (i in solutions.indices)
    {
        println("Solution ${i+1}")
        solutions[i].printPlatform()
        println()
    }
}

fun main()
{
    val valueHeuristic = BaselineValueHeuristic()
    val variableHeuristic = BaselineVariableHeuristic()
    solveSudoku(0, valueHeuristic, variableHeuristic, true)
    //solveSudoku(0, valueHeuristic, variableHeuristic, false)
}
