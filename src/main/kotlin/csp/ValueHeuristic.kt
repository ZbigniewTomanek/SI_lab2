package csp

interface ValueHeuristic<T>
{
    fun memorize()
    fun backtrack()

    fun getNextValue(domain: List<T>): T
    fun hasNextValue(domain: List<T>): Boolean
    fun copy(): ValueHeuristic<T>
}