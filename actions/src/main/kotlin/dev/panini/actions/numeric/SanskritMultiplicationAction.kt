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

/** Multiplication over a coordinated expression of canonical Sanskrit number words. */
object SanskritMultiplicationAction : DhatuAction("सङ्ख्यागुणनम्", "सङ्ख्यानां गुणनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return dev.panini.actions.missingKaraka(operation, Karaka.KARMAN)
        val operands = context.resolve(expression)

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Multiplication requires at least 2 number operands.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val product = runCatching { values.fold(1L, Math::multiplyExact) }.getOrElse {
            return numericOverflow(operation)
        }
        val result = renderSankhyaResult(product) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $product is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" * ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Resolved ${operands.joinToString(" * ")}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(product, result),
        )
    }
}
