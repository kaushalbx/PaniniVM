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
import kotlin.math.sqrt

/** Square Root (mūla) over Sanskrit number words. */
object SanskritSquareRootAction : DhatuAction("सङ्ख्यामूलम्", "सङ्ख्यायाः वर्गमूलम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return dev.panini.actions.missingKaraka(operation, Karaka.KARMAN)
        val operands = context.resolve(expression)
        val inputStr = operands.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Square root requires a number operand in KARMAN.",
            listOf("Selected operation ${operation.name}."),
        )
        val value = context.resolveSankhyaValues(expression)?.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "'$inputStr' is not an annotated saṅkhyā value.",
            listOf("Selected operation ${operation.name}."),
        )
        if (value < 0) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Square root of negative number $value is undefined.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val root = sqrt(value.toDouble()).toLong()
        if (root > 0 && root > Long.MAX_VALUE / root || root * root != value) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Square root of $value is not an exact integer in the current Sanskrit number model.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val result = renderSankhyaResult(root) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The root result $root is outside the supported Sanskrit number vocabulary.",
            listOf("Sqrt($value).")
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Calculated sqrt($value).",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(root, result),
        )
    }
}
