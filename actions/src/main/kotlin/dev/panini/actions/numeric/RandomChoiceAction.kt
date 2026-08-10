package dev.panini.actions.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.renderSankhyaResult
import dev.panini.execution.activeRange

object RandomChoiceAction : DhatuAction("क्रीडा", "यादृच्छिकचयनम् क्रीडा च") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val activeRange = context.activeRange()
        val minimum = context.numericBound(Karaka.APADANA) ?: activeRange?.minimum?.value
        val maximum = context.numericBound(Karaka.ADHIKARANA) ?: activeRange?.maximum?.value
        if (minimum != null && maximum != null && (minimum > maximum || maximum - minimum > MAX_RANGE_SPAN)) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Random range must be ordered and contain at most ${MAX_RANGE_SPAN + 1} values.",
            )
        }
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.KARTR]
        val resolved = if (minimum != null && maximum != null) {
            (minimum..maximum).map { value ->
                SanskritValue.Sankhya(value, context.renderSankhyaResult(value) ?: value.toString())
            }
        } else {
            expression?.let { context.resolveValues(it) }
                ?: listOf(SanskritValue.Shabda("अक्षः"))
        }
        val options = if (resolved.size == 1 && resolved.first() is SanskritValue.Suchi) {
            (resolved.first() as SanskritValue.Suchi).items
        } else {
            resolved
        }
        val chosen = options.randomOrNull() ?: SanskritValue.Shabda("अक्षः")
        val chosenText = chosen.toDisplayText()
        val message = "चयनम् सिद्धम्: $chosenText"
        return ExecutionResult.Success(
            message,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Random choice from ${options.map { it.toDisplayText() }} -> $chosenText."),
            chosen,
        )
    }

    private fun ExecutionContext.numericBound(karaka: Karaka): Long? =
        bindings[karaka]?.let(::resolveValues)?.singleOrNull()
            ?.let { it as? SanskritValue.Sankhya }?.value

    private const val MAX_RANGE_SPAN = 100_000L
}
