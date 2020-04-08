package csp

abstract class VariableHeuristic<T>
{
    lateinit var fields: List<List<Variable<T>>>

    abstract fun init(fields: List<List<Variable<T>>>)

    abstract fun getNextVariable(): Variable<T>
    abstract fun hasNextVariable(): Boolean

    abstract fun getPreviousVariable(): Variable<T>
    abstract fun hasPreviousVariable(): Boolean
}