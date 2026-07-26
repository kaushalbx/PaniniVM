package dev.panini.bhutasamkhya

/**
 * BhutasamkhyaDecoder decodes Bhutasamkhya symbolic terms into numeric values.
 */
class BhutasamkhyaDecoder {

    /**
     * Decodes a list of Bhutasamkhya terms (e.g. ["नेत्र", "वेद"]) into a Long value.
     */
    fun decodeTerms(terms: List<String>): Long {
        require(terms.isNotEmpty()) { "Bhutasamkhya terms list cannot be empty." }

        val values = terms.map { term ->
            BhutasamkhyaLexicon.getValue(term)
                ?: error("अज्ञाता भूतसङ्ख्या: '$term'")
        }

        // If all extracted values are single digits (0..9), apply "अङ्कानां वामतो गतिः" (reverse order)
        if (values.all { it in 0L..9L }) {
            val reversedValues = values.reversed()
            return reversedValues.fold(0L) { acc, digit -> acc * 10L + digit }
        }

        // Otherwise sum the individual component values
        return values.sum()
    }

    /**
     * Decodes a single composite or hyphenated Bhutasamkhya string (e.g. "नेत्र-वेद" or "नेत्र वेद").
     */
    fun decode(text: String): Long {
        val terms = text.split("-", " ").map { it.trim() }.filter { it.isNotEmpty() }
        return decodeTerms(terms)
    }
}
