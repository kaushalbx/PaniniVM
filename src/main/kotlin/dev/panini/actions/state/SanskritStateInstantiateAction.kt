package dev.panini.actions.state

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

object SanskritStateInstantiateAction : DhatuAction("सत्ता", "वस्तुसद्भावः उत्पत्तिश्च") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.KARTR]
        val entity = expression?.let { context.resolve(it).joinToString(" ") } ?: "वस्तु"
        val message = "सत्ता संजाता: $entity"
        return ExecutionResult.Success(
            message,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Instantiated state for $entity."),
            SanskritValue.Shabda(message),
        )
    }
}
