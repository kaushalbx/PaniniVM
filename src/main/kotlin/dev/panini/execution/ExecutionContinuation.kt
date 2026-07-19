package dev.panini.execution

/** Immutable state required to resume a paused program without repeating work. */
data class ExecutionContinuation(
    val planning: PlanningResult.Planned,
    val nextPlanIndex: Int,
    val values: Map<String, String>,
    val valueSamjnas: Map<String, Set<ExecutionSamjna>>,
    val trace: List<String>,
)
