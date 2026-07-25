package dev.panini.actions.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

object SanskritRandomChoiceAction : DhatuAction("क्रीडा", "यादृच्छिकचयनम् क्रीडा च") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.KARTR]
        val options = expression?.let { context.resolve(it) } ?: listOf("अक्षः")
        val chosen = options.randomOrNull() ?: "अक्षः"
        val message = "चयनम् सिद्धम्: $chosen"
        return ExecutionResult.Success(
            message,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Random choice from $options -> $chosen."),
            SanskritValue.Shabda(chosen),
        )
    }
}
