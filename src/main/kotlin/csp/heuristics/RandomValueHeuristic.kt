package csp.heuristics

import csp.ValueHeuristic
import kotlin.random.Random

//class RandomValueHeuristic : ValueHeuristic<Int>
//{
//    private val random = Random(System.currentTimeMillis())
//    override fun getNextValue(domain: List<Int>): Int
//    {
//        val index = random.nextInt(domain.size)
//        return domain[index]
//    }
//
//    override fun hasNextValue(domain: List<Int>): Boolean = domain.isNotEmpty()
//}