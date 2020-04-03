package csp

import java.lang.Exception

class InsolvableProblemException(msg: String) : Exception(msg)

object CSPSolver
{
    var assignmentsOfLastRun = 0
    var recurrencesOfLastRun = 0

    private fun <T, S> findValueForVariable(problem: CSPProblem<T, S>, variable: Variable<T>)
    {
        println(variable)
        var currentVariable = variable

        if (!variable.hasNextValue())
        {
            println("Recurrence")
            problem.backTrack()
            recurrencesOfLastRun++
            currentVariable = problem.getPreviousVariable()


            if (!problem.hasPreviousVariable())
            {
                throw InsolvableProblemException("Problem $problem is insolvable")
            }
            else
            {

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
                println("found value for variable $variable")
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

            currentVariable = problem.getNextVariable()
            if (!problem.hasNextVariable())
            {
                println("Znaleziono rozwiaznie")
                break
            }

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