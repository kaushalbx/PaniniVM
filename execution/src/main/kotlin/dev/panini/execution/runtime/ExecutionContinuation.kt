package dev.panini.execution

/** Immutable state required to resume a paused program without repeating work. */
data class ExecutionContinuation(
    val planning: PlanningResult.Planned,
    val nextPlanIndex: Int,
    val environment: ValueEnvironment,
    val trace: List<String>,
    val lastOutputKind: OutputKind = OutputKind.INTERNAL,
)
