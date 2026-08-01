package dev.panini.actions.io

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Standard Console Output Action (triggered by दृश् / दर्शय). */
object PrintAction : dev.panini.execution.DhatuAction("प्रदर्शनम्", "वाक्यस्य वा सङ्ख्यायाः प्रदर्शनम्") {
    override fun execute(context: dev.panini.execution.ExecutionContext, operation: dev.panini.execution.DhatuOperation): dev.panini.execution.ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.KARTR]
        val operands = if (expression != null) context.resolve(expression) else emptyList()
        val textToPrint = operands.joinToString(" ")

        return ExecutionResult.Success(
            textToPrint,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Printed '$textToPrint'.",
            ),
            dev.panini.execution.SanskritValue.Shabda(textToPrint),
        )
    }
}
