package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.sankhya.SankhyaVacana
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SourceCorrection
import dev.panini.vyakaranam.ast.SourceLocatedNode

/** Typed, source-located numeral diagnostics, so both backends report the same code instead of a crash. */
internal object NumeralDiagnostics {
    const val SEGMENTED_NUMERAL: String = "SEGMENTED_NUMERAL"

    private fun spanOf(pada: Pada) = (pada as? SourceLocatedNode)?.sourceSpan

    /** A cardinal whose written sup has no reading in the numeral's own number. */
    fun numberAgreement(pada: Pada, stem: String?, value: Long, written: SupAffix): PvmSemanticException {
        val span = spanOf(pada)
        val fix = SankhyaVacana.correctedSup(value, written)
        val corrections = if (span != null && fix != null && stem != null) listOf(SourceCorrection(
            span, "$stem + ${fix.upadesha}", "Use the ${fix.upadesha} ending, in the numeral's own number.",
        )) else emptyList()
        return PvmSemanticException(ExecutionDiagnostic(
            code = SankhyaVacana.AGREEMENT_CODE,
            message = SankhyaVacana.mismatchMessage(value, written.vacana),
            span = span,
            offendingText = pada.sourceText,
            corrections = corrections,
        ))
    }

    /** A one-word compound number (एकादशन्), which PVM writes segmented. */
    fun segmentedNumeral(stem: String, segmented: String): PvmSemanticException =
        PvmSemanticException(ExecutionDiagnostic(
            code = SEGMENTED_NUMERAL,
            message = "सङ्ख्या विभक्तरूपेण लेखनीया: '$stem' इत्यस्य स्थाने '$segmented' लिखत। " +
                "(Write the compound number segmented: '$segmented'.)",
            offendingText = stem,
        ))
}
