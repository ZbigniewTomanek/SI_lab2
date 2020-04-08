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
    private var valueHistory = Stack<Int>()

    /*
    ----------------
    Getting value
    ----------------
     */

    override fun getNextValue(): Int
    {
        val nextValue = valueHeuristic.getNextValue(domain)
        domain.remove(nextValue)
        return nextValue
    }

    override fun hasNextValue(): Boolean
    {
        return valueHeuristic.hasNextValue(domain)
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

    override fun backTrack()
    {
        this.value = valueHistory.pop()
        this.domain = domainHistory.pop()
    }

    override fun memorizeState()
    {
        domainHistory.push(domain.toMutableList())
        valueHistory.push(value)
    }


    override fun setDomain(domain: List<Int>)
    {
        this.domain = domain.toMutableList()
    }

    override fun getDomain(): List<Int> = domain.toList()


    /*
    ----------------
    Forward checking
    ----------------
     */

    override fun hasEmptyDomain(): Boolean = domain.isEmpty()

    override fun backTrackDomain()
    {
        domain = fcDomainHistory.pop()
    }

    override fun memorizeDomain()
    {

    }

    override fun filterDomain(value: Int)
    {
        fcDomainHistory.push(domain.toMutableList())
        domain.remove(value)
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