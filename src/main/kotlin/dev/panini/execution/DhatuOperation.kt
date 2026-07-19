package dev.panini.execution

/** One overload of an executable dhātu, selected by its declarative signature. */
data class DhatuOperation(
    val id: String,
    val description: String,
    val signature: OperationSignature,
    val action: DhatuAction,
    val effects: Set<ExecutionEffect> = setOf(ExecutionEffect.PURE),
    val resultSamjnas: Set<ExecutionSamjna> = emptySet(),
) {
    init {
        require(id.isNotBlank()) { "A dhātu operation requires an id." }
    }
}

