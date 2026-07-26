package dev.panini.actions.io

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

object SanskritEmitAction : dev.panini.execution.DhatuAction("अर्पणम्", "दानादनयोः अर्पणम् निष्कासनम् च") {
    override fun execute(context: dev.panini.execution.ExecutionContext, operation: dev.panini.execution.DhatuOperation): dev.panini.execution.ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: context.bindings[Karaka.KARTR]
        val target = expression?.let { context.resolve(it).joinToString(" ") } ?: "उत्सर्गः"
        val message = "अर्पितम्: $target"
        return _root_ide_package_.dev.panini.execution.ExecutionResult.Success(
            message,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Emitted $target."),
            _root_ide_package_.dev.panini.execution.SanskritValue.Shabda(message),
        )
    }
}
