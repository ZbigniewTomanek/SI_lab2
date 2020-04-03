package csp.heuristics

import csp.Variable
import csp.VariableHeuristic


class BaselineVariableHeuristic : VariableHeuristic<Char>
{
    private var currentVariable = -1

    override fun getNextVariable(variables: List<Variable<Char>>): Variable<Char> = variables[++currentVariable]

    override fun hasNextVariable(variables: List<Variable<Char>>): Boolean = currentVariable < variables.size - 1

    override fun getPreviousVariable(variables: List<Variable<Char>>): Variable<Char> = variables[--currentVariable]

    override fun hasPreviousVariable(variables: List<Variable<Char>>) = currentVariable > 0

}