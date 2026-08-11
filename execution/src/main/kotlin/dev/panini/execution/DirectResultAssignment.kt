package dev.panini.execution

data class DirectResultAssignmentSuggestion(
    val offset: Int,
    val length: Int,
    val replacement: String,
)

/** Recognizes verbose `क्रियाफलम् ... देहि` clauses that can consume the preceding ततः result. */
object DirectResultAssignment {
    private val verboseAssignment = Regex(
        "दा\\s*\\+\\s*लोट्\\s*\\+\\s*सिप्\\s+[^।॥\\n]*?\\+\\s*ङस्\\s+फल\\s*\\+\\s*अम्\\s+([\\p{L}\\p{M}]+)\\s*\\+\\s*ङे",
    )

    fun suggestions(source: String): List<DirectResultAssignmentSuggestion> =
        verboseAssignment.findAll(source).map { match ->
            val target = match.groupValues[1]
            DirectResultAssignmentSuggestion(
                offset = match.range.first,
                length = match.value.length,
                replacement = "$target + ङे दा + लोट् + सिप्",
            )
        }.toList()
}
