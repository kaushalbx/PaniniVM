package dev.panini.actions.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.renderSankhyaResult

/** Counting elements in a coordinated expression or collection. */
object SanskritCountingAction : DhatuAction("सङ्ख्यागणनम्", "पदार्थानां सङ्ख्यानम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return dev.panini.actions.missingKaraka(operation, Karaka.KARMAN)
        val operands = context.resolve(expression)
        val count = operands.size.toLong()
        val result = renderSankhyaResult(count) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The count $count is outside the supported Sanskrit number vocabulary.",
            listOf("Counted ${operands.size} elements."),
        )
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Counted ${operands.size} element(s).",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(count, result),
        )
    }
}
