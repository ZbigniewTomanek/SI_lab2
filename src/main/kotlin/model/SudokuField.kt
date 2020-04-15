package model

import csp.ValueHeuristic
import csp.Variable
import java.util.*

class SudokuField(private var value: Int, val posX: Int, val posY: Int, valueHeuristic: ValueHeuristic<Int>)
    : Variable<Int>(valueHeuristic)
{
    private lateinit var domain: MutableList<Int>

    private val domainHistory = Stack<MutableList<Int>>()
    private val fcDomainHistory = Stack<MutableList<Int>>()
    private val valueHistory = Stack<Int>()

    /*
    ----------------
    Getting value
    ----------------
     */

    override fun getNextValue(): Int
    {
        return valueHeuristic.getNextValue()
    }

    override fun hasNextValue(): Boolean
    {
        return valueHeuristic.hasNextValue()
    }

    override fun assignValue(value: Int)
    {
        this.value = value
    }

    override fun getValue(): Int = value

    /*
    ----------------
    Backtracking
    ----------------
     */

    override fun backtrack()
    {
        this.value = valueHistory.pop()
        this.domain = domainHistory.pop()
        valueHeuristic.init(this.domain)
        valueHeuristic.backtrack()
    }

    override fun memorizeState()
    {
        domainHistory.push(domain.toMutableList())
        valueHistory.push(value)
        valueHeuristic.memorize()
    }


    override fun setDomain(domain: List<Int>)
    {
        this.domain = domain.toMutableList()
        valueHeuristic.init(this.domain)
    }

    override fun getDomain(): List<Int> = domain.toList()

    fun addToDomain(value: Int)
    {
        if (value !in domain)
            domain.add(value)
    }

    /*
    ----------------
    Forward checking
    ----------------
     */

    override fun willMakeDomainEmpty(value: Int): Boolean = domain.size == 1 && value in domain

    override fun backtrackDomain()
    {
        domain = fcDomainHistory.pop()
        valueHeuristic.init(domain)
    }

    override fun memorizeDomain()
    {
        fcDomainHistory.push(domain.toMutableList())
    }

    override fun filterDomain(value: Int)
    {
        domain.remove(value)
        valueHeuristic.init(domain)
    }

    override fun backTrackFiltering(value: Int)
    {
        domain.add(value)
        valueHeuristic.init(domain)
    }


    /*
    ----------------
    Utilities
    ----------------
     */

    override fun toString() = "Var(val: $value, pos: ($posX, $posY)domain: $domain)"

    override fun hashCode(): Int = posX * 10 + posY

    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SudokuField

        if (value != other.value) return false
        if (posX != other.posX) return false
        if (posY != other.posY) return false

        return true
    }

}