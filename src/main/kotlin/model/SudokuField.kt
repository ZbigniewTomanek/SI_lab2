package model

import csp.ValueHeuristic
import csp.Variable

class SudokuField(private var value: Char, valueHeuristic: ValueHeuristic<Char>)
    : Variable<Char>(valueHeuristic)
{
    private lateinit var domain: MutableList<Char>

    private val domainHistory = mutableListOf<MutableList<Char>>()
    private var valueHistory = mutableListOf(value)


    override fun backTrack()
    {
        this.value = valueHistory.removeAt(valueHistory.size - 2)
        this.domain = domainHistory.removeAt(domainHistory.size - 2)
    }


    override fun assignValue(value: Char)
    {
        valueHistory.add(value)
        domainHistory.add(domain)

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
        domainHistory.add(this.domain)
    }

    override fun getValue(): Char = value

    override fun toString() = "Var(val: $value, domain: $domain)"
}