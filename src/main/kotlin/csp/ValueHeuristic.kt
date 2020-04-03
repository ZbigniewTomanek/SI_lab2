package csp

interface ValueHeuristic<T>
{
    fun getNextValue(domain: List<T>): T
    fun hasNextValue(domain: List<T>): Boolean
}