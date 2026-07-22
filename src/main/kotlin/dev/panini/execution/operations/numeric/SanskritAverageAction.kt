package dev.panini.execution.operations.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Averaging (sāmyakaraṇa / mādhyama) over Sanskrit number words. */
object SanskritAverageAction : DhatuAction("सङ्ख्यासाम्यम्", "सङ्ख्यानां माध्यमम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Averaging requires at least 1 number operand.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val sum = runCatching { values.fold(0L, Math::addExact) }.getOrElse {
            return numericOverflow(operation)
        }
        val avg = sum / values.size
        val result = renderSankhyaResult(avg) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The average result $avg is outside the supported Sanskrit number vocabulary.",
            listOf("Averaged ${operands.joinToString()}."),
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Averaged ${operands.joinToString()}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(avg, result),
        )
    }
}
