package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.NaturalOperation
import dev.panini.execution.NaturalOperationResolver
import dev.panini.execution.SanskritValue

/** Concatenate two lists (triggered by सृज् / संयोजन / संयोग). */
object ListConcatAction : DhatuAction("सूचीसंयोगः", "सूच्योः परस्पर-संयोजनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val naturalFrame = NaturalOperationResolver.resolve(operation, context)
            as? NaturalOperation.CollectionConcatenation

        // The semantic frame is primary. Direct kāraka lookup retains the old
        // सृज् construction during its compatibility window.
        val karmanExpr = naturalFrame?.collection ?: context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "सूचीसंयोगे संयोज्या सूची कर्मरूपेण अपेक्षिता।"
            )

        val karmanValues = context.resolveValues(karmanExpr)

        // In the natural युज् frame, the second collection is the instrument/
        // co-participant of joining. SAMPRADANA remains a legacy सृज् frame.
        val companionExpr = naturalFrame?.companion
            ?: context.bindings[Karaka.KARTR]
            ?: context.bindings[Karaka.KARANA]
            ?: context.bindings[Karaka.SAMPRADANA]
        val (list1, list2) = if (companionExpr != null) {
            val companionValues = context.resolveValues(companionExpr)
            karmanValues to companionValues
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
                    "सूचीसंयोगे द्वितीया सूची करणरूपेण अपेक्षिता।"
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
