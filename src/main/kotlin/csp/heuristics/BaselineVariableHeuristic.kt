package csp.heuristics

import csp.Variable
import csp.VariableHeuristic

class BaselineVariableHeuristic : VariableHeuristic<Int>()
{
    private lateinit var variables: List<Variable<Int>>

    override fun init(fields: List<List<Variable<Int>>>)
    {
        variables = fields.flatten()
    }

    private var currentVariable = -1

    override fun getNextVariable(): Variable<Int> = variables[++currentVariable]

    override fun hasNextVariable(): Boolean = currentVariable < variables.size - 1

    override fun getPreviousVariable(): Variable<Int> = variables[--currentVariable]

    override fun hasPreviousVariable() = currentVariable > 0
}