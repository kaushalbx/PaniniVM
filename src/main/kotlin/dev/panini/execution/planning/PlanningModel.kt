package dev.panini.execution

data class ActionDependency(val before: String, val after: String)

data class ExecutionProgram(
    val ukti: Ukti,
    val dependencies: Set<ActionDependency> = emptySet(),
) {
    val invocations: List<DhatuInvocation> get() = ukti.invocations
}

data class ExecutionPlan(
    val invocationId: String,
    val resolved: ResolvedOperation,
    val disposition: ExecutionDisposition,
    val requiredEffects: Set<ExecutionEffect>,
    val speaker: String,
    val listener: String,
)

sealed interface PlanningResult {
    data class Planned(val program: ExecutionProgram, val plans: List<ExecutionPlan>) : PlanningResult
    data class Failed(val result: ExecutionResult) : PlanningResult
}
