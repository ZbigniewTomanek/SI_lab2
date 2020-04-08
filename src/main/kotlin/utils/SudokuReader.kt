package utils

import model.Sudoku
import java.io.File

object SudokuReader {
    private const val SUDOKU_PATH = "problem_data/Sudoku.csv"

    private lateinit var sudokus: List<Sudoku>

    fun getSudoku(index: Int) = sudokus[index]
    fun getSudoku(difficulty: Float) = sudokus.first { s -> s.difficultyLevel == difficulty }

    init
    {
        readSudokus()
    }


    private fun stringToSudoku(sudokuString: String): List<List<Int>>
    {
        var index = 0
        var rowList: MutableList<Int>
        val sudokuList = mutableListOf<List<Int>>()

        for (i in 0 until Sudoku.GRID_SIZE)
        {
            rowList = mutableListOf()
            for (j in 0 until  Sudoku.GRID_SIZE)
            {
                val char = sudokuString[index++]
                rowList.add(if (char == '.') 0 else char.toInt() - '0'.toInt())
            }

            sudokuList.add(rowList)
        }


        return sudokuList.toList()
    }

    private fun readSudokus()
    {
        var content = File(SUDOKU_PATH).readLines()
        content = content.subList(1, content.size)

        val sudokus = mutableListOf<Sudoku>()

        for (line in content)
        {
            val data = line.split(";")
            val platform = stringToSudoku(data[2])
            sudokus.add(Sudoku(data[0].toInt(), data[1].toFloat(), platform))
        }

        this.sudokus = sudokus.toList()
    }
}

