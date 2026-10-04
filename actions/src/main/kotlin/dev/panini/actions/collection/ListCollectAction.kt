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
        val items = context.resolveValues(frame.items)
        val list = SanskritValue.Suchi(items)
        return ExecutionResult.Success(
            list.toDisplayText(),
            operation.name,
            listOf("Gathered ${items.size} item(s) into an ordered collection."),
            list,
        )
    }
}
