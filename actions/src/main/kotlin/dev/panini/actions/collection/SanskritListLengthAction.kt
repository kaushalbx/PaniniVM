package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.DevanagariDigits
import dev.panini.execution.renderSankhyaResult

/** Count the size/length of a list (triggered by गण / सङ्ख्यान). */
object SanskritListLengthAction : DhatuAction("सूच्याकारः", "सूच्याः दीर्घता-सङ्ख्यानम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List length execution requires a list in KARMAN."
            )

        val listValues = context.resolveValues(expression)
        val firstVal = listValues.firstOrNull()

        val size = when (firstVal) {
            is SanskritValue.Suchi -> firstVal.items.size
            is SanskritValue.Gana -> firstVal.elements.size
            null -> 0
            else -> listValues.size
        }

        val wordResult = renderSankhyaResult(size.toLong()) ?: DevanagariDigits.render(size)
        return ExecutionResult.Success(
            wordResult,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Counted list size: $size ($wordResult).",
            ),
            SanskritValue.Sankhya(size.toLong(), wordResult)
        )
    }
}
