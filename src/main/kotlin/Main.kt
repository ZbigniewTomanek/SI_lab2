import csp.CSPSolver
import csp.ValueHeuristic
import csp.VariableHeuristic
import csp.heuristics.BaselineValueHeuristic
import csp.heuristics.BaselineVariableHeuristic
import model.SudokuProblem
import utils.Reader

private fun checkHowManyCrashes()
{
    var crashes = 0
    for (i in 0 until 44)
    {
        val valueHeuristic = BaselineValueHeuristic()
        val variableHeuristic = BaselineVariableHeuristic()

        try
        {
            solveSudoku(i, valueHeuristic, variableHeuristic)
        } catch (e: Exception)
        {
            crashes++
        }

    }

    println("Solver has crashed $crashes times")
}

fun solveSudoku(index: Int, valueHeuristic: ValueHeuristic<Char>, variableHeuristic: VariableHeuristic<Char>)
{
    val sudoku = Reader.getSudoku(index)
    println("Before")
    println(sudoku)
    sudoku.printPlatform()
    val problem = SudokuProblem(sudoku, valueHeuristic, variableHeuristic)
    val solution = CSPSolver.solveCSPNaive(problem)
    println("Recurrences: ${CSPSolver.recurrencesOfLastRun}, Assignments: ${CSPSolver.assignmentsOfLastRun}")
    println("After")
    solution.printPlatform()
}

fun main()
{
    val valueHeuristic = BaselineValueHeuristic()
    val variableHeuristic = BaselineVariableHeuristic()
    //solveSudoku(2, valueHeuristic, variableHeuristic)
    checkHowManyCrashes()
}
