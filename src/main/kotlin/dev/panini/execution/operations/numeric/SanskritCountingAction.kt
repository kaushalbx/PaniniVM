package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.execution.operations.numeric.renderSankhyaResult

/** Counting elements in a coordinated expression or collection. */
object SanskritCountingAction : DhatuAction("सङ्ख्यागणनम्", "पदार्थानां सङ्ख्यानम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
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
