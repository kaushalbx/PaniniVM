package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.NaturalOperation
import dev.panini.execution.NaturalOperationResolver
import dev.panini.execution.SanskritValue
import dev.panini.execution.renderSankhyaResult
import dev.panini.execution.semanticallyEquals

/** Finds the first one-based position of an item in a collection. */
object ListPositionAction : DhatuAction("सूचीस्थानान्वेषणम्", "सूच्यां वस्तुनः प्रथमस्थानस्य अन्वेषणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val frame = NaturalOperationResolver.resolve(operation, context)
            as? NaturalOperation.CollectionPosition
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Collection-position lookup requires a list location and one sought item.",
            )
        val collection = context.resolveValues(frame.collection).singleOrNull()
        val items = when (collection) {
            is SanskritValue.Suchi -> collection.items
            is SanskritValue.Gana -> collection.elements
            else -> return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Collection-position lookup requires exactly one list or group.",
            )
        }
        val sought = context.resolveValues(frame.item).singleOrNull()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Collection-position lookup requires exactly one sought item.",
            )
        val index = items.indexOfFirst { it.semanticallyEquals(sought) }
        if (index < 0) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "The sought item '${sought.toDisplayText()}' is not present in the collection.",
            )
        }
        val position = index.toLong() + 1L
        val word = context.renderSankhyaResult(position)
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "The one-based position $position cannot be rendered as a Sanskrit number.",
            )
        return ExecutionResult.Success(
            word,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Found '${sought.toDisplayText()}' at the first one-based position $position.",
            ),
            SanskritValue.Sankhya(position, word),
        )
    }
}
