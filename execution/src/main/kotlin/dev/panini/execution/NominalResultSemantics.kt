package dev.panini.execution

import dev.panini.execution.binding.referenceKey
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SubantaPada

/** A nominal branch yields a value, without performing or remembering a verbal action. */
object NominalResultSemantics {
    data class Operand(val semantic: SemanticOperand, val fallback: SanskritValue) {
        val literal: SanskritValue? get() = (semantic as? SemanticOperand.Literal)?.value
        val reference: String? get() = (semantic as? SemanticOperand.Reference)?.name
        fun resolve(lookup: (String) -> SanskritValue?): SanskritValue =
            semantic.resolve(lookup) { fallback }
    }

    fun operand(pada: Pada): Operand {
        val nominal = pada as? SubantaPada
        val literal = NominalValueAnalysis.literalValue(pada)
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
        val fallback = NominalValueAnalysis.fallbackValue(pada)
        val semantic = SemanticOperandAnalysis.nominal(pada)
        return Operand(semantic, literal ?: fallback ?: SanskritValue.Shabda(key ?: pada.sourceText))
    }
}
