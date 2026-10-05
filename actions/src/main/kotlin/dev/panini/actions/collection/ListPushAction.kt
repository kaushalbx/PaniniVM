package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.NaturalOperation
import dev.panini.execution.NaturalOperationResolver

/** Append item to list (triggered by क्षिप / निक्षिप). */
object ListPushAction : dev.panini.execution.DhatuAction("सूचीनिक्षेपणम्", "सूच्याम् अंशस्य निक्षेपणम्") {
    override fun execute(context: dev.panini.execution.ExecutionContext, operation: dev.panini.execution.DhatuOperation): dev.panini.execution.ExecutionResult {
        val naturalFrame = NaturalOperationResolver.resolve(operation, context)
            as? NaturalOperation.CollectionInsertion
        val objectExpression = naturalFrame?.item ?: context.bindings[Karaka.KARMAN]
        val locationExpression = naturalFrame?.collection
        val objects = objectExpression?.let(context::resolveValues).orEmpty()
        val locations = locationExpression?.let(context::resolveValues).orEmpty()
        val appendedItems = if (locationExpression != null) {
            val collection = locations.singleOrNull() as? SanskritValue.Suchi
                ?: return dev.panini.execution.ExecutionResult.Failure(
                    dev.panini.execution.ExecutionError.INVALID_VALUE,
                    "Insertion with नि + क्षिप् requires a list in ADHIKARANA.",
                    listOf("Selected operation ${operation.name}."),
                )
            collection.items + objects
        } else {
            when (val first = objects.firstOrNull()) {
                is SanskritValue.Suchi -> first.items + objects.drop(1)
                else -> objects
            }
        }
        val listValue = SanskritValue.Suchi(appendedItems)

        return dev.panini.execution.ExecutionResult.Success(
            listValue.toDisplayText(),
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Created/Updated list with ${appendedItems.size} item(s): ${listValue.toDisplayText()}.",
            ),
            listValue,
        )
    }
}
