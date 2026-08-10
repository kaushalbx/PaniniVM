package dev.panini.actions.io

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.renderSankhyaResult

/** Standard Console Input Action (triggered by ग्रह् / गृह्णीहि). */
object ReadAction : dev.panini.execution.DhatuAction("स्वीकरणम्", "निवेशस्य स्वीकरणम्") {
    override fun execute(context: dev.panini.execution.ExecutionContext, operation: dev.panini.execution.DhatuOperation): dev.panini.execution.ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
        val variableName = expression?.let { context.resolve(it).firstOrNull() } ?: "आगतम्"
        val readValue = context.externalDispatcher
            ?.dispatchOrNull(ExecutionEffect.READ_RESOURCE, variableName)
            ?.trimEnd('\r', '\n')
            ?: "स्वीकृतम्"
        val typedValue = readValue.toSankhyaOrNull(context) ?: SanskritValue.Shabda(readValue)

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
        val normalized = map { character -> devanagariDigits[character] ?: character }.joinToString("")
        val value = normalized.toLongOrNull() ?: return null
        val surface = context.renderSankhyaResult(value) ?: this
        return SanskritValue.Sankhya(value, surface)
    }

    private val devanagariDigits = mapOf(
        '०' to '0', '१' to '1', '२' to '2', '३' to '3', '४' to '4',
        '५' to '5', '६' to '6', '७' to '7', '८' to '8', '९' to '9',
    )
}
