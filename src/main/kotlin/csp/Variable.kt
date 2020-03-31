package csp

interface Variable<T>: ValueHeuristic<T>
{
    fun setValue(value: T)
    fun getDomain(allVariables: List<Any>): Set<T>
}