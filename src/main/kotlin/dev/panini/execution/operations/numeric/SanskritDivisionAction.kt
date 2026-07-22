package dev.panini.execution.operations.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Division over a coordinated expression of canonical Sanskrit number words. */
object SanskritDivisionAction : DhatuAction("सङ्ख्याहरणम्", "सङ्ख्यानां विभाजनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Division requires at least 2 number operands.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        if (values.drop(1).any { it == 0L }) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Division by zero (शून्य) is undefined.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val quotient = values.drop(1).fold(values.first()) { acc, v -> acc / v }
        val result = renderSankhyaResult(quotient) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $quotient is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" / ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Resolved ${operands.joinToString(" / ")}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(quotient, result),
        )
    }
}
