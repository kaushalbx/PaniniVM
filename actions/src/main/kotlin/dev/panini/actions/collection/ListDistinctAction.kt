package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.semanticallyEquals

/** Retains the first member of every typed-equality class. */
object ListDistinctAction : DhatuAction("सूचीवैशिष्ट्यम्", "समानसदस्यानां प्रथममात्रस्य परिशेषणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return failure("Distinct requires one list or group in KARMAN.")
        val source = context.resolveValues(expression).singleOrNull()
            ?: return failure("Distinct requires exactly one list or group.")
        val members = when (source) {
            is SanskritValue.Suchi -> source.items
            is SanskritValue.Gana -> source.elements
            else -> return failure("Distinct cannot use a scalar collection operand.")
        }
        val distinct = members.fold(mutableListOf<SanskritValue>()) { result, member ->
            if (result.none { it.semanticallyEquals(member) }) result += member
            result
        }
        val value = SanskritValue.Suchi(distinct, (source as? SanskritValue.Suchi)?.memberType)
        return ExecutionResult.Success(
            value.toDisplayText(),
            operation.name,
            listOf("Retained ${distinct.size} first-seen member(s) from ${members.size}."),
            value,
        )
    }

    private fun failure(message: String) = ExecutionResult.Failure(ExecutionError.INVALID_VALUE, message)
}
