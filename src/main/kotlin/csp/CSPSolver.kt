package csp

import java.lang.Exception

class InsolvableProblemException(msg: String) : Exception(msg)

object CSPSolver
{
    var assignmentsOfLastRun = 0
    var recurrencesOfLastRun = 0

    private fun <T, S> findValueForVariable(problem: CSPProblem<T, S>, variable: Variable<T>)
    {
        var currentVariable = variable

        if (!variable.hasNextValue())
        {

            if (!problem.hasPreviousVariable())
            {
                throw InsolvableProblemException("Problem $problem is insolvable")
            }
            else
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

            if (problem.areConstraintsSatisfied())
            {
                return
            }
            else
            {
                findValueForVariable(problem, currentVariable)
            }
        }

    }

    private fun <T, S> findCSPSolution(problem: CSPProblem<T, S>): S
    {
        var currentVariable: Variable<T>

        while (true)
        {

            if (!problem.hasNextVariable())
            {
                println("Znaleziono rozwiaznie")
                break
            }
            currentVariable = problem.getNextVariable()
            findValueForVariable(problem, currentVariable)
        }

        return problem.getSolution()
    }

    fun <T, S> solveCSPNaive(problem: CSPProblem<T, S>) : S
    {
        assignmentsOfLastRun = 0
        recurrencesOfLastRun = 0

        return findCSPSolution(problem)
    }
}