package dev.panini.compiler

import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult

/** Preserves a PaniniVM execution failure across the generated JVM method boundary. */
class CompiledPaniniExecutionException(
    val error: ExecutionError,
    override val message: String,
    val trace: List<String> = emptyList(),
) : IllegalStateException("$error: $message") {
    companion object {
        internal fun from(result: ExecutionResult, context: String): CompiledPaniniExecutionException =
            when (result) {
                is ExecutionResult.Failure -> CompiledPaniniExecutionException(
                    result.error,
                    result.message,
                    result.trace,
                )
                is ExecutionResult.Ambiguous -> CompiledPaniniExecutionException(
                    ExecutionError.AMBIGUOUS_OPERATION,
                    result.message,
                    result.trace,
                )
                is ExecutionResult.NeedsInput -> CompiledPaniniExecutionException(
                    ExecutionError.MISSING_KARAKA,
                    result.message,
                    result.trace,
                )
                else -> CompiledPaniniExecutionException(
                    ExecutionError.ACTION_FAILED,
                    "$context returned ${result::class.simpleName} instead of a value.",
                    result.trace,
                )
            }
    }
}
