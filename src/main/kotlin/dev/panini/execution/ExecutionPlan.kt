package dev.panini.execution

data class ExecutionPlan(
    val invocationId: String,
    val resolved: ResolvedOperation,
    val disposition: ExecutionDisposition,
    val requiredEffects: Set<ExecutionEffect>,
    val speaker: String,
    val listener: String,
)
