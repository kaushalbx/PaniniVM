package dev.panini.execution

import dev.panini.core.Karaka

/** Declarative requirement for one kāraka in an operation signature. */
data class KarakaRequirement(
    val karaka: Karaka,
    val minimumMembers: Int = 1,
    val maximumMembers: Int? = null,
    val shape: ExpressionShape? = null,
    val memberSamjnas: Set<ExecutionSamjna> = emptySet(),
) {
    init {
        require(minimumMembers >= 1) { "Minimum member count must be positive." }
        require(maximumMembers == null || maximumMembers >= minimumMembers) {
            "Maximum member count cannot be smaller than the minimum."
        }
    }

    internal val specificity: Int
        get() = 1 + (if (shape == null) 0 else 1) + memberSamjnas.size +
            (if (minimumMembers == 1 && maximumMembers == null) 0 else 1)
}
