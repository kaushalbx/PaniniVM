package dev.panini.aryabhatiya

import dev.panini.aryabhatiya.AryabhatiyaMapping.ConsonantType
import kotlin.math.pow

/**
 * AryabhatiyaDecoder decodes Aryabhatiya Devanagari text into numeric values
 * based on the Geetika 2 rules of Āryabhaṭīya.
 */
class AryabhatiyaDecoder {

    fun decode(text: String): Long {
        var total = 0L
        val activeConsonants = mutableListOf<Pair<Long, ConsonantType>>()

        var i = 0
        while (i < text.length) {
            val ch = text[i]

            val consonantInfo = AryabhatiyaMapping.getConsonantValue(ch)
            if (consonantInfo != null) {
                activeConsonants.add(consonantInfo)
            } else {
                val vowelPower = AryabhatiyaMapping.getVowelPower(ch)
                if (vowelPower != null && activeConsonants.isNotEmpty()) {
                    total += evaluateCluster(activeConsonants, vowelPower)
                    activeConsonants.clear()
                }
            }
            i++
        }

        // Finalize remaining consonants at implicit vowel 'a' (vowel power 0)
        if (activeConsonants.isNotEmpty()) {
            total += evaluateCluster(activeConsonants, 0)
        }

        return total
    }

    private fun evaluateCluster(consonants: List<Pair<Long, ConsonantType>>, vowelPower: Int): Long {
        var sum = 0L
        val vargaMultiplier = 10.0.pow(2 * vowelPower).toLong()
        val avargaMultiplier = 10.0.pow(2 * vowelPower + 1).toLong()

        for ((value, type) in consonants) {
            sum += when (type) {
                ConsonantType.VARGA -> value * vargaMultiplier
                ConsonantType.AVARGA -> value * vargaMultiplier // Avarga base value already accounts for 10x (e.g. य=30)
            }
        }
        return sum
    }
}
