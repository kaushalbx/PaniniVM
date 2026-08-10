package dev.panini.actions.io

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionResult
import dev.panini.execution.InputRequest
import dev.panini.execution.InputValueType
import dev.panini.execution.SanskritValue
import dev.panini.execution.renderSankhyaResult
import dev.panini.execution.toInputLongOrNull
import dev.panini.execution.toInputBooleanOrNull

/** Standard Console Input Action (triggered by ग्रह् / गृह्णीहि). */
object ReadAction : dev.panini.execution.DhatuAction("स्वीकरणम्", "निवेशस्य स्वीकरणम्") {
    override fun execute(context: dev.panini.execution.ExecutionContext, operation: dev.panini.execution.DhatuOperation): dev.panini.execution.ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
        val variableName = when (expression) {
            is ExecutionExpression.Pada -> expression.prakriti
            is ExecutionExpression.Reference -> expression.name
            else -> expression?.let(context::resolve)?.firstOrNull()
        } ?: "आगतम्"
        val typeNames = context.bindings[Karaka.SAMPRADANA]?.let(context::resolve).orEmpty()
        val inputType = when {
            typeNames.any { it in numericTypeNames } -> InputValueType.NUMBER
            typeNames.any { it in booleanTypeNames } -> InputValueType.BOOLEAN
            typeNames.any { it in choiceTypeNames } -> InputValueType.CHOICE
            else -> InputValueType.TEXT
        }
        val choices = if (inputType == InputValueType.CHOICE) {
            typeNames.filterNot { it in choiceTypeNames }.distinct()
        } else {
            emptyList()
        }
        val minimum = context.bindings[Karaka.APADANA]
            ?.let(context::resolveValues)?.singleOrNull()
            ?.let { it as? SanskritValue.Sankhya }?.value
        val maximum = context.bindings[Karaka.ADHIKARANA]
            ?.let(context::resolveValues)?.singleOrNull()
            ?.let { it as? SanskritValue.Sankhya }?.value
        if (inputType == InputValueType.CHOICE && choices.isEmpty()) {
            return ExecutionResult.Failure(
                dev.panini.execution.ExecutionError.INVALID_VALUE,
                "Choice input for $variableName must declare at least one allowed value.",
            )
        }
        val readValue = context.externalDispatcher
            ?.dispatchOrNull(
                ExecutionEffect.READ_RESOURCE,
                InputRequest(variableName, inputType, choices, minimum, maximum).encode(),
            )
            ?.trimEnd('\r', '\n')
            ?: "स्वीकृतम्"
        val typedValue = when (inputType) {
            InputValueType.NUMBER -> readValue.toSankhyaOrNull(context) ?: return ExecutionResult.Failure(
                dev.panini.execution.ExecutionError.INVALID_VALUE, "Input for $variableName must be a number.",
            )
            InputValueType.BOOLEAN -> SanskritValue.Satya(
                readValue.toInputBooleanOrNull() ?: return ExecutionResult.Failure(
                    dev.panini.execution.ExecutionError.INVALID_VALUE, "Input for $variableName must be boolean.",
                ),
            )
            InputValueType.TEXT, InputValueType.CHOICE ->
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
    private val booleanTypeNames = setOf("सत्य", "सत्यम्", "तर्क", "बूलियन")
    private val choiceTypeNames = setOf("विकल्प", "विकल्पः")
}
