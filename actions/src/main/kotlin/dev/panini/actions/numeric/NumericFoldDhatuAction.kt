package dev.panini.actions.numeric

import dev.panini.actions.missingKaraka
import dev.panini.actions.sutra.BlueprintDhatuAction
import dev.panini.core.Karaka
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.numericOverflow
import dev.panini.execution.renderSankhyaResult
import dev.panini.execution.resolveSankhyaValues
import dev.panini.sutra.SutraRole
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraSource

enum class NumericFoldOperator(
    val symbol: String,
    val identity: Long?,
) {
    ADD("+", 0L),
    SUBTRACT("-", null),
    MULTIPLY("*", 1L),
    DIVIDE("/", null),
    MODULO("%", null),
}

/**
 * Shared interpreter for declarative numeric-fold action sūtras.
 */
abstract class NumericFoldDhatuAction(
    id: String,
    source: String,
    name: String,
    description: String,
    operator: NumericFoldOperator,
    minimumOperands: Int,
) : BlueprintDhatuAction(
    name = name,
    description = description,
    blueprint = SutraBlueprint(
        id = SutraId(id),
        source = SutraSource.Program(
            grantha = "dhatu-actions.numeric",
            location = id,
            text = source,
        ),
        role = SutraRole.Vidhi,
        artha = SutraArtha(
            kind = "dhatu-action",
            fields = mapOf(
                "action" to SutraArthaValue.Symbol("numeric-fold"),
                "operator" to SutraArthaValue.Symbol(operator.name),
                "operandKaraka" to SutraArthaValue.Symbol(Karaka.KARMAN.name),
                "minimumOperands" to SutraArthaValue.Number(minimumOperands.toLong()),
                "overflow" to SutraArthaValue.Symbol("reject"),
                "result" to SutraArthaValue.Symbol("sankhya"),
            ),
        ),
    ),
) {
    final override fun executeBlueprint(
        context: ExecutionContext,
        operation: DhatuOperation,
    ): ExecutionResult {
        val fields = blueprint.artha.fields
        val operandKaraka = (fields["operandKaraka"] as SutraArthaValue.Symbol).name
            .let(Karaka::valueOf)
        val expression = context.bindings[operandKaraka]
            ?: return missingKaraka(operation, operandKaraka)
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The operand is not an annotated saṅkhyā value.",
            listOf("Selected action sūtra ${blueprint.id}."),
        )
        val minimumOperands = (fields["minimumOperands"] as SutraArthaValue.Number).value.toInt()
        if (values.size < minimumOperands) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "$name requires at least $minimumOperands number operands.",
                listOf("Selected action sūtra ${blueprint.id}."),
            )
        }
        val operator = NumericFoldOperator.valueOf(
            (fields["operator"] as SutraArthaValue.Symbol).name,
        )
        val resultValue = runCatching {
            when (operator) {
                NumericFoldOperator.ADD -> values.fold(requireNotNull(operator.identity), Math::addExact)
                NumericFoldOperator.SUBTRACT ->
                    values.drop(1).fold(values.first(), Math::subtractExact)
                NumericFoldOperator.MULTIPLY ->
                    values.fold(requireNotNull(operator.identity), Math::multiplyExact)
                NumericFoldOperator.DIVIDE -> {
                    if (values.drop(1).any { it == 0L }) {
                        return ExecutionResult.Failure(
                            ExecutionError.INVALID_VALUE,
                            "Division by zero (शून्य) is undefined.",
                            listOf("Selected action sūtra ${blueprint.id}."),
                        )
                    }
                    values.drop(1).fold(values.first()) { acc, v -> acc / v }
                }
                NumericFoldOperator.MODULO -> {
                    if (values.drop(1).any { it == 0L }) {
                        return ExecutionResult.Failure(
                            ExecutionError.INVALID_VALUE,
                            "Modulo by zero (शून्य) is undefined.",
                            listOf("Selected action sūtra ${blueprint.id}."),
                        )
                    }
                    values.drop(1).fold(values.first()) { acc, v -> acc % v }
                }
            }
        }.getOrElse {
            return numericOverflow(operation)
        }
        val result = renderSankhyaResult(resultValue) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $resultValue is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" ${operator.symbol} ")}."),
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected action sūtra ${blueprint.id}.",
                "Resolved ${operands.joinToString(" ${operator.symbol} ")}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(resultValue, result),
        )
    }
}
