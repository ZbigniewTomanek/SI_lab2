package csp

object CSPSolver
{
    var assignmentsOfLastRun = 0
    var recurrencesOfLastRun = 0

    private fun <T, S> findValueForVariable(problem: CSPProblem<T, S>, variable: Variable<T>): Boolean
    {
        var currentVariable = variable

        if (!variable.hasNextValue())
        {

            return if (!problem.hasPreviousVariable())
            {
                true
            } else
            {
                problem.backTrack()
                recurrencesOfLastRun++
                currentVariable = problem.getPreviousVariable()

                findValueForVariable(problem, currentVariable)
            }
        }
        else
        {
            val value = variable.getNextValue()
            assignmentsOfLastRun++
            problem.assignValueForVariable(value, variable)

            return if (problem.areConstraintsSatisfied())
            {
                false
            }
            else
            {
                findValueForVariable(problem, currentVariable)
            }
        }

    }

    private fun <T, S> findCSPSolution(problem: CSPProblem<T, S>): List<S>
    {
        var currentVariable: Variable<T>
        val solutions = mutableListOf<S>()
        var foundAllSolutions = false

        while (!foundAllSolutions)
        {
            if (!problem.hasNextVariable())
            {
                println("solution")
                val solution = problem.getSolution()
                solutions.add(solution)

                problem.backTrack()
                recurrencesOfLastRun++
                currentVariable = problem.getPreviousVariable()
                foundAllSolutions = findValueForVariable(problem, currentVariable)

            }
            else
            {
                currentVariable = problem.getNextVariable()
                foundAllSolutions = findValueForVariable(problem, currentVariable)
            }
        }

        return solutions.toList()
    }

    fun <T, S> solveCSPNaive(problem: CSPProblem<T, S>) : List<S>
    {
        assignmentsOfLastRun = 0
        recurrencesOfLastRun = 0

        return findCSPSolution(problem)
    }
}