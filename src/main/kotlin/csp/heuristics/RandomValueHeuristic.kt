package csp.heuristics

import csp.ValueHeuristic
import kotlin.random.Random

class RandomValueHeuristic : ValueHeuristic<Char>
{
    val random = Random(System.currentTimeMillis())
    override fun getNextValue(domain: List<Char>): Char
    {
        val index = random.nextInt(domain.size)
        return domain[index]
    }

    override fun hasNextValue(domain: List<Char>): Boolean = domain.isNotEmpty()
}