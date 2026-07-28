package dev.panini.actions.state

import dev.panini.actions.missingKaraka
import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SutraAddress
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraIntrospector

/** Pure reflection over a host-supplied, immutable sūtra-grantha registry. */
object SutraInspectAction : DhatuAction(
    "सूत्रदर्शनम्",
    "सूत्रस्य अर्थस्य च निरीक्षणम्",
) {
    override fun execute(
        context: ExecutionContext,
        operation: DhatuOperation,
    ): ExecutionResult {
        val sutraExpression = context.bindings[Karaka.KARMAN]
            ?: return missingKaraka(operation, Karaka.KARMAN)
        val sutraId = context.resolve(sutraExpression).firstOrNull()
            ?.let(::SutraId)
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Sūtra inspection requires a sūtra identity in KARMAN.",
            )
        val registry = context.sutraRegistry ?: return ExecutionResult.Failure(
            ExecutionError.ACTION_FAILED,
            "Sūtra inspection requires a SutraGranthaRegistry supplied by the host.",
        )
        val granthaOrAlias = context.bindings[Karaka.ADHIKARANA]
            ?.let(context::resolve)
            ?.firstOrNull()
        val currentGrantha = context.currentGrantha
        val sutra = if (currentGrantha != null) {
            registry.resolve(
                requester = currentGrantha,
                alias = granthaOrAlias ?: currentGrantha.value,
                sutraId = sutraId,
            )
        } else {
            granthaOrAlias?.let { registry.sutra(SutraAddress(GranthaId(it), sutraId)) }
        } ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Sūtra $sutraId is not visible in the requested grantha scope.",
        )

        val field = context.bindings[Karaka.KARANA]
            ?.let(context::resolve)
            ?.firstOrNull()
        val value = if (field == null) {
            SutraIntrospector.describe(sutra)
        } else {
            SutraIntrospector.arthaField(sutra, field)
                ?: return ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "Sūtra $sutraId has no semantic field '$field'.",
                )
        }
        return ExecutionResult.Success(
            value = value.toDisplayText(),
            operation = operation.name,
            trace = listOf(
                "Selected operation ${operation.name}.",
                "Inspected sūtra $sutraId${field?.let { " field '$it'" } ?: ""}.",
            ),
            typedValue = value,
        )
    }
}
