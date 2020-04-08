package csp.heuristics

import csp.Variable
import csp.VariableHeuristic
import model.Sudoku
import java.util.*

class RandomVariableHeuristic : VariableHeuristic<Int>
{
    private var currentVariable = -1
    private val variableQueue: List<Int>

    init
    {
        val indicesList = (0 until Sudoku.GRID_SIZE * Sudoku.GRID_SIZE).toMutableList()
        indicesList.shuffle(Random(System.currentTimeMillis()))
        variableQueue = indicesList.toList()
    }

    override fun getNextVariable(variables: List<Variable<Int>>): Variable<Int>{
        return variables[variableQueue[++currentVariable]]
    }


    override fun hasNextVariable(variables: List<Variable<Int>>): Boolean = currentVariable < variableQueue.size - 1

    override fun getPreviousVariable(variables: List<Variable<Int>>): Variable<Int>
            = variables[variableQueue[--currentVariable]]

    override fun hasPreviousVariable(variables: List<Variable<Int>>) = currentVariable > 0

}