package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Pop last item from list (triggered by हृ / हरण). */
object SanskritListPopAction : DhatuAction("सूच्युद्धरणम्", "सूच्याः अन्तिमांशानाम् उद्धरणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List pop execution requires a list in KARMAN."
            )

        val listValues = context.resolveValues(expression)
        val firstVal = listValues.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Target operand resolved to an empty value."
        )

        val items = when (firstVal) {
            is SanskritValue.Suchi -> firstVal.items
            is SanskritValue.Gana -> firstVal.elements
            else -> listValues
        }

        if (items.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Cannot pop from an empty list."
            )
        }

        val popped = items.last()
        return ExecutionResult.Success(
            popped.toDisplayText(),
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Popped element '${popped.toDisplayText()}' from list.",
            ),
            popped
        )
    }
}
