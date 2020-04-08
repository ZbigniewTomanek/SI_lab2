package csp.heuristics

import csp.ValueHeuristic

class BaselineValueHeuristic : ValueHeuristic<Int>
{

    override fun getNextValue(domain: List<Int>): Int
    {
        return domain[0]
    }

    override fun hasNextValue(domain: List<Int>): Boolean = domain.isNotEmpty()
}