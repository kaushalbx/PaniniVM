package dev.panini.actions.collection

import dev.panini.core.Karaka
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
        val naturalFrame = NaturalOperationResolver.resolve(operation, context)
            as? NaturalOperation.IndexedRetrieval
        val listExpr = (naturalFrame?.collection ?: context.bindings[Karaka.KARMAN])
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List index execution requires a source list in APADANA (or legacy KARMAN)."
            )
        val indexExpr = (naturalFrame?.index ?: context.bindings[Karaka.KARANA])
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List index execution requires a 1-based position in ADHIKARANA (or legacy KARANA)."
            )

        val list = context.resolveValues(listExpr)
        val listItems = when (val whole = list.singleOrNull()) {
            is SanskritValue.Suchi -> whole.items
            is SanskritValue.Gana -> whole.elements
            else -> if (naturalFrame == null) list else return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE, "Indexed retrieval requires one source collection.",
            )
        }

        val indexValues = context.resolveValues(indexExpr)
        val indexSankhya = indexValues.singleOrNull() as? SanskritValue.Sankhya
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Index must resolve to exactly one saṅkhyā value."
            )

        if (indexSankhya.value !in 1L..listItems.size.toLong()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Index ${indexSankhya.value} out of bounds for list of size ${listItems.size}."
            )
        }
        val index = indexSankhya.value.toInt() - 1

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
