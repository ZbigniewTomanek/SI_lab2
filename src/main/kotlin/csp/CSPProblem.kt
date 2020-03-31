package csp

interface CSPProblem<T> : VariableHeuristic<T>
{
    fun getSolution(): Any
    fun hasPreviousVariable(): Boolean
    fun getPreviousVariable(): Variable<T>
    fun areConstraintsSatisfied(): Boolean
}
