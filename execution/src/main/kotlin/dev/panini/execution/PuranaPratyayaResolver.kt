package dev.panini.execution

import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.sankhya.SankhyaExpression
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.vyakaranam.ast.Pada

object PuranaPratyayaResolver {
    private val sankhyaEvaluator = SankhyaEvaluator()
    private val sankhyaGenerator = SankhyaGenerator()

    /** Returns the semantic ordinal value of a parsed pada, independent of its surface spelling. */
    fun ordinalValue(pada: Pada): Long? {
        val morphemes = pada.sourceText.split('+').map(String::trim).filter(String::isNotEmpty)
        if (morphemes.size < 2) return null
        val stems = morphemes.dropLast(1)
        (runCatching { sankhyaEvaluator.evaluateStems(stems) }.getOrNull() as? SankhyaExpression.Purana)
            ?.value?.let { return it }
        return (1L..100L).firstOrNull { value ->
            isOrdinal(pada.sourceText, value, sankhyaGenerator.ordinal(value).final.surface)
        }
    }

    private fun isOrdinal(padaSource: String, value: Long, surface: String): Boolean {
        val morphemes = padaSource.split('+').map(String::trim).filter(String::isNotEmpty)
        if (morphemes.size < 2) return false
        val stems = morphemes.dropLast(1)
        val expression = runCatching { sankhyaEvaluator.evaluateStems(stems) }.getOrNull()
        return (expression as? SankhyaExpression.Purana)?.value == value || stems.joinToString("") == surface
    }
}
