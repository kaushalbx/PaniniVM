package dev.panini.execution

abstract class DhatuAction(
    val name: String,
    val description: String,
) {
    abstract fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult
}
