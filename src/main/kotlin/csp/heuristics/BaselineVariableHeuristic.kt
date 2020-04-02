package csp.heuristics

import csp.Variable
import csp.VariableHeuristic
import model.SudokuField

class BaselineVariableHeuristic : VariableHeuristic<Char>
{
    private val variablesUsed: MutableSet<Variable<Char>> = mutableSetOf()

    override fun getNextVariable(variables: List<Variable<Char>>): Variable<Char>
    {
        val toTake = variables.first { v -> v !in variablesUsed }
        variablesUsed.add(toTake)
        return toTake
    }

    override fun hasNextVariable(variables: List<Variable<Char>>): Boolean = variables.any { v -> v !in variablesUsed }
}