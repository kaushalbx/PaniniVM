package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Check if a list contains a specific element. */
object ListContainsAction : DhatuAction("सूच्यस्तित्वम्", "सूच्याम् तत्त्वस्य अस्तित्व-परीक्षणम् (कन्टेन्स्)") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val listExpr = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List contains check requires a list in KARMAN."
            )

        val list = context.resolveValues(listExpr)
        val listItems = if (list.size == 1 && list.first() is SanskritValue.Suchi) {
            (list.first() as SanskritValue.Suchi).items
        } else {
            list
        }

        // Find query element in KARANA or KARTR
        val queryExpr = context.bindings[Karaka.KARANA]
            ?: context.bindings[Karaka.KARTR]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List contains check requires a query element in KARANA or KARTR."
            )

        val queryValues = context.resolveValues(queryExpr)
        val queryText = queryValues.map { it.toDisplayText() }

        // Check if any element in list matches any query value
        val contains = listItems.any { item ->
            queryText.contains(item.toDisplayText())
        }

        val trace = listOf(
            "Selected operation ${operation.name}.",
            "Checked if list contains elements: $queryText."
        )

        return ExecutionResult.Success(
            if (contains) "सत्यम्" else "असत्यम्",
            operation.name,
            trace,
            SanskritValue.Satya(contains)
        )
    }
}
