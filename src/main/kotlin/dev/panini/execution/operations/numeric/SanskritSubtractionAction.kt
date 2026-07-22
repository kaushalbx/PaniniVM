package dev.panini.execution.operations.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Subtraction over a coordinated expression of canonical Sanskrit number words. */
object SanskritSubtractionAction : DhatuAction("सङ्ख्यावियोगः", "सङ्ख्यानां वियोगः") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Subtraction requires at least 2 number operands.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val diff = runCatching { values.drop(1).fold(values.first(), Math::subtractExact) }.getOrElse {
            return numericOverflow(operation)
        }
        val result = renderSankhyaResult(diff) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $diff is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" - ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Resolved ${operands.joinToString(" - ")}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(diff, result),
        )
    }
}
