package dev.panini.actions.io

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionResult
import dev.panini.execution.InputRequest
import dev.panini.execution.InputValueType
import dev.panini.execution.SanskritValue
import dev.panini.execution.renderSankhyaResult
import dev.panini.execution.toInputLongOrNull

/** Standard Console Input Action (triggered by ग्रह् / गृह्णीहि). */
object ReadAction : dev.panini.execution.DhatuAction("स्वीकरणम्", "निवेशस्य स्वीकरणम्") {
    override fun execute(context: dev.panini.execution.ExecutionContext, operation: dev.panini.execution.DhatuOperation): dev.panini.execution.ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
        val operands = expression?.let(context::resolve).orEmpty()
        val variableName = operands.firstOrNull() ?: "आगतम्"
        val typeNames = context.bindings[Karaka.SAMPRADANA]?.let(context::resolve).orEmpty()
        val inputType = if (typeNames.any { it in numericTypeNames }) InputValueType.NUMBER else InputValueType.TEXT
        val readValue = context.externalDispatcher
            ?.dispatchOrNull(ExecutionEffect.READ_RESOURCE, InputRequest(variableName, inputType).encode())
            ?.trimEnd('\r', '\n')
            ?: "स्वीकृतम्"
        val typedValue = if (inputType == InputValueType.NUMBER) {
            readValue.toSankhyaOrNull(context) ?: return ExecutionResult.Failure(
                dev.panini.execution.ExecutionError.INVALID_VALUE,
                "Input for $variableName must be a number.",
            )
        } else {
            readValue.toSankhyaOrNull(context) ?: SanskritValue.Shabda(readValue)
        }

        return ExecutionResult.Success(
            typedValue.toDisplayText(),
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Read input into variable $variableName.",
            ),
            typedValue,
        )
    }

    private fun String.toSankhyaOrNull(context: ExecutionContext): SanskritValue.Sankhya? {
        val value = toInputLongOrNull() ?: return null
        val surface = context.renderSankhyaResult(value) ?: this
        return SanskritValue.Sankhya(value, surface)
    }

    private val numericTypeNames = setOf("सङ्ख्या", "संख्या")
}
