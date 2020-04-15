package csp.heuristics

import csp.Variable
import csp.VariableHeuristic

class LeastLimitingVariableHeuristic : VariableHeuristic<Int>()
{
    private val usedVariables = mutableListOf<Variable<Int>>()
    private lateinit var variablesToUse: MutableList<Variable<Int>>

    override fun copy(): VariableHeuristic<Int>
    {
        return LeastLimitingVariableHeuristic()
    }

    override fun init(fields: List<List<Variable<Int>>>)
    {
        this.variablesToUse = fields.flatten().toMutableList()
    }

    override fun getNextVariable(): Variable<Int>
    {

        variablesToUse.sortBy { v -> - v.getDomain().size }
        val nextVar = variablesToUse.removeAt(variablesToUse.size - 1)
        usedVariables.add(nextVar)

        return nextVar
    }

    override fun hasNextVariable(): Boolean = variablesToUse.isNotEmpty()

    override fun getPreviousVariable(): Variable<Int>
    {
        val currVar = usedVariables.removeAt(usedVariables.size - 1)
        variablesToUse.add(currVar)

        return usedVariables[usedVariables.size - 1]
    }


    override fun hasPreviousVariable(): Boolean = usedVariables.size > 1
}