package csp

object CSPSolver
{
    private var startTime = 0L
    var totalTimeOfLastRun = 0L
    var timeToFirstSolution = 0L
    var totalAssignmentsOfLastRun = 0
    var totalRecurrencesOfLastRun = 0
    var assignmentsToFirstSolution = 0
    var recurrencesToFirstSolution = 0
    var foundFirstSolution = false


    private fun <T, S> nBackTrack(problem: CSPProblem<T, S>): Boolean
    {
        totalRecurrencesOfLastRun++

        problem.backTrack()
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
            totalAssignmentsOfLastRun++

            val value = variable.getNextValue()
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
                if (!foundFirstSolution){
                    foundFirstSolution = true

                    timeToFirstSolution = System.currentTimeMillis() - startTime
                    assignmentsToFirstSolution = totalAssignmentsOfLastRun
                    recurrencesToFirstSolution = totalRecurrencesOfLastRun
                }

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

        totalTimeOfLastRun = System.currentTimeMillis() - startTime
        return solutions.toList()
    }


    private fun <T, S> fcBackTrack(problem: CSPProblem<T, S>): Boolean
    {
        totalRecurrencesOfLastRun++

        problem.fcBackTrack()
        return findValueForVariableForwardChecking(problem, (problem).getPreviousVariable())
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
            totalAssignmentsOfLastRun++

            problem.backTrackFiltering()
            val value = variable.getNextValue()
            problem.assignValueForVariable(value, variable)

            return if (problem.areConstraintsSatisfied())
            {
                problem.checkForward()

                if (problem.isAnyDomainEmpty())
                {
                    fcBackTrack(problem)
                } else
                {
                    false
                }

            } else
            {
                findValueForVariableForwardChecking(problem, variable)
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
                if (!foundFirstSolution){
                    foundFirstSolution = true

                    timeToFirstSolution = System.currentTimeMillis() - startTime
                    assignmentsToFirstSolution = totalAssignmentsOfLastRun
                    recurrencesToFirstSolution = totalRecurrencesOfLastRun
                }
                val solution = problem.getSolution()
                solutions.add(solution)
                foundAllSolutions = fcBackTrack(problem)
            }
            else
            {
                currentVariable = problem.getNextVariable()
                problem.saveCorrelatedVarsDomainsState()
                foundAllSolutions = findValueForVariableForwardChecking(problem, currentVariable)
            }
        }

        totalTimeOfLastRun = System.currentTimeMillis() - startTime
        return solutions.toList()
    }

    private fun resetMeasures()
    {
        totalAssignmentsOfLastRun = 0
        totalRecurrencesOfLastRun = 0
        recurrencesToFirstSolution = 0
        assignmentsToFirstSolution = 0
        totalTimeOfLastRun = 0
        timeToFirstSolution = 0
        foundFirstSolution = false
    }


    fun <T, S> solveCSP(problem: CSPProblem<T, S>, checkForward: Boolean=false) : List<S>
    {
        resetMeasures()

        startTime = System.currentTimeMillis()
        return if (checkForward)
        {
            findCSPSolutionForwardChecking(problem)
        }
        else
        {
            findCSPSolution(problem)
        }
    }
}