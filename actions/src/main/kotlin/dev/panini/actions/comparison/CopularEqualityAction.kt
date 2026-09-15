package dev.panini.actions.comparison

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Evaluates the grammatical predicate "X Y-टा समम् अस्ति". */
object CopularEqualityAction : DhatuAction("समता", "समतापरीक्षणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val predicate = context.bindings[Karaka.KARMAN]?.let(context::resolveValues).orEmpty()
        val predicateText = predicate.singleOrNull()?.toDisplayText()
            ?.removeSuffix("म्")
            ?.removeSuffix("ं")
        if (predicateText != "सम") {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Copular equality requires समम् as its predicate.",
            )
        }
        val subjects = context.bindings[Karaka.KARTR]?.let(context::resolveValues).orEmpty()
        val standards = context.bindings[Karaka.KARANA]?.let(context::resolveValues).orEmpty()
        if (subjects.isEmpty() || standards.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Copular equality requires a subject and an instrumental comparison standard.",
            )
        }
        val equal = subjects.any { subject -> standards.any { standard -> subject.equivalentTo(standard) } }
        return ExecutionResult.Success(
            value = if (equal) "सत्यम्" else "असत्यम्",
            operation = operation.name,
            trace = listOf("Compared the nominative subject with the instrumental standard under समम्."),
            typedValue = SanskritValue.Satya(equal),
            conditionValue = equal,
        )
    }

    private fun SanskritValue.equivalentTo(other: SanskritValue): Boolean =
        if (this is SanskritValue.Sankhya && other is SanskritValue.Sankhya) value == other.value
        else toDisplayText() == other.toDisplayText()
}
