package csp.heuristics

import csp.Variable
import csp.VariableHeuristic
import model.Sudoku
import java.util.*
import kotlin.random.Random

class RandomVariableHeuristic : VariableHeuristic<Int>()
{
    private var currentVariable = -1
    private lateinit var variables: List<Variable<Int>>
    private val random = Random(Sudoku.seed)

    override fun copy(): VariableHeuristic<Int>
    {
        return RandomVariableHeuristic()
    }

    override fun init(fields: List<List<Variable<Int>>>)
    {
        val shuffledVars = fields.flatten().toMutableList()
        shuffledVars.shuffle(random)
        variables = shuffledVars.toList()
    }


    override fun getNextVariable(): Variable<Int>{
        return variables[++currentVariable]
    }


    override fun hasNextVariable(): Boolean = currentVariable < variables.size - 1

    override fun getPreviousVariable(): Variable<Int>
            = variables[--currentVariable]

    override fun hasPreviousVariable() = currentVariable > 0
}