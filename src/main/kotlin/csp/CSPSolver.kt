package csp

object CSPSolver
{
    var assignmentsOfLastRun = 0
    var recurrencesOfLastRun = 0

    private fun <T, S> nBackTrack(problem: CSPProblem<T, S>): Boolean
    {
        problem.backTrack()
        recurrencesOfLastRun++

        return findValueForVariable(problem, problem.getPreviousVariable())
    }

    private fun <T, S> findValueForVariable(problem: CSPProblem<T, S>, variable: Variable<T>): Boolean
    {

        if (!variable.hasNextValue())
        {

            return if (!problem.hasPreviousVariable())
            {
                true
            }
            else
            {
                nBackTrack(problem)
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
                findValueForVariable(problem, variable)
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

                foundAllSolutions = nBackTrack(problem)

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

    private var currVar: Int = 0

    private fun <T, S> fcBackTrack(problem: CSPProblem<T, S>): Boolean
    {
        recurrencesOfLastRun++
        //problem.fcBackTrack()
        problem.backTrack()
        currVar--
        return findValueForVariableForwardChecking(problem, problem.getPreviousVariable())
    }

    private fun <T, S> findValueForVariableForwardChecking(problem: CSPProblem<T, S>, variable: Variable<T>): Boolean
    {
        if (!variable.hasNextValue())
        {
            return if (problem.hasPreviousVariable())
            {
                fcBackTrack(problem)
            } else
            {
                true
            }

        } else
        {
            assignmentsOfLastRun++
            val value = variable.getNextValue()
            problem.assignValueForVariable(value, variable)
            problem.checkForward()

            return if (!problem.areConstraintsSatisfied())
            {
                problem.backTrackFiltering()
                findValueForVariableForwardChecking(problem, variable)
            } else
            {
                return false
            }
        }
    }

    private fun <T, S> findCSPSolutionForwardChecking(problem: CSPProblem<T, S>): List<S>
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

                foundAllSolutions = fcBackTrack(problem)
            }
            else
            {
                currentVariable = problem.getNextVariable()
                problem.saveCorrelatedVarsDomainsState()
                currVar++
                println(currVar)

                foundAllSolutions = findValueForVariableForwardChecking(problem, currentVariable)
            }
        }

        return solutions.toList()
    }

    fun <T, S> solveCSPForwardChecking(problem: CSPProblem<T, S>) : List<S>
    {
        assignmentsOfLastRun = 0
        recurrencesOfLastRun = 0

        return findCSPSolutionForwardChecking(problem)
    }
}