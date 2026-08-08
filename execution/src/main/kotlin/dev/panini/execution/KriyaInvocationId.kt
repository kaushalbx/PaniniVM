package dev.panini.execution

/** Canonical identity for a verbal invocation within one utterance. */
internal object KriyaInvocationId {
    private const val PREFIX = "योग-"

    fun of(oneBasedIndex: Int): String {
        require(oneBasedIndex > 0) { "A kriyā invocation index must be positive." }
        return "$PREFIX$oneBasedIndex"
    }

    fun indexOf(id: String): Int? = id
        .takeIf { it.startsWith(PREFIX) }
        ?.removePrefix(PREFIX)
        ?.toIntOrNull()
        ?.takeIf { it > 0 }
}
