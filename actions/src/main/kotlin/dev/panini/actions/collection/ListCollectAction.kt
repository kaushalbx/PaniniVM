package dev.panini.actions.collection

import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.NaturalOperation
import dev.panini.execution.NaturalOperationResolver
import dev.panini.execution.SanskritValue

/** Gather one or more accusative objects into an ordered list (सम् + ग्रह्). */
object ListCollectAction : DhatuAction("सूचीसङ्ग्रहः", "पदार्थानाम् एकस्यां सूच्याम् सङ्ग्रहः") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val frame = NaturalOperationResolver.resolve(operation, context)
            as? NaturalOperation.CollectionFormation
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Collection with सम् + ग्रह् requires one or more objects in KARMAN.",
            )
        val items = context.resolveCompleteValues(frame.items)?.takeIf { it.isNotEmpty() }
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Collection gathering requires a resolved value for every object.")
        val memberType = context.metadata["listMemberType"]?.let(dev.panini.execution.ListMemberType::valueOf)
        if (memberType != null && items.any { !memberType.accepts(it) }) return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "List members must satisfy the declared $memberType type.",
        )
        val list = SanskritValue.Suchi(items, memberType)
        return ExecutionResult.Success(
            list.toDisplayText(),
            operation.name,
            listOf("Gathered ${items.size} item(s) into an ordered collection."),
            list,
        )
    }
}
