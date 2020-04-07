package csp.heuristics

import csp.Variable
import csp.VariableHeuristic

class LeastLimitingVariableHeuristic : VariableHeuristic<Char>
{
    private val variableHistory = mutableListOf<Variable<Char>>()

    override fun getNextVariable(variables: List<Variable<Char>>): Variable<Char>
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

    override fun hasNextVariable(variables: List<Variable<Char>>): Boolean = variableHistory.size < variables.size

    override fun getPreviousVariable(variables: List<Variable<Char>>): Variable<Char>
            = variableHistory.removeAt(variableHistory.size - 1)

    override fun hasPreviousVariable(variables: List<Variable<Char>>): Boolean = variableHistory.isNotEmpty()
}