package dev.panini.katapayadi

import dev.panini.shiksha.Vyanjana

/**
 * KatapayadiEncoder encodes non-negative numeric values into Katapayadi Devanagari text.
 */
class KatapayadiEncoder {

    /**
     * Encodes a non-negative number into its Katapayadi Devanagari representation.
     */
    fun encode(value: Long): String {
        require(value >= 0) { "Value must be non-negative: $value" }

        if (value == 0L) {
            return "ञ" // 'ञ' represents 0 (ordinal 9 in Vyanjana)
        }

        val sb = StringBuilder()
        var temp = value
        while (temp > 0L) {
            val digit = (temp % 10).toInt()
            temp /= 10

            val char = if (digit == 0) {
                Vyanjana.entries[9].devanagari.first() // 'ञ' (NYA) is ordinal 9
            } else {
                Vyanjana.entries[digit - 1].devanagari.first()
            }
            sb.append(char)
        }
        return sb.toString()
    }
}
