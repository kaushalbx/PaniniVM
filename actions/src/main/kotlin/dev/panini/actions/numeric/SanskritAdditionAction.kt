package dev.panini.actions.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.numericOverflow
import dev.panini.execution.renderSankhyaResult
import dev.panini.execution.resolveSankhyaValues

/** Addition over a coordinated expression of canonical Sanskrit number words. */
object SanskritAdditionAction : DhatuAction("सङ्ख्यायोजनम्", "सङ्ख्यानां योगः") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The operand is not an annotated saṅkhyā value.",
            listOf("Selected operation ${operation.name}."),
        )
        val sum = runCatching { values.fold(0L, Math::addExact) }.getOrElse {
            return numericOverflow(operation)
        }
        val result = renderSankhyaResult(sum) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $sum is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" + ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Resolved ${operands.joinToString(" + ")}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(sum, result),
        )
    }
}
