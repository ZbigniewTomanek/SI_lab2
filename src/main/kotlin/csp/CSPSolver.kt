package csp

import java.lang.Exception

class InsolvableProblemException(msg: String) : Exception(msg)

object CSPSolver
{
    fun <T> findCSPSolution(problem: CSPProblem<T>): Any
    {
        var currentVariable: Variable<T>
        while (problem.hasNextVariable())
        {
            currentVariable = problem.getNextVariable()

            if (!currentVariable.hasNextValue())
            {
                if (problem.hasPreviousVariable())
                {
                    currentVariable = problem.getPreviousVariable()
                }
                else
                {
                    throw InsolvableProblemException("Problem $problem is insolvable")
                }
            }
        }

        return problem.getSolution()
    }
}