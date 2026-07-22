package dev.panini.execution

import dev.panini.core.Karaka

/** External System Dispatch Action (preṣ / बाह्यप्रेषणम्). */
object BahyaSendAction : DhatuAction("बाह्यप्रेषणम्", "बाह्यतन्त्राय सन्देशप्रेषणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "External dispatch requires a payload/command in KARMAN.")
        val operands = context.resolve(expression)
        val payload = operands.joinToString(" ")

        val effect = operation.effects.firstOrNull { it == ExecutionEffect.NETWORK || it == ExecutionEffect.EXECUTE_PROCESS || it == ExecutionEffect.SEND_MESSAGE }
            ?: ExecutionEffect.NETWORK

        val dispatcher = context.externalDispatcher ?: return ExecutionResult.Failure(
            ExecutionError.ACTION_FAILED,
            "External dispatch requires a dispatcher supplied by the host.",
        )
        val output = dispatcher.dispatch(effect, payload)

        return ExecutionResult.Success(
            output,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Dispatched external effect $effect with payload '$payload'."),
        )
    }
}
