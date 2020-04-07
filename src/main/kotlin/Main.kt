import csp.CSPSolver
import csp.ValueHeuristic
import csp.VariableHeuristic
import csp.heuristics.BaselineValueHeuristic
import csp.heuristics.BaselineVariableHeuristic
import csp.heuristics.RandomValueHeuristic
import csp.heuristics.RandomVariableHeuristic
import model.SudokuProblem
import utils.Reader

fun solveSudoku(index: Int, valueHeuristic: ValueHeuristic<Char>, variableHeuristic: VariableHeuristic<Char>)
{
    val sudoku = Reader.getSudoku(index)
    println("Before")
    println(sudoku)
    sudoku.printPlatform()
    val problem = SudokuProblem(sudoku, valueHeuristic, variableHeuristic)

    val t1 = System.currentTimeMillis()
    val solutions = CSPSolver.solveCSPNaive(problem)
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
    solveSudoku(0, valueHeuristic, variableHeuristic)
    //checkHowManyCrashes()
}
