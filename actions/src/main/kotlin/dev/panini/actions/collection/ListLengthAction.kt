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
import dev.panini.execution.DevanagariDigits
import dev.panini.execution.renderSankhyaResult

/** Count the size/length of a list (triggered by गण / सङ्ख्यान). */
object ListLengthAction : DhatuAction("सूच्याकारः", "सूच्याः दीर्घता-सङ्ख्यानम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val naturalFrame = NaturalOperationResolver.resolve(operation, context)
            as? NaturalOperation.CollectionCardinality
        val expression = naturalFrame?.collection ?: context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "सूचीगणने सूची कर्मरूपेण अपेक्षिता।"
            )

        val listValues = context.resolveValues(expression)
        val firstVal = listValues.firstOrNull()

        val size = when (firstVal) {
            is SanskritValue.Suchi -> firstVal.items.size
            is SanskritValue.Gana -> firstVal.elements.size
            null -> 0
            else -> listValues.size
        }

        val wordResult = context.renderSankhyaResult(size.toLong()) ?: DevanagariDigits.render(size)
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
