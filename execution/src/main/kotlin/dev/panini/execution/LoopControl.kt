package dev.panini.execution

/** Keep the action's value visible while consuming its nearest-loop control effect. */
internal fun ExecutionResult.consumeLoopBreak(): ExecutionResult =
    if (this is ExecutionResult.Success && controlSignal == ExecutionControlSignal.BREAK_LOOP)
        copy(controlSignal = null) else this
