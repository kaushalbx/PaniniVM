package dev.panini.execution.binding

/** Legacy normalization for fused action surfaces used by फल history matching. */
internal object ActionStemNormalizer {
    private val COMPATIBILITY_PREFIXES = listOf("नि", "प्र")
    private val TERMINAL_MARKERS = charArrayOf('न', 'म', '्', 'अ')

    fun normalize(stem: String): String = COMPATIBILITY_PREFIXES
        .fold(stem) { current, prefix -> current.removePrefix(prefix) }
        .trimEnd(*TERMINAL_MARKERS)
}
