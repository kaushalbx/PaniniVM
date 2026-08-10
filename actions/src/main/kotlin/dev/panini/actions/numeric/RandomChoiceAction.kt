package dev.panini.actions.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

object RandomChoiceAction : DhatuAction("क्रीडा", "यादृच्छिकचयनम् क्रीडा च") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.KARTR]
        val options = expression?.let { context.resolveValues(it) }
            ?: listOf(SanskritValue.Shabda("अक्षः"))
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
}
