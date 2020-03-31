package csp

import java.lang.Exception

class InsolvableProblemException(msg: String) : Exception(msg)

object CSPSolver
{
    private fun <T, S> findValueForVariable(problem: CSPProblem<T, S>, variable: Variable<T>)
    {
        var currentVariable = variable
        var value = variable.getNextValue()

        if (!variable.hasNextValue())
        {
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
            currentVariable.assignValue(value)

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

    fun <T, S> findCSPSolution(problem: CSPProblem<T, S>): S
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
}