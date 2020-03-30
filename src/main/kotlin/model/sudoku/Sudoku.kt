package model.sudoku

import csp.CSPProblem
import csp.Variable

class Sudoku(private val index: Int,
             val difficultyLevel: Float,
             val fields: List<List<Char>>) : CSPProblem<Char>()
{
    override fun toString() = "Sudoku nr $index, diff: $difficultyLevel"

    companion object
    {
        const val GRID_SIZE = 9
        const val SUBGRID_SIZE = 3
        const val EMPTY_FIELD_REPR = '.'
    }

    override fun getNextVariable(): Variable<Char>
    {
        TODO("Not yet implemented")
    }

    override fun hasNextVariable(): Boolean
    {
        TODO("Not yet implemented")
    }
}
