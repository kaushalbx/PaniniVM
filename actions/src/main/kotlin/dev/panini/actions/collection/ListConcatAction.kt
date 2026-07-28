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
                if (singleVal is SanskritValue.Sankhya && singleVal.value == 0L) {
                    val one = SanskritValue.Sankhya(1L, "एकम्")
                    val listValue = SanskritValue.Suchi(listOf(one, one))
                    return ExecutionResult.Success(
                        listValue.toDisplayText(),
                        operation.name,
                        listOf("Selected operation ${operation.name}.", "Initialized Fibonacci loop state with [1, 1]."),
                        listValue
                    )
                } else {
                    val listItems = when (singleVal) {
                        is SanskritValue.Suchi -> singleVal.items
                        is SanskritValue.Gana -> singleVal.elements
                        else -> listOf(singleVal)
                    }
                    if (listItems.size >= 2 && listItems.all { it is SanskritValue.Sankhya }) {
                        val last = listItems[listItems.size - 1] as SanskritValue.Sankhya
                        val secondLast = listItems[listItems.size - 2] as SanskritValue.Sankhya
                        val sum = last.value + secondLast.value
                        val sumWord = dev.panini.execution.renderSankhyaResult(sum) ?: sum.toString()
                        val nextElement = SanskritValue.Sankhya(sum, sumWord)
                        val combined = SanskritValue.Suchi(listItems + nextElement)
                        return ExecutionResult.Success(
                            combined.toDisplayText(),
                            operation.name,
                            listOf(
                                "Selected operation ${operation.name}.",
                                "Appended sum of last two elements (${last.value} + ${secondLast.value} = $sum) to list."
                            ),
                            combined
                        )
                    } else {
                        return ExecutionResult.Failure(
                            ExecutionError.INVALID_VALUE,
                            "List must contain at least 2 elements to grow the Fibonacci/Pingala sequence.",
                            listOf("Selected operation ${operation.name}.")
                        )
                    }
                }
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
