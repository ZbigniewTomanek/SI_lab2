package csp

abstract class Variable<T>(val valueHeuristic: ValueHeuristic<T>)
{
    abstract fun assignValue(value: T)
    abstract fun getValue(): T

    abstract fun getNextValue(): T
    abstract fun hasNextValue(): Boolean

    abstract fun setDomain(domain: List<T>)
    abstract fun getDomain(): List<T>

    abstract fun backTrack()
    abstract fun memorizeState()
}