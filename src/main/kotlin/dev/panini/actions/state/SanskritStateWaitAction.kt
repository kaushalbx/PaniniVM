package dev.panini.actions.state

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

object SanskritStateWaitAction : DhatuAction("स्थितिः", "स्थानस्थितिः प्रतीक्षा च") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.KARTR]
        val location = expression?.let { context.resolve(it).joinToString(" ") } ?: "अत्र"
        val message = "स्थितिः संजाता: $location"
        return ExecutionResult.Success(
            message,
            operation.name,
            listOf("Selected operation ${operation.name}.", "State waiting at $location."),
            SanskritValue.Shabda(message),
        )
    }
}
