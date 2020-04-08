package csp

abstract class CSPProblem<T, S>(
    val variableHeuristic: VariableHeuristic<T>)
{
    abstract fun initVariables(data: S)
    abstract fun getNextVariable(): Variable<T>
    abstract fun hasNextVariable(): Boolean
    abstract fun assignValueForVariable(value: T, variable: Variable<T>)

    abstract fun getSolution(): S

    abstract fun hasPreviousVariable(): Boolean
    abstract fun getPreviousVariable(): Variable<T>

    abstract fun areConstraintsSatisfied(): Boolean

    abstract fun assignValueForVariable(variable: Variable<T>, value: T)
    abstract fun backTrack()

    abstract fun checkForward()
    abstract fun fcBackTrack()
    abstract fun isAnyFilteredDomainEmpty(): Boolean
}
