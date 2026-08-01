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

/** Minimum Selection (kaniṣṭhatva / nyūnatva) over Sanskrit number words. */
object MinAction : DhatuAction("सङ्ख्यान्यूनत्वम्", "सङ्ख्यानां न्यूनत्वम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return dev.panini.actions.missingKaraka(operation, Karaka.KARMAN)
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Minimum selection requires at least 1 number operand.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val minVal = values.minOrNull() ?: 0L
        val result = context.renderSankhyaResult(minVal) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The min result $minVal is outside the supported Sanskrit number vocabulary.",
            listOf("Calculated minimum of ${operands.joinToString()}."),
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Found minimum among ${operands.joinToString()}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(minVal, result),
        )
    }
}
