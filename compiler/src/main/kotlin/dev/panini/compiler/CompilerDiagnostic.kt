package dev.panini.compiler

internal enum class CompilerUnsupportedKind {
    INVOCATION,
    CONDITIONAL,
    REPETITION,
    LOOP,
    PIPELINE,
    PROCEDURE,
    IR_VALIDATION,
    UNKNOWN,
}

internal class CompilerUnsupportedException(
    val kind: CompilerUnsupportedKind,
    val source: String,
    detail: String,
) : IllegalArgumentException("[$kind] $detail Source: ${source.trim()}")

internal object CompilerFailureClassifier {
    fun classify(error: Throwable): CompilerUnsupportedKind = when (error) {
        is CompilerUnsupportedException -> error.kind
        else -> when {
            error.message.orEmpty().startsWith("IR ") -> CompilerUnsupportedKind.IR_VALIDATION
            else -> CompilerUnsupportedKind.UNKNOWN
        }
    }
}
