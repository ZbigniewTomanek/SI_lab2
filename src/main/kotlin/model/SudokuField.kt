package model

import csp.ValueHeuristic
import csp.Variable

class SudokuField(var value: Char, valueHeuristic: ValueHeuristic<Char>)
    : Variable<Char>(valueHeuristic)
{
    lateinit var domain: List<Char>


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

    override fun calculateDomain(variables: List<List<Variable<Char>>>)
    {
        TODO("Not yet implemented")
    }

}