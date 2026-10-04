package dev.panini.execution

import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.sankhya.SankhyaExpression
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada

object PuranaPratyayaResolver {
    private val sankhyaEvaluator = SankhyaEvaluator()
    /** Returns the semantic ordinal value of a parsed pada, independent of its surface spelling. */
    fun ordinalValue(pada: Pada): Long? {
        return when (pada) {
            is SankhyaPuranaPada -> pada.value ?: typedOrdinalValue(pada.stems)
            is SubantaPada -> (pada.pratipadika as? MulaPratipadika)
                ?.lexicalIdentity
                ?.ordinalValue
            else -> null
        }
    }

    private fun typedOrdinalValue(stems: List<String>): Long? {
        if (stems.size < 2) return null
        val base = stems.dropLast(1)
        val suffix = stems.last()
        if (suffix == "अमच्" && base == listOf("प्रथ्")) return 1L
        (runCatching { sankhyaEvaluator.evaluateStems(stems) }.getOrNull()
            as? SankhyaExpression.Purana)?.value?.let { return it }
        if (suffix !in setOf("थ", "म", "तम", "तीय", "अमच्")) return null
        return runCatching { sankhyaEvaluator.evaluateStems(base).value }.getOrNull()
    }
}
