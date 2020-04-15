package csp.heuristics

import csp.ValueHeuristic
import java.util.*
import kotlin.random.Random

class RandomValueHeuristic : ValueHeuristic<Int>
{
    private var usedValues: MutableSet<Int> = mutableSetOf()
    private val usedValuesHistory = Stack<MutableSet<Int>>()
    val r = Random(System.currentTimeMillis())


    override fun getNextValue(domain: List<Int>): Int
    {
        var value: Int
        do
        {
            value = domain.random(r)
        } while (value in usedValues)

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