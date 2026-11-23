package dev.panini.execution

/** Compatibility exception at source-loading APIs; semantic consumers retain [diagnostic]. */
class PvmSemanticException(
    val diagnostic: ExecutionDiagnostic,
) : IllegalArgumentException("${diagnostic.code}: ${diagnostic.message}")

/** The typed diagnostic an exception carries, if it came from an analysis phase that reports one. */
internal val IllegalArgumentException.semanticDiagnostic: ExecutionDiagnostic?
    get() = when (this) {
        is PvmSemanticException -> diagnostic
        is PadaConstraintException -> diagnostic
        is dev.panini.vyakaranam.parser.PaniniSemanticException -> ExecutionDiagnostic.from(diagnostic)
        else -> null
    }

/** Code for a construction-specific requirement that is not met, when no more specific code exists. */
internal const val INVALID_CONSTRUCTION: String = "INVALID_CONSTRUCTION"

/** Code for source the grammar cannot parse at all. */
internal const val SYNTAX_ERROR: String = "SYNTAX_ERROR"

/**
 * The diagnostic for a language-facing failure: the typed one when the analysis phase produced it, otherwise
 * [INVALID_CONSTRUCTION] naming the offending source, so every such failure carries a code.
 */
internal fun IllegalArgumentException.languageDiagnostic(default: String, source: String = ""): ExecutionDiagnostic =
    semanticDiagnostic ?: ExecutionDiagnostic(INVALID_CONSTRUCTION, message ?: default, offendingText = source)

/** A failure that keeps the code, span and corrections of a typed diagnostic instead of its message alone. */
internal fun IllegalArgumentException.toFailure(default: String, source: String = ""): ExecutionResult.Failure {
    val diagnostic = languageDiagnostic(default, source)
    return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, diagnostic.message, diagnostic = diagnostic)
}
