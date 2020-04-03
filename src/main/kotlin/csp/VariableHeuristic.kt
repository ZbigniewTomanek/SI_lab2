package csp

interface VariableHeuristic<T>
{
    fun getNextVariable(variables: List<Variable<T>>): Variable<T>
    fun hasNextVariable(variables: List<Variable<T>>): Boolean

    fun getPreviousVariable(variables: List<Variable<T>>): Variable<T>
    fun hasPreviousVariable(variables: List<Variable<T>>): Boolean
}