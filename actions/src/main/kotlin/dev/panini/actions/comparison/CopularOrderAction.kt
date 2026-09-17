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

/** Evaluates the grammatical proposition “X is less than Y.” */
object CopularOrderAction : DhatuAction("न्यूनता", "एकस्य मूल्यस्य अन्यस्मात् न्यूनत्वम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val subject = context.bindings[Karaka.KARTR] ?: return missingKaraka(operation, Karaka.KARTR)
        val standard = context.bindings[Karaka.APADANA] ?: return missingKaraka(operation, Karaka.APADANA)
        val predicate = context.bindings[Karaka.KARMAN] ?: return missingKaraka(operation, Karaka.KARMAN)
        val predicateText = context.resolve(predicate).singleOrNull()?.removeSuffix("म्")?.removeSuffix("ं")
        if (predicateText != "न्यून") {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Copular order requires न्यूनम् as its predicate.",
            )
        }
        val left = context.resolveSankhyaValues(subject)?.singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "The subject of न्यूनम् must be a number.")
        val right = context.resolveSankhyaValues(standard)?.singleOrNull()
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "The ablative standard of comparison must be a number.")
        val result = left < right
        return ExecutionResult.Success(
            value = if (result) "सत्यम्" else "असत्यम्",
            operation = operation.name,
            typedValue = SanskritValue.Satya(result),
            conditionValue = result,
        )
    }
}
