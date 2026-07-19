package dev.panini.execution

data class BhashaProgram(
    val nirdesha: Nirdesha,
    val invocations: List<DhatuInvocation>,
    val dependencies: Set<ActionDependency> = emptySet(),
)
