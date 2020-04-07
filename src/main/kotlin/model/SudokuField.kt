package model

import csp.ValueHeuristic
import csp.Variable
import java.util.*

class SudokuField(private var value: Char, val posX: Int, val posY: Int, valueHeuristic: ValueHeuristic<Char>)
    : Variable<Char>(valueHeuristic)
{
    private lateinit var domain: MutableList<Char>

    private val domainHistory = Stack<MutableList<Char>>()
    private val fcDomainHistory = Stack<MutableList<Char>>()
    private var valueHistory = Stack<Char>()


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


    override fun assignValue(value: Char)
    {
        this.value = value
    }

    override fun getNextValue(): Char
    {
        val nextValue = valueHeuristic.getNextValue(domain)
        domain.remove(nextValue)
        return nextValue
    }

    override fun hasNextValue(): Boolean
    {
        return valueHeuristic.hasNextValue(domain)
    }

    override fun setDomain(domain: List<Char>)
    {
        this.domain = domain.toMutableList()
    }

    override fun getDomain(): List<Char> = domain.toList()

    override fun getValue(): Char = value

    override fun hasEmptyDomain(): Boolean = domain.isEmpty()

    override fun backTrackDomain()
    {
        domain = fcDomainHistory.pop()
    }

    override fun memorizeDomain()
    {

    }

    override fun filterDomain(value: Char)
    {
        fcDomainHistory.push(domain.toMutableList())
        domain.remove(value)
    }

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