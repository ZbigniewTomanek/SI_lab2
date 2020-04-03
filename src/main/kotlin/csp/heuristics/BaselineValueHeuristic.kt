package csp.heuristics

import csp.ValueHeuristic

class BaselineValueHeuristic : ValueHeuristic<Char>
{

    override fun getNextValue(domain: List<Char>): Char
    {
        return domain[0]
    }

    override fun hasNextValue(domain: List<Char>): Boolean = domain.isNotEmpty()
}