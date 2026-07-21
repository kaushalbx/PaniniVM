package dev.panini.execution

import dev.panini.sankhya.SanskritSankhyaGenerator
import dev.panini.sankhya.SanskritSankhyaParser
import java.math.BigInteger

/** Canonical number lookup delegating to SanskritSankhyaGenerator and SanskritSankhyaParser. */
object SanskritNumbers {
    private val generator = SanskritSankhyaGenerator()
    private val parser = SanskritSanskritParserWrapper()

    private val words = listOf(
        "शून्य", "एक", "द्वि", "त्रि", "चतुर्", "पञ्च", "षट्", "सप्त", "अष्ट", "नव", "दश",
        "एकादश", "द्वादश", "त्रयोदश", "चतुर्दश", "पञ्चदश", "षोडश", "सप्तदश", "अष्टादश", "नवदश", "विंशति",
    )
    private val values = words.withIndex().associate { (value, word) -> word to value }

    fun valueOf(word: String): Int? {
        val parsed = parser.parse(word).firstOrNull()?.value?.toInt()
        if (parsed != null) return parsed
        val cleanWord = word.removeSuffix("ः").removeSuffix("म्")
        val cleanParsed = parser.parse(cleanWord).firstOrNull()?.value?.toInt()
        if (cleanParsed != null) return cleanParsed
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

private class SanskritSanskritParserWrapper {
    private val p = SanskritSankhyaParser()
    fun parse(s: String) = p.parse(s)
}
