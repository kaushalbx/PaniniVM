package dev.panini.execution

internal fun Phala.toExecutionResult(operation: String): ExecutionResult = when (this) {
    is Phala.Siddha -> ExecutionResult.Success(
        value = values.values.lastOrNull() ?: "",
        operation = operation,
        trace = trace,
        typedValue = values.keys.lastOrNull()?.let(typedValues::get),
        outputKind = outputKind,
        controlSignal = controlSignal,
        conditionValue = (values.keys.lastOrNull()?.let(typedValues::get) as? SanskritValue.Satya)?.boolean,
    )
    is Phala.Asiddha -> result
    is Phala.AnumatiApekshita -> pipelineContinuation?.let { resumable ->
        ExecutionResult.NeedsApproval(
            invocationId = invocationId,
            requiredEffects = effects,
            continuation = resumable,
            trace = continuation.trace,
        )
    } ?: ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Approval continuation is unavailable.")
    is Phala.SvikaraApekshita -> pipelineContinuation?.let { resumable ->
        ExecutionResult.NeedsAcceptance(
            invocationId = invocationId,
            speaker = speaker,
            listener = listener,
            continuation = resumable,
            trace = continuation.trace,
        )
    } ?: ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Acceptance continuation is unavailable.")
    is Phala.Nirasta -> ExecutionResult.Failure(ExecutionError.ACTION_FAILED, reason)
    else -> ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Execution resulted in $this")
}
