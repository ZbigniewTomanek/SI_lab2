package utils

import model.Sudoku
import model.SudokuProblem
import java.io.File

object Reader
{
    private const val SUDOKU_PATH = "problem_data/Sudoku.csv"

    private lateinit var sudokus: List<Sudoku>

    fun getSudoku(index: Int) = sudokus[index]
    fun getSudoku(difficulty: Float) = sudokus.first { s -> s.difficultyLevel == difficulty }

    init
    {
        readSudokus()
    }


    private fun stringToSudoku(sudokuString: String): List<List<Char>>
    {
        var index = 0
        var rowList: MutableList<Char>
        val sudokuList = mutableListOf<List<Char>>()

        for (i in 0 until Sudoku.GRID_SIZE)
        {
            rowList = mutableListOf()
            for (j in 0 until  Sudoku.GRID_SIZE)
            {
                rowList.add(sudokuString[index++])
            }

            sudokuList.add(rowList)
        }


        return sudokuList.toList()
    }

    fun readSudokus()
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