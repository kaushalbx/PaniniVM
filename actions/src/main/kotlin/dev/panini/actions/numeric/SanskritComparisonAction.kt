package dev.panini.actions.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.resolveSankhyaValues

enum class ComparisonType {
    GREATER_THAN,
    LESS_THAN,
    EQUALS,
}

/** Comparison (<, >, ==) over canonical Sanskrit number values. */
class SanskritComparisonAction(
    val comparisonType: ComparisonType = ComparisonType.GREATER_THAN,
) : DhatuAction("सङ्ख्यातुलना", "सङ्ख्यानां तुलना") {

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return dev.panini.actions.missingKaraka(operation, Karaka.KARMAN)
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The operand is not an annotated saṅkhyā value.",
            listOf("Selected operation ${operation.name}."),
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Comparison requires at least 2 numeric operands.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val isTrue = when (comparisonType) {
            ComparisonType.GREATER_THAN -> values[0] > values[1]
            ComparisonType.LESS_THAN -> values[0] < values[1]
            ComparisonType.EQUALS -> values[0] == values[1]
        }
        val resultText = if (isTrue) "सत्यम्" else "असत्यम्"
        val opSymbol = when (comparisonType) {
            ComparisonType.GREATER_THAN -> ">"
            ComparisonType.LESS_THAN -> "<"
            ComparisonType.EQUALS -> "=="
        }
        return ExecutionResult.Success(
            resultText,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Compared ${operands[0]} (${values[0]}) $opSymbol ${operands[1]} (${values[1]}).",
                "Produced $resultText.",
            ),
            SanskritValue.Shabda(resultText),
        )
    }

    companion object {
        val GreaterThan = SanskritComparisonAction(ComparisonType.GREATER_THAN)
        val LessThan = SanskritComparisonAction(ComparisonType.LESS_THAN)
        val Equals = SanskritComparisonAction(ComparisonType.EQUALS)
    }
}
