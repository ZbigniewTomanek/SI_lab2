package model

data class Sudoku(val index: Int, val difficultyLevel: Float, val platform: List<List<Char>>)
{
    override fun toString() = "Sudoku nr $index, diff: $difficultyLevel"
    companion object
    {
        fun getDomainAsChar() = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        const val GRID_SIZE = 9
        const val SUBGRID_SIZE = 3
        const val EMPTY_FIELD_REPR = '.'
    }
}
