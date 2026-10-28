package dev.panini.actions.collection

import dev.panini.execution.*

/** Persistent insertion before a one-based collection position. */
object ListInsertAtAction : DhatuAction("सूचीस्थाननिक्षेपणम्", "सूच्याः स्थाने मूल्यस्य निक्षेपणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val frame = NaturalOperationResolver.resolve(operation, context) as? NaturalOperation.IndexedInsertion
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Indexed insertion requires a collection, one-based index, and inserted value.")
        val collection = context.resolveValues(frame.collection).singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Indexed insertion requires one collection.")
        val index = context.resolveValues(frame.index).singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Indexed insertion requires one index.")
        val inserted = context.resolveCompleteValues(frame.inserted)?.singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Indexed insertion requires one value.")
        val result = try { insertListMember(collection, index, inserted) }
        catch (error: PersistentListOperationException) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, error.message.orEmpty())
        }
        return ExecutionResult.Success(result.toDisplayText(), operation.name,
            listOf("Returned a persistent list with one member inserted."), result)
    }
}
