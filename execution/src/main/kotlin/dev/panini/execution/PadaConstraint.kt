package dev.panini.execution

import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SourceCorrection
import dev.panini.vyakaranam.ast.SourceLocatedNode

/**
 * A requirement that fails on one written pada. It carries the pada itself, so the diagnostic points at
 * that word rather than at its clause or sentence.
 */
class PadaConstraintException(
    val pada: Pada,
    message: String,
    val code: String = INVALID_CONSTRUCTION,
    val corrections: List<SourceCorrection> = emptyList(),
) : IllegalArgumentException(message) {
    val diagnostic: ExecutionDiagnostic
        get() = ExecutionDiagnostic(
            code = code,
            message = message.orEmpty(),
            span = (pada as? SourceLocatedNode)?.sourceSpan,
            offendingText = pada.sourceText,
            corrections = corrections,
        )
}

/** Codes for operand requirements that fail on one pada. */
internal object PadaConstraintCode {
    /** Guard: the parser admits only known sup upadeśas, so source cannot reach this. */
    const val UNKNOWN_SUP_ENDING = "UNKNOWN_SUP_ENDING"
    /** Guard: one sup upadeśa has at most one vibhakti per case or kāraka, so source cannot reach this. */
    const val AMBIGUOUS_SUP_SLOT = "AMBIGUOUS_SUP_SLOT"
    const val MISSING_NIRDHARANA_GROUP = "MISSING_NIRDHARANA_GROUP"
    const val AMBIGUOUS_COMPARISON_STANDARD = "AMBIGUOUS_COMPARISON_STANDARD"
    const val AMBIGUOUS_COPULAR_PREDICATE = "AMBIGUOUS_COPULAR_PREDICATE"
    const val DERIVED_COMPARISON_PREDICATE = "DERIVED_COMPARISON_PREDICATE"
    const val UNKNOWN_RANGE_BOUNDARY = "UNKNOWN_RANGE_BOUNDARY"
}

internal inline fun requirePada(condition: Boolean, pada: Pada, code: String, message: () -> String) {
    if (!condition) throw PadaConstraintException(pada, message(), code)
}

internal inline fun <T : Any> requirePadaNotNull(value: T?, pada: Pada, code: String, message: () -> String): T =
    value ?: throw PadaConstraintException(pada, message(), code)
