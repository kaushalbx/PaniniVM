package dev.panini.aryabhatiya

import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana

/**
 * AryabhatiyaEncoder encodes numeric values into Aryabhatiya Devanagari text
 * based on the Geetika 2 rules of Āryabhaṭīya.
 */
class AryabhatiyaEncoder {

    /**
     * Encodes a positive number into its Aryabhatiya Devanagari alphabetic representation.
     * The value must be > 0 and < 10^14 (since maximum vowel power is 6).
     */
    fun encode(value: Long): String {
        require(value > 0) { "Value must be positive: $value" }
        require(value < 100_000_000_000_000L) { "Value exceeds maximum representable value of 10^14 - 1: $value" }

        val sb = StringBuilder()
        var temp = value

        // Find the highest power of 100 that is non-zero
        var highestPower = 0
        var checkVal = value
        for (p in 0..6) {
            if (checkVal % 100 > 0) {
                highestPower = p
            }
            checkVal /= 100
        }

        for (power in 0..6) {
            val d = (temp % 100).toInt()
            temp /= 100

            if (d > 0) {
                val consonants = getConsonantsForValue(d)
                val cluster = consonants.joinToString("्")
                val matra = if (power == 0) {
                    if (power == highestPower) "" else "अ"
                } else {
                    Svara.entries.getOrNull(2 * power)?.matra
                        ?: throw IllegalArgumentException("Invalid power: $power")
                }
                sb.append(cluster).append(matra)
            }
        }

        return sb.toString()
    }

    private fun getConsonantsForValue(d: Int): List<Char> {
        if (d <= 25) {
            val vargaChar = Vyanjana.entries.getOrNull(d - 1)?.devanagari?.first()
                ?: throw IllegalArgumentException("No Varga consonant for value: $d")
            return listOf(vargaChar)
        }
        if (d in 26..29) {
            // Decompose into 20 (न) and (d - 20)
            val v1 = Vyanjana.entries[19].devanagari.first() // 'न' is ordinal 19
            val v2 = Vyanjana.entries[d - 21].devanagari.first()
            return listOf(v1, v2)
        }
        // d >= 30
        val avargaVal = (d / 10) * 10
        val vargaVal = d - avargaVal
        val result = mutableListOf<Char>()
        if (vargaVal > 0) {
            val vChar = Vyanjana.entries.getOrNull(vargaVal - 1)?.devanagari?.first()
                ?: throw IllegalArgumentException("No Varga consonant for value: $vargaVal")
            result.add(vChar)
        }
        val aChar = Vyanjana.entries.getOrNull(avargaVal / 10 + 22)?.devanagari?.first()
            ?: throw IllegalArgumentException("No Avarga consonant for value: $avargaVal")
        result.add(aChar)
        return result
    }
}
