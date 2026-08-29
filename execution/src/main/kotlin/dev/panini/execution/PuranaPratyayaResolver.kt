package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.sankhya.SankhyaExpression
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.parser.PaniniParser

object PuranaPratyayaResolver {
    private val parser = PaniniParser()
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

    /** Replaces parsed pūraṇa parameter padas having the requested ordinal value. */
    fun replacePatterns(text: String, index: Int, rawArgVal: String): String {
        val cleanArg = if (isAccusative(rawArgVal)) rawArgVal else "$rawArgVal + अम्"
        val ukti = parser.parseOrNull(text.trim()) ?: return text
        val ordinalValue = index + 1L
        val ordinalSurface = sankhyaGenerator.ordinal(ordinalValue).final.surface
        val ordinalSources = ukti.grammaticalVakyas().asSequence()
            .flatMap { it.padas.asSequence() }
            .map { it.sourceText }
            .filter { isOrdinal(it, ordinalValue, ordinalSurface) }
            .distinct()
            .toList()
        return ordinalSources.fold(text) { result, source ->
            result.replace(sourcePattern(source), cleanArg)
        }
    }

    private fun isAccusative(source: String): Boolean {
        val padas = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' '))
            ?.grammaticalVakyas()
            ?.flatMap { it.padas }
            ?: return false
        val argument = padas.singleOrNull() as? SubantaPada ?: return false
        return SupAffix.fromUpadesha(argument.sup.text)?.vibhakti == Vibhakti.DVITIYA
    }

    private fun isOrdinal(padaSource: String, value: Long, surface: String): Boolean {
        val morphemes = padaSource.split('+').map(String::trim).filter(String::isNotEmpty)
        if (morphemes.size < 2) return false
        val stems = morphemes.dropLast(1)
        val expression = runCatching { sankhyaEvaluator.evaluateStems(stems) }.getOrNull()
        return (expression as? SankhyaExpression.Purana)?.value == value || stems.joinToString("") == surface
    }

    private fun sourcePattern(source: String): Regex = Regex(
        source.split('+').joinToString("\\s*\\+\\s*") { Regex.escape(it) },
    )
}
