package utils

import model.Jolka
import model.Sudoku
import java.io.File

object Reader
{
    private const val JOLKA_PUZZLE_FILENAME = "puzzle"
    private const val JOLKA_WORDS_FILENAME = "words"
    private const val SUDOKU_PATH = "problem_data/Sudoku.csv"
    private const val JOLKA_PATH = "problem_data/Jolka"

    private lateinit var sudokus: List<Sudoku>

    init
    {
        readSudokus()
    }


    private fun readSudokus()
    {
        var content = File(SUDOKU_PATH).readLines()
        content = content.subList(1, content.size)

        val sudokus = mutableListOf<Sudoku>()

        for (line in content)
        {
            val data = line.split(";")
            sudokus.add(Sudoku(data[0].toInt(), data[1].toFloat(), data[2].toList()))
        }

        this.sudokus = sudokus.toList()
    }


    fun getJolka(index: Int): Jolka
    {
        val words = mutableListOf<String>()
        val puzzle = mutableListOf<List<Char>>()

        val puzzleFilePath = "$JOLKA_PATH/$JOLKA_PUZZLE_FILENAME$index"
        val wordsFilePath = "$JOLKA_PATH/$JOLKA_WORDS_FILENAME$index"

        val puzzleLines = File(puzzleFilePath).readLines()
        puzzleLines.forEach { row ->
            if (row != "\n")
                puzzle.add(row.toList())
        }

        val wordsLines = File(wordsFilePath).readLines()
        wordsLines.forEach {word ->
            if (word != "\n")
                words.add(word)
        }

        return Jolka(index, puzzle, words)
    }

    fun getSudoku(index: Int) = sudokus[index]
    fun getSudoku(difficulty: Float) = sudokus.first { s -> s.difficultyLevel == difficulty }

}