package dev.panini.actions.resource

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

object ResourceReleaseAction : DhatuAction("पानम्", "संसाधनस्य उपभोगः मोचनम् च") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.KARTR]
        val resource = expression?.let { context.resolve(it).joinToString(" ") } ?: "संसाधनम्"
        val message = "पानम् सम्पन्नम्: $resource"
        return ExecutionResult.Success(
            message,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Consumed/Released resource $resource."),
            SanskritValue.Shabda(message),
        )
    }
}
