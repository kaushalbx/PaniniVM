package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.vyakaranam.ast.DocumentSourceSpan
import dev.panini.vyakaranam.ast.SemanticDiagnostic

enum class OutputKind { INTERNAL, CONSOLE, EXTERNAL }
enum class ExecutionControlSignal { BREAK_LOOP, RETURN_PROCEDURE, HALT_SCRIPT }

/** Stable execution-layer snapshot of a typed semantic diagnostic. */
data class ExecutionDiagnostic(
    val code: String,
    val message: String,
    val span: DocumentSourceSpan? = null,
    val offendingText: String = "",
) {
    companion object {
        fun from(diagnostic: SemanticDiagnostic<*>): ExecutionDiagnostic = ExecutionDiagnostic(
            code = diagnostic.code.toString(),
            message = diagnostic.message,
            span = diagnostic.span,
            offendingText = diagnostic.offendingText,
        )
    }
}

/** The value of a successful execution is always a Sanskrit string. */
sealed interface ExecutionResult {
    enum class LoopOutcome(val sanskritName: String) {
        VIJAYA("विजय"),
        SAMAPTI("समाप्ति"),
    }

    val trace: List<String>

    data class Success(
        val value: String,
        val operation: String,
        override val trace: List<String> = emptyList(),
        val typedValue: SanskritValue? = null,
        val outputKind: OutputKind = OutputKind.INTERNAL,
        val controlSignal: ExecutionControlSignal? = null,
        val conditionValue: Boolean? = null,
        val loopOutcome: LoopOutcome? = null,
        val iterationCount: Long? = null,
    ) : ExecutionResult

    data class Failure(
        val error: ExecutionError,
        val message: String,
        override val trace: List<String> = emptyList(),
        val diagnostic: ExecutionDiagnostic? = null,
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

    data class NeedsApproval(
        val invocationId: String,
        val requiredEffects: Set<ExecutionEffect>,
        val continuation: Any,
        override val trace: List<String> = emptyList(),
    ) : ExecutionResult

    data class NeedsAcceptance(
        val invocationId: String,
        val speaker: String,
        val listener: String,
        val continuation: Any,
        override val trace: List<String> = emptyList(),
    ) : ExecutionResult
}
