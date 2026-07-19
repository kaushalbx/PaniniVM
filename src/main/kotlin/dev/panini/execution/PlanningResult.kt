package dev.panini.execution

sealed interface PlanningResult {
    data class Planned(val program: BhashaProgram, val plans: List<ExecutionPlan>) : PlanningResult
    data class Failed(val result: ExecutionResult) : PlanningResult
}
