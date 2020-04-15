package csp.heuristics

import csp.ValueHeuristic
import java.util.*

class BaselineValueHeuristic : ValueHeuristic<Int>
{
    private var usedValues: MutableSet<Int> = mutableSetOf()
    private val usedValuesHistory = Stack<MutableSet<Int>>()


    override fun getNextValue(domain: List<Int>): Int
    {
        val value =  domain.first { v -> v !in usedValues }
        usedValues.add(value)
        return value
    }

    override fun hasNextValue(domain: List<Int>): Boolean = domain.size > usedValues.size

    override fun copy(): ValueHeuristic<Int> = BaselineValueHeuristic()

    override fun memorize()
    {
        usedValuesHistory.push(usedValues.toMutableSet())
    }

    override fun backtrack()
    {
        usedValues = usedValuesHistory.pop()
    }
}