package csp.heuristics

import csp.Variable
import csp.VariableHeuristic


class BaselineVariableHeuristic : VariableHeuristic<Int>
{
    private var currentVariable = -1

    override fun getNextVariable(variables: List<Variable<Int>>): Variable<Int> = variables[++currentVariable]

    override fun hasNextVariable(variables: List<Variable<Int>>): Boolean = currentVariable < variables.size - 1

    override fun getPreviousVariable(variables: List<Variable<Int>>): Variable<Int> = variables[--currentVariable]

    override fun hasPreviousVariable(variables: List<Variable<Int>>) = currentVariable > 0

}