package dev.panini.actions.linguistic

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult

/** Morphological subanta derivation from nominal prātipadika stem. */
object SubantaDerivationAction : DhatuAction("पदनिष्पत्तिः", "प्रातिपदिकस्य सुबन्तरूपसिद्धिः") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return dev.panini.actions.missingKaraka(operation, Karaka.KARMAN)
        val operands = context.resolve(expression)
        val stem = operands.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Nominal derivation requires a prātipadika stem in KARMAN.",
            listOf("Selected operation ${operation.name}."),
        )
        return try {
            val handler = context.linguisticServices.deriveSubanta ?: { "$it-स" }
            val result = handler(stem)
            ExecutionResult.Success(
                result,
                operation.name,
                listOf(
                    "Selected operation ${operation.name}.",
                    "Derived subanta for prātipadika '$stem'.",
                    "Produced $result.",
                ),
            )
        } catch (e: Exception) {
            ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "Subanta derivation failed for stem '$stem': ${e.message}",
                listOf("Selected operation ${operation.name}."),
            )
        }
    }
}
