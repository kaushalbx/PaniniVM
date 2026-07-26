package dev.panini.actions.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.renderSankhyaResult
import dev.panini.execution.resolveSankhyaValues

/** Modulo (remainder after division) over Sanskrit number words. */
object SanskritModuloAction : DhatuAction("सङ्ख्याशेषः", "सङ्ख्याविभाजनात् शेषः") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Modulo requires at least 2 number operands.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val divisor = values[1]
        if (divisor == 0L) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Modulo by zero (शून्य) is undefined.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val rem = values[0] % divisor
        val result = renderSankhyaResult(rem) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $rem is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands[0]} % ${operands[1]}.")
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Resolved ${operands[0]} % ${operands[1]}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(rem, result),
        )
    }
}

