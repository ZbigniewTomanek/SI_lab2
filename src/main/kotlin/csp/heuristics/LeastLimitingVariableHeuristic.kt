package csp.heuristics

import csp.Variable
import csp.VariableHeuristic

class LeastLimitingVariableHeuristic : VariableHeuristic<Int>
{
    private val variableHistory = mutableListOf<Variable<Int>>()

    override fun getNextVariable(variables: List<Variable<Int>>): Variable<Int>
    {
        val variablesCopy = variables.toMutableList()
        variablesCopy.sortBy { v -> v.getDomain().size }

        for (v in variablesCopy)
        {
            if (v !in variableHistory)
            {
                variableHistory.add(v)
                return v
            }
        }

        return variables[0]
    }

    override fun hasNextVariable(variables: List<Variable<Int>>): Boolean = variableHistory.size < variables.size

    override fun getPreviousVariable(variables: List<Variable<Int>>): Variable<Int>
            = variableHistory.removeAt(variableHistory.size - 1)

    override fun hasPreviousVariable(variables: List<Variable<Int>>): Boolean = variableHistory.isNotEmpty()
}