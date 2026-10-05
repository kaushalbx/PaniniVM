package dev.panini.actions.comparison

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.semanticallyEquals

/** Evaluates the grammatical predicate "X Y-टा समम् अस्ति". */
object CopularEqualityAction : DhatuAction("समता", "समतापरीक्षणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        if (dev.panini.execution.CopularPredicate.from(context.bindings[Karaka.KARMAN]) !=
            dev.panini.execution.CopularPredicate.EQUAL) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Equality requires the predicate सम.")
        }
        val subjects = context.bindings[Karaka.KARTR]?.let(context::resolveValues).orEmpty()
        val standards = context.bindings[Karaka.KARANA]?.let(context::resolveValues).orEmpty()
        if (subjects.size != 1 || standards.size != 1) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Copular equality requires exactly one subject value and one instrumental standard value.",
            )
        }
        val equal = subjects.single().semanticallyEquals(standards.single())
        return ExecutionResult.Success(
            value = if (equal) "सत्यम्" else "असत्यम्",
            operation = operation.name,
            trace = listOf("Compared the nominative subject with the instrumental standard under समम्."),
            typedValue = SanskritValue.Satya(equal),
            conditionValue = equal,
        )
    }

}
