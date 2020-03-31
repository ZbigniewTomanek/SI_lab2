package csp

interface VariableHeuristic<T>
{
    fun getNextVariable(variables: List<Variable<T>>): Variable<T>
    fun hasNextVariable(variables: List<Variable<T>>): Boolean
}