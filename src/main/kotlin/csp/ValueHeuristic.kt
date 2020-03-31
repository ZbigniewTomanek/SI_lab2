package csp

interface ValueHeuristic<T> // zapamiętać jakie były już użyte
{
    fun getNextValue(domain: List<T>): T
    fun hasNextValue(domain: List<T>): Boolean
}