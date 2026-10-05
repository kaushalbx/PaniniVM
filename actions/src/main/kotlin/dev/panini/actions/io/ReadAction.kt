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
import dev.panini.execution.InputValidation
import dev.panini.execution.SanskritValue
import dev.panini.execution.renderSankhyaResult
import dev.panini.execution.toInputLongOrNull
import dev.panini.execution.toInputBooleanOrNull
import dev.panini.execution.activeRange

/** Standard Console Input Action (triggered by ग्रह् / गृह्णीहि). */
object ReadAction : dev.panini.execution.DhatuAction("स्वीकरणम्", "निवेशस्य स्वीकरणम्") {
    override fun execute(context: dev.panini.execution.ExecutionContext, operation: dev.panini.execution.DhatuOperation): dev.panini.execution.ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
        val variableName = when (expression) {
            is ExecutionExpression.Pada -> expression.prakriti
            is ExecutionExpression.Reference -> expression.name
            else -> expression?.let(context::resolve)?.firstOrNull()
        } ?: "आगतम्"
        val declarationOperands = listOf(Karaka.SAMPRADANA, Karaka.KARANA)
            .flatMap { karaka -> context.bindings[karaka]?.declarationMembers().orEmpty() }
        val typeNames = declarationOperands.mapNotNull { it.declarationIdentity() }
        val declaredTypes = typeNames.mapNotNull { name ->
            when (name) {
                in numericTypeNames -> InputValueType.NUMBER
                in booleanTypeNames -> InputValueType.BOOLEAN
                in choiceTypeNames -> InputValueType.CHOICE
                in textTypeNames -> InputValueType.TEXT
                else -> null
            }
        }.distinct()
        if (declaredTypes.size > 1) {
            return ExecutionResult.Failure(
                dev.panini.execution.ExecutionError.INVALID_VALUE,
                "Input for $variableName has conflicting type declarations: ${declaredTypes.joinToString()}.",
            )
        }
        val inputType = declaredTypes.singleOrNull() ?: InputValueType.TEXT
        val choiceOperands = declarationOperands.filterNot { it.declarationIdentity() in choiceTypeNames }
        if (inputType == InputValueType.CHOICE && choiceOperands.any { context.resolveValues(it).isEmpty() }) {
            return ExecutionResult.Failure(
                dev.panini.execution.ExecutionError.INVALID_VALUE,
                "Each declared choice for $variableName must resolve to a value.",
            )
        }
        val choices = if (inputType == InputValueType.CHOICE) {
            choiceOperands.flatMap(context::resolve).distinct()
        } else {
            emptyList()
        }
        // A discourse range constrains numeric input only.  In particular, it must not
        // turn a text request (or an incorrectly selected overload) into an invalid
        // InputRequest whose type and bounds contradict one another.
        val activeRange = context.activeRange().takeIf { inputType == InputValueType.NUMBER }
        if (inputType == InputValueType.NUMBER) {
            for (role in listOf(Karaka.APADANA, Karaka.ADHIKARANA)) {
                val bound = context.bindings[role] ?: continue
                if (context.resolveValues(bound).singleOrNull() !is SanskritValue.Sankhya) {
                    return ExecutionResult.Failure(
                        dev.panini.execution.ExecutionError.INVALID_VALUE,
                        "Explicit input bound in ${role.sanskritName} must resolve to exactly one number.",
                    )
                }
            }
        }
        val minimum = if (inputType == InputValueType.NUMBER) {
            context.bindings[Karaka.APADANA]
                ?.let(context::resolveValues)?.singleOrNull()
                ?.let { it as? SanskritValue.Sankhya }?.value ?: activeRange?.minimum?.value
        } else {
            null
        }
        val maximum = if (inputType == InputValueType.NUMBER) {
            context.bindings[Karaka.ADHIKARANA]
                ?.let(context::resolveValues)?.singleOrNull()
                ?.let { it as? SanskritValue.Sankhya }?.value ?: activeRange?.maximum?.value
        } else {
            null
        }
        if (inputType == InputValueType.CHOICE && choices.isEmpty()) {
            return ExecutionResult.Failure(
                dev.panini.execution.ExecutionError.INVALID_VALUE,
                "Choice input for $variableName must declare at least one allowed value.",
            )
        }
        if (minimum != null && maximum != null && minimum > maximum) {
            return ExecutionResult.Failure(
                dev.panini.execution.ExecutionError.INVALID_VALUE,
                "The minimum input bound cannot exceed the maximum.",
            )
        }
        val request = InputRequest(variableName, inputType, choices, minimum, maximum)
        val rawReadValue = context.externalDispatcher
            ?.dispatchOrNull(
                ExecutionEffect.READ_RESOURCE,
                request.encode(),
            )
            ?.trimEnd('\r', '\n')
            ?: "स्वीकृतम्"
        val readValue = when (val validation = request.validate(rawReadValue)) {
            is InputValidation.Valid -> validation.value
            is InputValidation.Invalid -> return ExecutionResult.Failure(
                dev.panini.execution.ExecutionError.INVALID_VALUE, validation.message,
            )
        }
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
                SanskritValue.Shabda(readValue)
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

    private fun ExecutionExpression.declarationMembers(): List<ExecutionExpression> = when (this) {
        is ExecutionExpression.Coordination -> members.flatMap { it.declarationMembers() }
        else -> listOf(this)
    }

    /** Classify the retained nominal identity, never its mutable runtime value. */
    private fun ExecutionExpression.declarationIdentity(): String? = when (this) {
        is ExecutionExpression.Pada -> prakriti
        is ExecutionExpression.Reference -> name
        is ExecutionExpression.TypedOperand, is ExecutionExpression.Coordination -> null
    }

    private val numericTypeNames = setOf("सङ्ख्या", "संख्या", "सङ्ख्यात्व", "संख्यात्व", "सङ्ख्यात्वेन", "संख्यात्वेन")
    private val booleanTypeNames = setOf("सत्य", "सत्यम्", "तर्क", "बूलियन")
    private val choiceTypeNames = setOf("विकल्प", "विकल्पः")
    private val textTypeNames = setOf("शब्द", "शब्दः")
}
