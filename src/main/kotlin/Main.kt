import csp.CSPSolver
import csp.heuristics.BaselineValueHeuristic
import csp.heuristics.BaselineVariableHeuristic
import model.SudokuProblem
import utils.Reader

fun main()
{
    val sudoku = Reader.getSudoku(3)
    val valueHeuristic = BaselineValueHeuristic()
    val variableHeuristic = BaselineVariableHeuristic()

    val problem = SudokuProblem(sudoku, valueHeuristic, variableHeuristic)
    CSPSolver.findCSPSolution(problem)

    println(problem.areConstraintsSatisfied())
}
