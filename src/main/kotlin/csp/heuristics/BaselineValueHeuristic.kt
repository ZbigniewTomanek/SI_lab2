package csp.heuristics

import csp.ValueHeuristic

class BaselineValueHeuristic : ValueHeuristic<Char>
{
    private val usedValues = mutableSetOf<Char>()

    override fun getNextValue(domain: List<Char>): Char
    {
        val toPick = domain.first { c -> c !in usedValues }
        usedValues.add(toPick)
        return toPick
    }

    override fun hasNextValue(domain: List<Char>): Boolean = domain.any { c -> c !in usedValues }
}