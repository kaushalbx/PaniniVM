package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Append item to list (triggered by क्षिप / निक्षिप). */
object SanskritListPushAction : DhatuAction("सूचीनिक्षेपणम्", "सूच्याम् अंशस्य निक्षेपणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.ADHIKARANA]
        val operands = if (expression != null) context.resolve(expression) else emptyList()
        val items = operands.map { SanskritValue.Shabda(it) }
        val listValue = SanskritValue.Suchi(items)

        return ExecutionResult.Success(
            listValue.toDisplayText(),
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Created/Updated list with ${items.size} item(s): ${listValue.toDisplayText()}.",
            ),
            listValue,
        )
    }
}
