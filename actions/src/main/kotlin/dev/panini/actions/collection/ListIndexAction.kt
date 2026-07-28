package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Get the element at a specific index from a list (1-indexed). */
object ListIndexAction : DhatuAction("सूचीस्थानम्", "सूच्याः निर्दिष्टस्थाने वर्तमानस्य वस्तुनः उद्धरणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val listExpr = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List index execution requires a list in KARMAN."
            )
        val indexExpr = context.bindings[Karaka.KARANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List index execution requires a 1-based index in KARANA."
            )

        val list = context.resolveValues(listExpr)
        val listItems = if (list.size == 1 && list.first() is SanskritValue.Suchi) {
            (list.first() as SanskritValue.Suchi).items
        } else {
            list
        }

        val indexValues = context.resolveValues(indexExpr)
        val indexSankhya = indexValues.filterIsInstance<SanskritValue.Sankhya>().firstOrNull()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Index must be a valid saṅkhyā value."
            )

        val index = indexSankhya.value.toInt() - 1
        if (index < 0 || index >= listItems.size) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Index ${index + 1} out of bounds for list of size ${listItems.size}."
            )
        }

        val element = listItems[index]
        return ExecutionResult.Success(
            element.toDisplayText(),
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Retrieved element at index ${index + 1} -> ${element.toDisplayText()}."
            ),
            element
        )
    }
}
