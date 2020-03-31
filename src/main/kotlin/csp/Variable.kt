package csp

abstract class Variable<T>(val valueHeuristic: ValueHeuristic<T>)
{
    abstract fun assignValue(value: T)
    abstract fun getNextValue(): T
    abstract fun hasNextValue(): Boolean
    abstract fun calculateDomain(variables: List<List<Variable<T>>>)
}