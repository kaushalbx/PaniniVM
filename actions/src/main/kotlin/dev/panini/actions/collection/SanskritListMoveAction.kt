package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

object SanskritListMoveAction : DhatuAction("नयनम्", "अग्रनयनम् सम्प्रापणम् च") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.KARTR]
        val target = expression?.let { context.resolve(it).joinToString(" ") } ?: "लक्ष्यम्"
        val message = "नयनम् सिद्धम्: $target"
        return ExecutionResult.Success(
            message,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Moved/Led target $target forward."),
            SanskritValue.Shabda(message),
        )
    }
}
