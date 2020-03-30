package csp

interface ValueHeuristic<T>
{
    fun getNextValue(): T
    fun hasNextValue(): Boolean
}