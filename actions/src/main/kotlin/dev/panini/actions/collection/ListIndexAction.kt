package dev.panini.actions.collection

import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.NaturalOperation
import dev.panini.execution.NaturalOperationResolver

/** Get the element at a specific index from a list (1-indexed). */
object ListIndexAction : DhatuAction("सूचीस्थानम्", "सूच्याः निर्दिष्टस्थाने वर्तमानस्य वस्तुनः उद्धरणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val naturalFrame = NaturalOperationResolver.resolve(operation, context) as? NaturalOperation.IndexedRetrieval
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Indexed retrieval requires a source collection, a locative position and a result object."
            )
        val list = context.resolveValues(naturalFrame.collection)
        val listItems = when (val whole = list.singleOrNull()) {
            is SanskritValue.Suchi -> whole.items
            is SanskritValue.Gana -> whole.elements
            else -> return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE, "Indexed retrieval requires one source collection.",
            )
        }

        val indexValues = context.resolveValues(naturalFrame.index)
        val position = when (val value = indexValues.singleOrNull()) {
            is SanskritValue.Sankhya -> value.value
            is SanskritValue.Purana -> value.value
            else -> null
        }
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Index must resolve to exactly one cardinal or ordinal position."
            )

        if (position !in 1L..listItems.size.toLong()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Index $position out of bounds for list of size ${listItems.size}."
            )
        }
        val index = position.toInt() - 1

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
