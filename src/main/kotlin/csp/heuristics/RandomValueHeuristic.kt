package csp.heuristics

import csp.ValueHeuristic

class RandomValueHeuristic : ValueHeuristic<Char>
{
    private val valuesUsed: MutableSet<Char> = mutableSetOf()
    override fun getNextValue(domain: List<Char>): Char
    {
        TODO("Not yet implemented")
    }

    override fun hasNextValue(domain: List<Char>): Boolean
    {
        TODO("Not yet implemented")
    }
}