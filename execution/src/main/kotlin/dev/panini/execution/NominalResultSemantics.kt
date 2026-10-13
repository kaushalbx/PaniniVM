package dev.panini.execution

import dev.panini.execution.binding.NumeralPadaBinder
import dev.panini.execution.binding.referenceKey
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SubantaPada

/** A nominal branch yields a value, without performing or remembering a verbal action. */
object NominalResultSemantics {
    data class Operand(val literal: SanskritValue?, val reference: String?, val fallback: SanskritValue) {
        fun resolve(lookup: (String) -> SanskritValue?): SanskritValue =
            literal ?: reference?.let(lookup) ?: fallback
    }

    fun operand(pada: Pada): Operand {
        val nominal = pada as? SubantaPada
        val ordinal = PuranaPratyayaResolver.semanticValue(pada)
        val lexicalOrdinal = (nominal?.pratipadika as? dev.panini.vyakaranam.ast.MulaPratipadika)?.lexicalOrdinalValue != null
        val literal = if (lexicalOrdinal) null else ordinal ?: NumeralPadaBinder.resolveSemanticValue(pada)
            ?: nominal?.pratipadika?.let(::nominalTruthValue)
        val cardinalSup = when (pada) {
            is dev.panini.vyakaranam.ast.SankhyaPada -> pada.sup
            is SubantaPada -> pada.sup.takeUnless {
                (pada.pratipadika as? dev.panini.vyakaranam.ast.MulaPratipadika)?.lexicalOrdinalValue != null
            }
            else -> null
        }
        if (literal is SanskritValue.Sankhya && cardinalSup != null) {
            val ending = dev.panini.core.SupAffix.candidates(cardinalSup.text)
                .firstOrNull { it.vibhakti == dev.panini.core.Vibhakti.PRATHAMA }
            if (ending != null) dev.panini.sankhya.SankhyaVacana.requireCompatible(literal.value, ending.vacana)
        }
        val key = nominal?.pratipadika?.referenceKey()
        return Operand(literal, key, literal ?: ordinal ?: SanskritValue.Shabda(key ?: pada.sourceText))
    }
}
