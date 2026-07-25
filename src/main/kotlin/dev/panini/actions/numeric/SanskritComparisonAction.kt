package dev.panini.actions.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.resolveSankhyaValues

/** Comparison (<, >, ==) over canonical Sanskrit number values. */
object SanskritComparisonAction : DhatuAction("सङ्ख्यातुलना", "सङ्ख्यानां तुलना") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
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
        val isGreater = values[0] > values[1]
        val resultText = if (isGreater) "सत्यम्" else "असत्यम्"
        return ExecutionResult.Success(
            resultText,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Compared ${operands[0]} (${values[0]}) > ${operands[1]} (${values[1]}).",
                "Produced $resultText.",
            ),
            SanskritValue.Shabda(resultText),
        )
    }
}
