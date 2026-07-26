package dev.panini.katapayadi

/**
 * KatapayadiDecoder decodes Katapayadi Devanagari text into integer values
 * following the traditional rule "अङ्कानां वामतो गतिः" (digits proceed right-to-left).
 */
class KatapayadiDecoder {

    /**
     * Decodes a Katapayadi Devanagari word or phrase into its numeric value.
     */
    fun decode(text: String): Long {
        val digits = extractDigits(text)
        require(digits.isNotEmpty()) { "No valid Katapayadi consonants found in text: '$text'" }

        // Reverse extracted digits according to "अङ्कानां वामतो गतिः"
        val reversedDigits = digits.reversed()
        return reversedDigits.fold(0L) { acc, digit -> acc * 10L + digit }
    }

    /**
     * Extracts digits left-to-right from Devanagari text.
     * In conjunct consonants (e.g. ख्य, ज्ञ, स्म), only the last consonant contributes a digit.
     */
    fun extractDigits(text: String): List<Int> {
        val digits = mutableListOf<Int>()
        var currentConsonant: Char? = null
        var isConjunct = false

        for (i in 0 until text.length) {
            val ch = text[i]

            when {
                ch == '्' -> {
                    isConjunct = true
                }
                KatapayadiMapping.isConsonant(ch) -> {
                    if (currentConsonant != null && !isConjunct) {
                        KatapayadiMapping.getDigit(currentConsonant)?.let { digits.add(it) }
                    }
                    currentConsonant = ch
                    isConjunct = false
                }
                else -> {
                    if (currentConsonant != null) {
                        KatapayadiMapping.getDigit(currentConsonant)?.let { digits.add(it) }
                        currentConsonant = null
                    }
                    isConjunct = false
                }
            }
        }

        if (currentConsonant != null) {
            KatapayadiMapping.getDigit(currentConsonant)?.let { digits.add(it) }
        }

        return digits
    }
}
