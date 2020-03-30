package csp

interface Variable<T>: ValueHeuristic<T>
{
    fun getDomain(allVariables: List<Any>): Set<T>
}