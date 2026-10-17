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

/** Non-mutating member extraction; legacy object-only calls select the last item. */
object ListPopAction : DhatuAction("सूच्युद्धरणम्", "सूचीसदस्यस्य उद्धरणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val frame = NaturalOperationResolver.resolve(operation, context) as? NaturalOperation.CollectionExtraction
        val member = frame?.member as? dev.panini.execution.ExecutionExpression.Pada
        if (frame != null && member?.memberSelection !in setOf(
                dev.panini.execution.CollectionMemberSelection.FINAL,
                dev.panini.execution.CollectionMemberSelection.ORDINAL)) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Collection extraction requires a final or ordinal member selector.")
        }
        val expression = frame?.collection ?: context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List pop execution requires a list in KARMAN."
            )

        val listValues = context.resolveValues(expression)
        if (frame != null && (listValues.size != 1 ||
                listValues.single() !is SanskritValue.Suchi && listValues.single() !is SanskritValue.Gana)) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Collection extraction requires one collection as its genitive whole.")
        }
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

        val position = if (member?.memberSelection == dev.panini.execution.CollectionMemberSelection.ORDINAL)
            (member.value as? SanskritValue.Purana)?.value else items.size.toLong()
        if (position == null || position !in 1L..items.size.toLong()) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Collection member position is outside the one-based list bounds.")
        }
        val popped = items[(position - 1).toInt()]
        return ExecutionResult.Success(
            popped.toDisplayText(),
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Extracted member $position: '${popped.toDisplayText()}'.",
            ),
            popped
        )
    }
}
