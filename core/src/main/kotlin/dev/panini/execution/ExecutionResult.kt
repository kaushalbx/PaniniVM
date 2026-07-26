package dev.panini.execution

import dev.panini.core.Karaka

/** The value of a successful execution is always a Sanskrit string. */
sealed interface ExecutionResult {
    val trace: List<String>

    data class Success(
        val value: String,
        val operation: String,
        override val trace: List<String> = emptyList(),
        val typedValue: SanskritValue? = null,
    ) : ExecutionResult

    data class Failure(
        val error: ExecutionError,
        val message: String,
        override val trace: List<String> = emptyList(),
    ) : ExecutionResult

    data class Ambiguous(
        val matchingOperations: List<String>,
        val message: String,
        override val trace: List<String> = emptyList(),
    ) : ExecutionResult

    data class NeedsInput(
        val missingKarakas: Set<Karaka>,
        val message: String,
        override val trace: List<String> = emptyList(),
    ) : ExecutionResult
}
