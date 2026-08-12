package dev.panini.actions.control

import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionControlSignal
import dev.panini.execution.ExecutionResult
import dev.panini.execution.OutputKind
import dev.panini.execution.SanskritValue

/** Terminates the nearest active bounded loop (vi-sthā, "stop"). */
object LoopBreakAction : DhatuAction("विजयः", "आवृत्तेः निवृत्तिः") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult =
        ExecutionResult.Success(
            value = "विजयः",
            operation = operation.name,
            trace = listOf("Selected operation ${operation.name}.", "Requested termination of the nearest loop."),
            typedValue = SanskritValue.Shabda("विजयः"),
            outputKind = OutputKind.CONSOLE,
            controlSignal = ExecutionControlSignal.BREAK_LOOP,
        )
}
