package dev.panini.execution

import dev.panini.sankhya.SanskritSankhyaGenerator
import java.math.BigInteger

/** Canonical VM number lexicon; generation is delegated to SanskritSankhyaGenerator. */
object SanskritNumbers {
    private val generator = SanskritSankhyaGenerator()

    private val words = listOf(
        "शून्य", "एक", "द्वि", "त्रि", "चतुर्", "पञ्च", "षट्", "सप्त", "अष्ट", "नव", "दश",
        "एकादश", "द्वादश", "त्रयोदश", "चतुर्दश", "पञ्चदश", "षोडश", "सप्तदश", "अष्टादश", "नवदश", "विंशति",
    )
    private val values = words.withIndex().associate { (value, word) -> word to value }
    private val inflectedValues = mapOf(
        "शून्यम्" to 0,
        "एकः" to 1,
        "एका" to 1,
        "एकम्" to 1,
        "द्वे" to 2,
        "त्रयः" to 3,
        "तिस्रः" to 3,
        "त्रीणि" to 3,
        "चत्वारः" to 4,
        "चतस्रः" to 4,
        "चत्वारि" to 4,
    )

    fun valueOf(word: String): Int? {
        inflectedValues[word]?.let { return it }
        val cleanWord = word.removeSuffix("ः").removeSuffix("म्")
        return values[word] ?: values[cleanWord]
    }

    fun wordFor(value: Int): String? {
        if (value < 0) return null
        return try {
            generator.generateDeclinedSurface(BigInteger.valueOf(value.toLong()))
        } catch (e: Exception) {
            words.getOrNull(value)
        }
    }
}
