package dev.panini.actions.linguistic

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

object SummarizeAction : DhatuAction("अभिषवः", "अभिषवः संक्षेपः च") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.KARTR]
        val text = expression?.let { context.resolve(it).joinToString(" ") } ?: "वाक्यम्"
        val summary = if (text.length > 15) text.take(15) + "..." else text
        val message = "संक्षेपः सिद्धः: $summary"
        return ExecutionResult.Success(
            message,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Summarized text to '$summary'."),
            SanskritValue.Shabda(summary),
        )
    }
}
