import csp.CSPSolver
import csp.heuristics.BaselineValueHeuristic
import csp.heuristics.BaselineVariableHeuristic
import model.SudokuProblem
import utils.Reader

fun main()
{
    val sudoku = Reader.getSudoku(4)

    println("Before")
    sudoku.printPlatform()
    val valueHeuristic = BaselineValueHeuristic()
    val variableHeuristic = BaselineVariableHeuristic()

    val problem = SudokuProblem(sudoku, valueHeuristic, variableHeuristic)

    val solution = CSPSolver.solveCSPNaive(problem)

    println("Recurrences: ${CSPSolver.recurrencesOfLastRun}, Assignments: ${CSPSolver.assignmentsOfLastRun}")

    println("After")
    solution.printPlatform()
}
