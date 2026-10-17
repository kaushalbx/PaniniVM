package dev.panini.execution


data class ExecutionProgram(
    val ukti: ExecutableUkti,
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

/** Backend-independent resolved semantics shared by execution and code generation. */
data class ResolvedSemanticProgram(
    val source: ExecutionProgram,
    val plans: List<ExecutionPlan>,
) {
    init {
        require(plans.map { it.invocationId }.distinct().size == plans.size) {
            "A resolved semantic program cannot contain duplicate invocation identities."
        }
    }
}

sealed interface PlanningResult {
    data class Planned(val program: ExecutionProgram, val plans: List<ExecutionPlan>) : PlanningResult {
        val resolvedProgram: ResolvedSemanticProgram = ResolvedSemanticProgram(program, plans)
    }
    data class Failed(val result: ExecutionResult) : PlanningResult
}
