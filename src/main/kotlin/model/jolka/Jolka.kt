package model.jolka

class Jolka(private val index: Int,
            private val puzzle: List<List<Char>>,
            private val words: List<String>)
{
    override fun toString() = "Jolka nr $index with words: $words"
    companion object
    {
        const val LETTER_FIELD_REPR = '_'
        const val EMPTY_FIELD_REPR = '#'
    }
}