package dev.panini.execution

import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.sankhya.SankhyaExpression
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SubantaPada

object PuranaPratyayaResolver {
    private val sankhyaEvaluator = SankhyaEvaluator()
    private val generator = dev.panini.sankhya.SankhyaGenerator()

    internal fun sourceDerivation(pada: SankhyaPuranaPada, position: Long): dev.panini.derivation.DerivationResult {
        val sourceRank = typedOrdinalValue(pada.stems)
        require(sourceRank == null || sourceRank == position) {
            "Ordinal value evidence disagrees with its source morphology."
        }
        if (pada.stems == listOf("प्रथ्", "अमच्") && position == 1L) return generator.ordinal(position)
        val sourceAffix = pada.stems.lastOrNull()
        return generator.ordinalVariants(position).firstOrNull { derivation ->
            derivation.final.terms.any {
                it.kind == dev.panini.derivation.TermKind.PRATYAYA && it.upadesha == sourceAffix
            }
        } ?: throw IllegalArgumentException("No supported ordinal derivation licenses source affix $sourceAffix for $position.")
    }

    fun semanticValue(pada: Pada): SanskritValue.Purana? = ordinalValue(pada)?.let { position ->
        val word = if (pada is SankhyaPuranaPada && pada.striAffix != null)
            OrdinalFeminineDerivation.derive(pada)
        else if (pada is SankhyaPuranaPada) sourceDerivation(pada, position).final.surface
        else generator.ordinal(position).final.surface
        SanskritValue.Purana(position, word)
    }
    /** Returns the semantic ordinal value of a parsed pada, independent of its surface spelling. */
    fun ordinalValue(pada: Pada): Long? {
        return when (pada) {
            is SankhyaPuranaPada -> pada.value ?: typedOrdinalValue(pada.stems)
            is SankhyaPada -> (runCatching { sankhyaEvaluator.evaluateStems(pada.stems) }.getOrNull()
                as? SankhyaExpression.Purana)?.value
            is SubantaPada -> (pada.pratipadika as? MulaPratipadika)
                ?.lexicalOrdinalValue
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
