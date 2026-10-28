package dev.panini.actions.collection

import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.NaturalOperation
import dev.panini.execution.NaturalOperationResolver
import dev.panini.execution.PersistentListOperationException
import dev.panini.execution.replaceListMember

/** Persistent one-based indexed replacement. */
object ListReplaceAction : DhatuAction("सूचीप्रतिस्थापनम्", "सूच्याः स्थाने मूल्यस्य प्रतिस्थापनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val frame = NaturalOperationResolver.resolve(operation, context) as? NaturalOperation.IndexedReplacement
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Indexed replacement requires a collection, one-based index, and replacement value.")
        val collection = context.resolveValues(frame.collection).singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Indexed replacement requires exactly one collection value.")
        val index = context.resolveValues(frame.index).singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Indexed replacement requires exactly one index value.")
        val replacement = context.resolveCompleteValues(frame.replacement)?.singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Indexed replacement requires exactly one replacement value.")
        val result = try {
            replaceListMember(collection, index, replacement)
        } catch (error: PersistentListOperationException) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, error.message.orEmpty())
        }
        return ExecutionResult.Success(result.toDisplayText(), operation.name,
            listOf("Returned a persistent list with one member replaced."), result)
    }
}
