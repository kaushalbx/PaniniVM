package dev.panini.execution

import dev.panini.dhatupatha.Dhatu

/** Compatibility facade for executing one already-structured dhātu invocation. */
object ExecutionEngine {
    fun execute(dhatu: Dhatu, context: ExecutionContext): ExecutionResult {
        val invocation = DhatuInvocation(
            id = "invocation",
            dhatu = dhatu,
            bindings = context.bindings,
            selectedOperation = context.selectedOperation,
            metadata = context.metadata,
        )
        return when (val resolution = OperationResolver.resolve(invocation, context.variables)) {
            is OperationResolution.Resolved -> resolution.value.operation.action.execute(
                resolution.value.context.copy(
                    stateStore = context.stateStore,
                    externalDispatcher = context.externalDispatcher,
                ),
                resolution.value.operation,
            )
            is OperationResolution.MissingInput -> ExecutionResult.NeedsInput(resolution.karakas, resolution.message)
            is OperationResolution.Invalid -> ExecutionResult.Failure(resolution.error, resolution.message)
            is OperationResolution.Ambiguous -> ExecutionResult.Ambiguous(resolution.operations, resolution.message)
        }
    }
}
