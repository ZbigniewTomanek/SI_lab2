package csp.heuristics

import csp.ValueHeuristic
import java.util.*

class BaselineValueHeuristic : ValueHeuristic<Int>
{
    private lateinit var domain: MutableList<Int>
    private var usedValues: MutableList<Int> = mutableListOf()
    private val usedValuesHistory = Stack<MutableList<Int>>()

    override fun init(domain: List<Int>)
    {
        this.domain = domain as MutableList<Int>
    }

    override fun getNextValue(): Int
    {
        val value =  domain.first { v -> v !in usedValues }
        usedValues.add(value)
        return value
    }

    override fun hasNextValue(): Boolean = domain.size > usedValues.size

    override fun copy(): ValueHeuristic<Int> = BaselineValueHeuristic()

    override fun memorize()
    {
        usedValuesHistory.push(usedValues.toMutableList())
    }

    override fun backtrack()
    {
        usedValues = usedValuesHistory.pop()
    }
}