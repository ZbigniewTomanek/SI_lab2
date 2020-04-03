package model

import csp.ValueHeuristic
import csp.Variable

class SudokuField(private var value: Char, valueHeuristic: ValueHeuristic<Char>)
    : Variable<Char>(valueHeuristic)
{
    private lateinit var domain: MutableList<Char>

    private val domainHistory = mutableListOf<MutableList<Char>>()
    private var valueHistory = mutableListOf<Char>()


    override fun backTrack()
    {
        this.value = valueHistory.removeAt(valueHistory.size - 1)
        this.domain = domainHistory.removeAt(domainHistory.size - 1)
    }

    override fun memorizeState()
    {
        domainHistory.add(domain.toMutableList())
        valueHistory.add(value)
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

    override fun toString() = "Var(val: $value, domain: $domain)"
}