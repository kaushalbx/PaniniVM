package dev.panini.execution

import dev.panini.execution.binding.NumeralPadaBinder
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SubantaPada

/** Shared AST-based value evidence, independent of action or procedure dispatch. */
object NominalValueAnalysis {
    /** Value evidence used only after contextual reference lookup fails. */
    fun fallbackValue(pada: Pada): SanskritValue? =
        PuranaPratyayaResolver.semanticValue(pada) ?: numericFallback(pada)

    /** Numeric meaning available after a contextual reference fails to resolve. */
    fun numericFallback(pada: Pada): SanskritValue.Sankhya? = NumeralPadaBinder.resolveSemanticValue(pada)

    /** Intrinsic literals precede lookup; lexical ordinal names remain contextual. */
    fun literalValue(pada: Pada): SanskritValue? {
        val nominal = (pada as? SubantaPada)?.pratipadika
        nominal?.let(::nominalTruthValue)?.let { return it }
        if ((nominal as? MulaPratipadika)?.lexicalOrdinalValue != null) return null
        PuranaPratyayaResolver.semanticValue(pada)?.let { return it }
        return numericFallback(pada)
    }
}
