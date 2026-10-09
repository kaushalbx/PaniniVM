package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.NaturalOperation
import dev.panini.execution.NaturalOperationResolver

/** Slice a list from start index to end index (inclusive, 1-indexed). */
object ListSliceAction : DhatuAction("सूचीविभागः", "सूच्याः एकस्मात् स्थानात् अन्यस्थानं यावत् विभागः") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val naturalFrame = NaturalOperationResolver.resolve(operation, context)
            as? NaturalOperation.CollectionSlice
        val listExpr = naturalFrame?.collection ?: context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List slice execution requires a list in KARMAN."
            )
        val startExpr = naturalFrame?.start ?: context.bindings[Karaka.KARANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List slice execution requires a start index in KARANA."
            )
        val endExpr = naturalFrame?.endInclusive ?: context.bindings[Karaka.SAMPRADANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List slice execution requires an end index in SAMPRADANA."
            )

        val list = context.resolveValues(listExpr)
        val listItems = when (val whole = list.singleOrNull()) {
            is SanskritValue.Suchi -> whole.items
            is SanskritValue.Gana -> whole.elements
            else -> if (naturalFrame == null) list else return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE, "Collection slicing requires one genitive collection.",
            )
        }

        val startLong = (context.resolveValues(startExpr).singleOrNull() as? SanskritValue.Sankhya)?.value
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Start index must resolve to exactly one saṅkhyā value."
            )
        val endLong = (context.resolveValues(endExpr).singleOrNull() as? SanskritValue.Sankhya)?.value
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "End index must resolve to exactly one saṅkhyā value."
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

        val start = (startLong - 1L).coerceAtLeast(0L).toInt()
        val end = endVal.coerceAtMost(listItems.size)

        if (start > end || start >= listItems.size) {
            return ExecutionResult.Success(
                "[]",
                operation.name,
                listOf(
                    "Selected operation ${operation.name}.",
                    "Slice boundaries out of range or empty: $startVal to $endVal."
                ),
                SanskritValue.Suchi(emptyList(), (list.singleOrNull() as? SanskritValue.Suchi)?.memberType)
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
            SanskritValue.Suchi(sliced, (list.singleOrNull() as? SanskritValue.Suchi)?.memberType)
        )
    }
}
