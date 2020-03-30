package model.sudoku

import csp.Variable

class SudokuField : Variable<Char>
{

    override fun getDomain(allVariables: List<Any>): Set<Char>
    {
        TODO("Not yet implemented")
    }

    override fun getNextValue(): Char
    {
        TODO("Not yet implemented")
    }

    override fun hasNextValue(): Boolean
    {
        TODO("Not yet implemented")
    }
}