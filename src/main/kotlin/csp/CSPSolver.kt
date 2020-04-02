package csp

import java.lang.Exception

class InsolvableProblemException(msg: String) : Exception(msg)

object CSPSolver
{
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
                currentVariable = problem.getPreviousVariable()
                findValueForVariable(problem, currentVariable)
            }
        }
        else
        {
            val value = variable.getNextValue()
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


            if (!problem.hasNextVariable())
            {
                println("Znaleziono rozwiaznie")
                break
            }
            else
            {
                currentVariable = problem.getNextVariable()
            }

            findValueForVariable(problem, currentVariable)
        }

        return problem.getSolution()
    }
}