package model

data class Sudoku(val index: Int, val difficultyLevel: Float, val platform: List<List<Char>>)
{
    override fun toString() = "Sudoku nr $index, diff: $difficultyLevel"

    fun printPlatform()
    {
        for (i in platform.indices)
        {
            if (i % SUBGRID_SIZE == 0)
                println()

            for (j in (platform[i]).indices)
            {
                if (j % SUBGRID_SIZE == 0)
                    print(" ")
                print("${platform[i][j]} ")
            }

            println()
        }

    }
    companion object
    {
        fun getDomainAsChar() = listOf('1', '2', '3', '4', '5', '6', '7', '8', '9')
        const val GRID_SIZE = 9
        const val SUBGRID_SIZE = 3
        const val EMPTY_FIELD_REPR = '.'
    }
}
