package dev.panini.actions.collection

import dev.panini.execution.*

/** Persistent indexed removal. */
object ListRemoveAtAction : DhatuAction("सूचीस्थाननिष्कासनम्", "सूच्याः स्थानात् सदस्यस्य निष्कासनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val frame = NaturalOperationResolver.resolve(operation, context) as? NaturalOperation.IndexedRemoval
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Indexed removal requires a collection and one-based index.")
        val collection = context.resolveValues(frame.collection).singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Indexed removal requires one collection.")
        val index = context.resolveValues(frame.index).singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Indexed removal requires one index.")
        val result = try { removeListMember(collection, index) }
        catch (error: PersistentListOperationException) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, error.message.orEmpty())
        }
        return ExecutionResult.Success(result.toDisplayText(), operation.name,
            listOf("Returned a persistent list without the selected member."), result)
    }
}

/** Persistent final removal returning both the shortened list and removed value. */
object ListRemoveFinalAction : DhatuAction("सूच्यन्त्यनिष्कासनम्", "सूच्या अन्त्यसदस्यस्य निष्कासनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val frame = NaturalOperationResolver.resolve(operation, context) as? NaturalOperation.FinalRemoval
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Final removal requires one collection.")
        val collection = context.resolveValues(frame.collection).singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Final removal requires one collection.")
        val result = try { removeFinalListMember(collection) }
        catch (error: PersistentListOperationException) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, error.message.orEmpty())
        }
        return ExecutionResult.Success(result.toDisplayText(), operation.name,
            listOf("Returned the shortened list and removed final member."), result)
    }
}
