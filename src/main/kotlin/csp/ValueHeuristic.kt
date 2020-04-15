package csp

interface ValueHeuristic<T>
{
    fun init(domain: List<T>)

    fun memorize()
    fun backtrack()

    fun getNextValue(): T
    fun hasNextValue(): Boolean
    fun copy(): ValueHeuristic<T>
}