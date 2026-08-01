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

object ScaleAction : DhatuAction("वर्धनम्", "सङ्ख्यायाः वर्धनम् (गुणाकारः 2x)") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return dev.panini.actions.missingKaraka(operation, Karaka.KARMAN)
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The operand is not an annotated saṅkhyā value.",
        )
        val scaled = values.first() * 2
        val word = context.renderSankhyaResult(scaled) ?: scaled.toString()
        return ExecutionResult.Success(
            word,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Scaled ${operands.first()} ($values) x 2 -> $word."),
            SanskritValue.Sankhya(scaled, word),
        )
    }
}
