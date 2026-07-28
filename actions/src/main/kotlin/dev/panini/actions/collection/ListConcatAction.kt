package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Concatenate two lists (triggered by सृज् / संयोजन / संयोग). */
object ListConcatAction : DhatuAction("सूचीसंयोगः", "सूच्योः परस्पर-संयोजनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        // Resolve first list operand from KARMAN
        val karmanExpr = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List concatenation requires a list in KARMAN."
            )

        val karmanValues = context.resolveValues(karmanExpr)

        // Resolve second list operand from SAMPRADANA
        val sampradanaExpr = context.bindings[Karaka.SAMPRADANA]
        val (list1, list2) = if (sampradanaExpr != null) {
            val sampradanaValues = context.resolveValues(sampradanaExpr)
            karmanValues to sampradanaValues
        } else {
            // If SAMPRADANA is absent, check if KARMAN is a Coordination of multiple lists
            if (karmanValues.size >= 2) {
                val first = karmanValues.first()
                val second = karmanValues.drop(1)
                listOf(first) to second
            } else if (karmanValues.size == 1) {
                val singleVal = karmanValues.first()
                val listItems = when (singleVal) {
                    is SanskritValue.Suchi -> singleVal.items
                    is SanskritValue.Gana -> singleVal.elements
                    else -> listOf(singleVal)
                }
                listItems to emptyList()
            } else {
                return ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "List concatenation requires a second list in SAMPRADANA or as a coordinated KARMAN."
                )
            }
        }

        // Unpack list items (Suchi, Gana, or simple list)
        fun unpack(values: List<SanskritValue>): List<SanskritValue> = when (val first = values.firstOrNull()) {
            is SanskritValue.Suchi -> first.items
            is SanskritValue.Gana -> first.elements
            else -> values
        }

        val items1 = unpack(list1)
        val items2 = unpack(list2)
        val combined = SanskritValue.Suchi(items1 + items2)

        return ExecutionResult.Success(
            combined.toDisplayText(),
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Concatenated list of size ${items1.size} with list of size ${items2.size} -> total size ${items1.size + items2.size}.",
            ),
            combined
        )
    }
}
