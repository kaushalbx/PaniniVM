package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Flatten a list of lists. */
object ListFlattenAction : DhatuAction("सूचीप्रसारणम्", "सूची-प्रसारण क्रिया (फ्लैटन्)") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val listExpr = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List flatten execution requires a list in KARMAN."
            )

        val list = context.resolveValues(listExpr)
        val listItems = if (list.size == 1 && list.first() is SanskritValue.Suchi) {
            (list.first() as SanskritValue.Suchi).items
        } else {
            list
        }

        val flatItems = mutableListOf<SanskritValue>()
        for (item in listItems) {
            if (item is SanskritValue.Suchi) {
                flatItems.addAll(item.items)
            } else {
                flatItems.add(item)
            }
        }

        val displays = flatItems.map { it.toDisplayText() }
        return ExecutionResult.Success(
            "[${displays.joinToString(", ")}]",
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Flattened the list."
            ),
            SanskritValue.Suchi(flatItems)
        )
    }
}
