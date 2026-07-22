package dev.panini.execution.operations.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Exponentiation (power / ghāta) over Sanskrit number words. */
object SanskritExponentiationAction : DhatuAction("सङ्ख्याघातः", "सङ्ख्यायाः घातवर्धनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Exponentiation requires at least 2 number operands (base and exponent).",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val base = values[0]
        val exp = values[1]
        if (exp < 0 || exp > Int.MAX_VALUE) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Exponent $exp is unsupported.")
        }
        val pow = runCatching {
            var value = 1L
            repeat(exp.toInt()) { value = Math.multiplyExact(value, base) }
            value
        }.getOrElse { return numericOverflow(operation) }
        val result = renderSankhyaResult(pow) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $pow ($base^$exp) is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved $base ^ $exp.")
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Resolved $base ^ $exp.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(pow, result),
        )
    }
}
