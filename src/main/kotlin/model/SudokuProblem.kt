package model

import csp.CSPProblem
import csp.ValueHeuristic
import csp.Variable
import csp.VariableHeuristic
import model.Sudoku.Companion.getCorrelatedFields
import java.util.*

class SudokuProblem(private val sudokuData: Sudoku,
                    private val valueHeuristic: ValueHeuristic<Int>,
                    variableHeuristic: VariableHeuristic<Int>)
    : CSPProblem<Int, Sudoku>(variableHeuristic)
{
    lateinit var fields: List<List<SudokuField>>
    private lateinit var flattenFields: List<SudokuField>
    private lateinit var currentVariable: SudokuField
    private val correlatedFieldsHistory = Stack<List<SudokuField>>()

    /*
    ---------------
    Initialisation
    ---------------
    */
    init
    {
        initVariables(sudokuData)
        variableHeuristic.init(fields)
    }

    override fun initVariables(data: Sudoku)
    {
        val platform = sudokuData.platform
        val variables = mutableListOf<List<SudokuField>>()

        for (i in platform.indices)
        {   val vRow = mutableListOf<SudokuField>()

            for (j in platform[i].indices)
            {
                val sf = SudokuField(platform[i][j], j, i, valueHeuristic)
                vRow.add(sf)
            }

            vRow.forEach { v -> setVariableDefaultDomain(v) }
            variables.add(vRow.toList())
        }

        this.fields = variables.toList()
        flattenFields = variables.flatten()
    }

    private fun setVariableDefaultDomain(variable: Variable<Int>)
    {
        val vValue = variable.getValue()
        val vDomain = if (vValue != Sudoku.EMPTY_FIELD_REPR)
            listOf(vValue)
        else
            Sudoku.getDomain()

        variable.setDomain(vDomain)
    }

    override fun getSolution(): Sudoku
    {
        val solution = mutableListOf<List<Int>>()

        for (row in fields)
        {
            val sRow = row.map { v -> v.getValue() }
            solution.add(sRow)
        }

        return Sudoku(sudokuData.index, sudokuData.difficultyLevel, solution.toList())
    }

    /*
    ---------------
    Next/prev
    ---------------
    */

    override fun getNextVariable(): Variable<Int> {
        currentVariable = variableHeuristic.getNextVariable() as SudokuField
        currentVariable.memorizeState()
        return currentVariable
    }

    override fun hasNextVariable(): Boolean = variableHeuristic.hasNextVariable()
    override fun hasPreviousVariable(): Boolean = variableHeuristic.hasPreviousVariable()
    override fun getPreviousVariable(): Variable<Int>
    {
        currentVariable = variableHeuristic.getPreviousVariable() as SudokuField
        return currentVariable
    }


    /*
    ---------------
    Backtracking
    ---------------
    */


    override fun areConstraintsSatisfied(): Boolean
            = Sudoku.validatePlatformForField(currentVariable.posX, currentVariable.posY, fields)

    override fun assignValueForVariable(value: Int, variable: Variable<Int>)
    {
        variable.assignValue(value)
    }

    override fun assignValueForVariable(variable: Variable<Int>, value: Int)
    {
        variable.assignValue(value)
    }

    override fun backTrack()
    {
        currentVariable.backTrack()
    }


    /*
    ----------------
    Forward checking
    ----------------
     */

    override fun checkForward()
    {
        val value = currentVariable.getValue()
        val correlatedFields = correlatedFieldsHistory.peek()

        for (field in correlatedFields)
        {
            field.filterDomain(value)
        }
    }

    /*
    ----------------
    FC backtracking
    ----------------
     */

    override fun backTrackFiltering()
    {
        val correlatedFields = correlatedFieldsHistory.peek()
        correlatedFields.forEach { f -> f.recoverDomainElement() }
    }

    override fun saveCorrelatedVarsDomainsState()
    {
        val correlatedFields = getCorrelatedFields(currentVariable, fields)

        correlatedFields.forEach { f -> f.memorizeDomain() }

        correlatedFieldsHistory.push(correlatedFields)
    }

    override fun fcBackTrack()
    {
        val correlatedFields = correlatedFieldsHistory.pop()
        correlatedFields.forEach { f -> f.backTrackDomain() }
        currentVariable.backTrack()
    }

    override fun wouldAnyDomainBeEmpty(value: Int): Boolean {
        val correlatedFields = correlatedFieldsHistory.peek()
        return correlatedFields.any { f -> f.willMakeDomainEmpty(value) }
    }

    override fun toString() = "Sudoku problem nr ${sudokuData.index}, diff: ${sudokuData.difficultyLevel}"
}
