package csp

interface VariableHeuristic<T>
{
    fun getNextVariable(): Variable<T>
    fun hasNextVariable(): Boolean
}