package dev.panini.execution.operations.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Fraction / Ratio / Proportion (bhāga / trairāśika) over Sanskrit number words. */
object SanskritFractionAction : DhatuAction("सङ्ख्याभागः", "सङ्ख्यानां भागः त्रैराशिकं वा") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Fraction/Ratio requires at least 2 number operands.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val res = if (values.size >= 3) {
            val divisor = values[2]
            if (divisor == 0L) return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Division by zero in proportion.", listOf("Selected operation ${operation.name}."))
            val numerator = runCatching { Math.multiplyExact(values[0], values[1]) }
                .getOrElse { return numericOverflow(operation) }
            numerator / divisor
        } else {
            val divisor = values[1]
            if (divisor == 0L) return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Division by zero in fraction.", listOf("Selected operation ${operation.name}."))
            values[0] / divisor
        }
        val result = renderSankhyaResult(res) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The fraction result $res is outside the supported Sanskrit number vocabulary.",
            listOf("Calculated fraction/proportion."),
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Calculated ratio/proportion ${operands.joinToString()}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(res, result),
        )
    }
}
