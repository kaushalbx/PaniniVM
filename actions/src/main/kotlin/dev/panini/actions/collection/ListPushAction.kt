package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Append item to list (triggered by क्षिप / निक्षिप). */
object ListPushAction : dev.panini.execution.DhatuAction("सूचीनिक्षेपणम्", "सूच्याम् अंशस्य निक्षेपणम्") {
    override fun execute(context: dev.panini.execution.ExecutionContext, operation: dev.panini.execution.DhatuOperation): dev.panini.execution.ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.ADHIKARANA]
        val items = if (expression != null) context.resolveValues(expression) else emptyList()
        val appendedItems = when (val first = items.firstOrNull()) {
            is SanskritValue.Suchi -> first.items + items.drop(1)
            else -> items
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
