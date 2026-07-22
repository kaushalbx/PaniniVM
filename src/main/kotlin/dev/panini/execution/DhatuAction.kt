package dev.panini.execution

fun interface DhatuAction {
    fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult
}

