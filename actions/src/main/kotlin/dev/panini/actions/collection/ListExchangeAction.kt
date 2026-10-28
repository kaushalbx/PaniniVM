package dev.panini.actions.collection

import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.NaturalOperation
import dev.panini.execution.NaturalOperationResolver
import dev.panini.execution.PersistentListOperationException
import dev.panini.execution.exchangeListMembers

/** Persistent exchange of the members at two one-based collection positions. */
object ListExchangeAction : DhatuAction("सूचीस्थानविनिमयः", "सूच्याः स्थानयोः मूल्यविनिमयः") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val frame = NaturalOperationResolver.resolve(operation, context) as? NaturalOperation.IndexedExchange
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Collection exchange requires one list and exactly two one-based positions.",
            )
        val collection = context.resolveValues(frame.collection).singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Collection exchange requires exactly one list value.")
        val first = context.resolveValues(frame.firstIndex).singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Collection exchange requires one first position.")
        val second = context.resolveValues(frame.secondIndex).singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Collection exchange requires one second position.")
        val result = try {
            exchangeListMembers(collection, first, second)
        } catch (error: PersistentListOperationException) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, error.message.orEmpty())
        }
        return ExecutionResult.Success(
            result.toDisplayText(), operation.name,
            listOf("Returned a persistent list with two members exchanged."), result,
        )
    }
}
