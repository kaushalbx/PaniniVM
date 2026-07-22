package dev.panini.execution.operations.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Comparison (maximum selection / tulanā) over Sanskrit number words. */
object SanskritComparisonAction : DhatuAction("सङ्ख्यातुलना", "सङ्ख्यानां तुलना") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Comparison requires at least 1 number operand.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val maxVal = values.maxOrNull() ?: 0L
        val result = renderSankhyaResult(maxVal) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The max result $maxVal is outside the supported Sanskrit number vocabulary.",
            listOf("Compared ${operands.joinToString()}."),
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Compared ${operands.joinToString()}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(maxVal, result),
        )
    }
}
