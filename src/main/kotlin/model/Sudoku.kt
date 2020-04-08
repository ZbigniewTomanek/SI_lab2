package model

data class Sudoku(val index: Int, val difficultyLevel: Float, val platform: List<List<Int>>)
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
        const val GRID_SIZE = 9
        const val SUBGRID_SIZE = 3
        const val EMPTY_FIELD_REPR = 0

        fun getDomain() = (1..9).toList()

        private fun validatePlatformPart(part: List<SudokuField>): Boolean
        {
            val repetitionCheckSet = mutableSetOf<Int>()

            for (field in part)
            {
                val fieldValue = field.getValue()
                if (fieldValue != EMPTY_FIELD_REPR)
                {
                    if (fieldValue in repetitionCheckSet)
                    {
                        return false
                    }
                    else
                    {
                        repetitionCheckSet.add(fieldValue)
                    }
                }
            }

            return true
        }

        fun validatePlatformForField(posX: Int, posY: Int, fields: List<List<SudokuField>>): Boolean
        {

            val column = mutableListOf<SudokuField>()
            val square = mutableListOf<SudokuField>()
            val row = fields[posY]

            for (i in fields[posX].indices)
            {
                column.add(fields[i][posX])
                square.add(fields
                        [(posY / SUBGRID_SIZE) * SUBGRID_SIZE + i % SUBGRID_SIZE]
                        [(posX / SUBGRID_SIZE) * SUBGRID_SIZE + i / SUBGRID_SIZE])
            }


            if (!validatePlatformPart(column) || !validatePlatformPart(row) || !validatePlatformPart(square))
            {
                return false
            }

            return true
        }

        fun validateWholePlatform(fields: List<List<SudokuField>>): Boolean
        {
            for (i in fields.indices)
            {
                val column = mutableListOf<SudokuField>()
                val square = mutableListOf<SudokuField>()
                val row = fields[i]

                for (j in fields[i].indices)
                {
                    column.add(fields[j][i])
                    square.add(fields
                            [(i / SUBGRID_SIZE) * SUBGRID_SIZE + j / SUBGRID_SIZE]
                            [i * SUBGRID_SIZE % GRID_SIZE + j % SUBGRID_SIZE])
                }

                if (!validatePlatformPart(column) || !validatePlatformPart(row) || !validatePlatformPart(square))
                {
                    return false
                }

            }

            return true
        }

        fun getCorrelatedFields(field: SudokuField, fields: List<List<SudokuField>>): List<SudokuField>
        {
            val posX = field.posX
            val posY = field.posY
            val relatedFields = mutableSetOf<SudokuField>()

            for (i in fields[posX].indices)
            {
                relatedFields.add(fields[i][posX])
                relatedFields.add(fields
                        [(posY / SUBGRID_SIZE) * SUBGRID_SIZE + i % SUBGRID_SIZE]
                        [(posX / SUBGRID_SIZE) * SUBGRID_SIZE + i / SUBGRID_SIZE])
            }

            fields[posX].forEach { f -> relatedFields.add(f) }
            relatedFields.remove(field)
            return relatedFields.filter { f -> f.getValue() == EMPTY_FIELD_REPR }
        }
    }
}
