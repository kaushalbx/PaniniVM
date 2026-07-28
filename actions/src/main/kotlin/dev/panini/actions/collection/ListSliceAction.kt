package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Slice a list from start index to end index (inclusive, 1-indexed). */
object ListSliceAction : DhatuAction("सूचीविभागः", "सूच्याः एकस्मात् स्थानात् अन्यस्थानं यावत् विभागः") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val listExpr = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List slice execution requires a list in KARMAN."
            )
        val startExpr = context.bindings[Karaka.KARANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List slice execution requires a start index in KARANA."
            )
        val endExpr = context.bindings[Karaka.SAMPRADANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List slice execution requires an end index in SAMPRADANA."
            )

        val list = context.resolveValues(listExpr)
        val listItems = if (list.size == 1 && list.first() is SanskritValue.Suchi) {
            (list.first() as SanskritValue.Suchi).items
        } else {
            list
        }

        val startLong = context.resolveValues(startExpr).filterIsInstance<SanskritValue.Sankhya>().firstOrNull()?.value
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Start index must be a valid saṅkhyā value."
            )
        val endLong = context.resolveValues(endExpr).filterIsInstance<SanskritValue.Sankhya>().firstOrNull()?.value
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "End index must be a valid saṅkhyā value."
            )
        if (startLong !in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong() ||
            endLong !in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()
        ) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Slice indices are outside the supported range."
            )
        }
        val startVal = startLong.toInt()
        val endVal = endLong.toInt()

        val start = (startVal - 1).coerceAtLeast(0)
        val end = endVal.coerceAtMost(listItems.size)

        if (start > end || start >= listItems.size) {
            return ExecutionResult.Success(
                "[]",
                operation.name,
                listOf(
                    "Selected operation ${operation.name}.",
                    "Slice boundaries out of range or empty: $startVal to $endVal."
                ),
                SanskritValue.Suchi(emptyList())
            )
        }

        val sliced = listItems.subList(start, end)
        val displays = sliced.map { it.toDisplayText() }
        return ExecutionResult.Success(
            "[${displays.joinToString(", ")}]",
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Sliced list from index $startVal to $endVal."
            ),
            SanskritValue.Suchi(sliced)
        )
    }
}
