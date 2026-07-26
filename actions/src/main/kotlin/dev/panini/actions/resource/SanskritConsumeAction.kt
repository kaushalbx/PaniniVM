package dev.panini.actions.resource

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

object SanskritConsumeAction : DhatuAction("भक्षणम्", "भक्षणम् सम्प्रोक्षणम् च") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.KARTR]
        val target = expression?.let { context.resolve(it).joinToString(" ") } ?: "सामग्री"
        val message = "भक्षणम् सम्पन्नम्: $target"
        return ExecutionResult.Success(
            message,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Consumed $target."),
            SanskritValue.Shabda(message),
        )
    }
}
