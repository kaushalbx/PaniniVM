package dev.panini.actions.external

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult

/** External System Dispatch Action (preṣ / बाह्यप्रेषणम्). */
object BahyaSendAction : dev.panini.execution.DhatuAction("बाह्यप्रेषणम्", "बाह्यतन्त्राय सन्देशप्रेषणम्") {
    override fun execute(context: dev.panini.execution.ExecutionContext, operation: dev.panini.execution.DhatuOperation): dev.panini.execution.ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return _root_ide_package_.dev.panini.execution.ExecutionResult.Failure(
                _root_ide_package_.dev.panini.execution.ExecutionError.INVALID_VALUE, "External dispatch requires a payload/command in KARMAN.")
        val operands = context.resolve(expression)
        val payload = operands.joinToString(" ")

        val effect = operation.effects.firstOrNull { it == _root_ide_package_.dev.panini.execution.ExecutionEffect.NETWORK || it == _root_ide_package_.dev.panini.execution.ExecutionEffect.EXECUTE_PROCESS || it == _root_ide_package_.dev.panini.execution.ExecutionEffect.SEND_MESSAGE }
            ?: _root_ide_package_.dev.panini.execution.ExecutionEffect.NETWORK

        val dispatcher = context.externalDispatcher ?: return _root_ide_package_.dev.panini.execution.ExecutionResult.Failure(
            _root_ide_package_.dev.panini.execution.ExecutionError.ACTION_FAILED,
            "External dispatch requires a dispatcher supplied by the host.",
        )
        val output = dispatcher.dispatch(effect, payload)

        return _root_ide_package_.dev.panini.execution.ExecutionResult.Success(
            output,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Dispatched external effect $effect with payload '$payload'."),
        )
    }
}
