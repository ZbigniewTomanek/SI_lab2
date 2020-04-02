package model

import csp.ValueHeuristic
import csp.Variable

class SudokuField(private var value: Char, valueHeuristic: ValueHeuristic<Char>)
    : Variable<Char>(valueHeuristic)
{
    private lateinit var domain: List<Char>


    override fun assignValue(value: Char)
    {
        this.value = value
    }

    override fun getNextValue(): Char
    {
        return valueHeuristic.getNextValue(domain)
    }

    override fun hasNextValue(): Boolean
    {
        return valueHeuristic.hasNextValue(domain)
    }

    override fun setDomain(domain: List<Char>)
    {
        this.domain = domain
    }

    override fun getValue(): Char = value

    override fun toString() = "Var(val: $value, domain: $domain)"
}