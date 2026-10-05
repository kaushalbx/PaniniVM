package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.semanticallyEquals

/** Check if a list contains a specific element. */
object ListContainsAction : DhatuAction("सूच्यस्तित्वम्", "सूच्याम् तत्त्वस्य अस्तित्व-परीक्षणम् (कन्टेन्स्)") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val listExpr = context.bindings[Karaka.ADHIKARANA]
            ?: context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "सूच्यस्तित्वे अधिकरणरूपेण सूची अपेक्षिता।"
            )

        val list = context.resolveValues(listExpr)
        val collection = list.singleOrNull()
        if (Karaka.ADHIKARANA in context.bindings && collection !is SanskritValue.Suchi && collection !is SanskritValue.Gana) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "The locative membership source must be one collection.")
        }
        val listItems = when (collection) {
            is SanskritValue.Suchi -> collection.items
            is SanskritValue.Gana -> collection.elements
            else -> list
        }

        // The natural existential frame makes the entity the nominative kartṛ.
        val queryExpr = context.bindings[Karaka.KARTR]
            ?: context.bindings[Karaka.KARANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "सूच्यस्तित्वे कर्तृरूपेण अन्वेष्यं वस्तु अपेक्षितम्।"
            )

        val queryValues = context.resolveValues(queryExpr)
        if (Karaka.ADHIKARANA in context.bindings && queryValues.size != 1) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Membership requires one subject value.")
        }
        val queryText = queryValues.map { it.toDisplayText() }

        // Check if any element in list matches any query value
        val contains = listItems.any { item -> queryValues.any { query -> item.semanticallyEquals(query) } }

        val trace = listOf(
            "Selected operation ${operation.name}.",
            "Checked whether ${queryText.joinToString()} exists in the locative collection."
        )

        return ExecutionResult.Success(
            if (contains) "सत्यम्" else "असत्यम्",
            operation.name,
            trace,
            SanskritValue.Satya(contains)
        )
    }

}
