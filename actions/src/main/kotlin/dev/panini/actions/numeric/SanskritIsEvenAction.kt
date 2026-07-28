package dev.panini.actions.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.resolveSankhyaValues

/** Check if a number is even (युग्म). */
object SanskritIsEvenAction : DhatuAction("युग्मत्वम्", "सङ्ख्यायाः युग्मत्व-परीक्षणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return dev.panini.actions.missingKaraka(operation, Karaka.KARMAN)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The operand is not an annotated saṅkhyā value.",
        )
        if (values.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "No numeric operand provided.",
            )
        }
        val num = values.first()
        val isEven = num % 2 == 0L
        val resultText = if (isEven) "सत्यम्" else "असत्यम्"
        return ExecutionResult.Success(
            resultText,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Checked if $num is even -> $resultText."),
            SanskritValue.Satya(isEven),
        )
    }
}
