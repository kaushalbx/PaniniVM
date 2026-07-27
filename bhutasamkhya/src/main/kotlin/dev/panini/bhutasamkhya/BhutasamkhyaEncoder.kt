package dev.panini.bhutasamkhya

/**
 * BhutasamkhyaEncoder encodes non-negative numeric values into Bhutasamkhya symbolic terms.
 */
class BhutasamkhyaEncoder {

    /**
     * Encodes a non-negative number into its Bhutasamkhya representation.
     * It first checks if the number has a direct symbolic representation in the lexicon.
     * Otherwise, it decomposes the number digit-by-digit from right-to-left.
     */
    fun encode(value: Long): String {
        require(value >= 0) { "Value must be non-negative: $value" }

        // First, check if there is a direct word for the whole value (e.g. 10 -> "दिक्", 12 -> "सूर्य")
        BhutasamkhyaLexicon.getTerm(value)?.let { return it }

        val terms = mutableListOf<String>()
        var temp = value
        while (temp > 0L) {
            val digit = temp % 10
            temp /= 10

            val word = BhutasamkhyaLexicon.getTerm(digit)
                ?: throw IllegalArgumentException("No symbol found for digit: $digit")
            terms.add(word)
        }

        return terms.joinToString("-")
    }
}
