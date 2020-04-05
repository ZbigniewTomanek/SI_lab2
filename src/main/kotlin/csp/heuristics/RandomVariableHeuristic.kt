package csp.heuristics

import csp.Variable
import csp.VariableHeuristic
import model.Sudoku
import java.util.*

class RandomVariableHeuristic : VariableHeuristic<Char>
{
    private var currentVariable = -1
    private val variableQueue: List<Int>

    init
    {
        val indicesList = (0 until Sudoku.GRID_SIZE * Sudoku.GRID_SIZE).toMutableList()
        indicesList.shuffle(Random(System.currentTimeMillis()))
        variableQueue = indicesList.toList()
    }

    override fun getNextVariable(variables: List<Variable<Char>>): Variable<Char>{
        return variables[variableQueue[++currentVariable]]
    }


    override fun hasNextVariable(variables: List<Variable<Char>>): Boolean = currentVariable < variableQueue.size - 1

    override fun getPreviousVariable(variables: List<Variable<Char>>): Variable<Char>
            = variables[variableQueue[--currentVariable]]

    override fun hasPreviousVariable(variables: List<Variable<Char>>) = currentVariable > 0

}