package dev.panini.actions.comparison

import dev.panini.actions.missingKaraka
import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.resolveSankhyaValues
import dev.panini.execution.CopularPredicate

/** Evaluates न्यून/अधिक with a nominative subject and an ablative standard. */
object CopularOrderAction : DhatuAction("न्यूनता", "एकस्य मूल्यस्य अन्यस्मात् न्यूनत्वम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val predicate = CopularPredicate.from(context.bindings[Karaka.KARMAN])
        if (predicate !in setOf(CopularPredicate.LESS_THAN, CopularPredicate.GREATER_THAN)) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Ordering requires the predicate न्यून or अधिक.")
        }
        val subject = context.bindings[Karaka.KARTR] ?: return missingKaraka(operation, Karaka.KARTR)
        val standard = context.bindings[Karaka.APADANA] ?: return missingKaraka(operation, Karaka.APADANA)
        val left = context.resolveSankhyaValues(subject)?.singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "The subject of ordering must be a number.")
        val right = context.resolveSankhyaValues(standard)?.singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "The ablative standard of comparison must be a number.")
        val result = if (predicate == CopularPredicate.LESS_THAN) left < right else left > right
        return ExecutionResult.Success(
            value = if (result) "सत्यम्" else "असत्यम्",
            operation = operation.name,
            typedValue = SanskritValue.Satya(result),
            conditionValue = result,
        )
    }
}
