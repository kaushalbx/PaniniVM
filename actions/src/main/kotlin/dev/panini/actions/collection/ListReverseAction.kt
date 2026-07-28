package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Reverse the order of elements in a list. */
object ListReverseAction : DhatuAction("सूचीविलोमः", "सूच्याः विपरीतीकरणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val listExpr = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List reverse execution requires a list in KARMAN."
            )

        val list = context.resolveValues(listExpr)
        val listItems = if (list.size == 1 && list.first() is SanskritValue.Suchi) {
            (list.first() as SanskritValue.Suchi).items
        } else {
            list
        }

        val reversed = listItems.reversed()
        val displays = reversed.map { it.toDisplayText() }
        return ExecutionResult.Success(
            "[${displays.joinToString(", ")}]",
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Reversed the list."
            ),
            SanskritValue.Suchi(reversed)
        )
    }
}
