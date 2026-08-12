package dev.panini.sankhya

data class CanonicalNumeralStemSuggestion(
    val offset: Int,
    val surface: String,
    val canonical: String,
)

/** Finds inflected numeral surfaces used as segmented prātipadikas before `+`. */
object CanonicalNumeralStem {
    private val replacements = linkedMapOf(
        "पञ्च" to "पञ्चन्",
        "षट्" to "षष्",
        "सप्त" to "सप्तन्",
        "अष्ट" to "अष्टन्",
        "नव" to "नवन्",
        "दश" to "दशन्",
    )
    private val candidate = Regex("(?<![\\p{L}\\p{M}])(${replacements.keys.joinToString("|")})(?=\\s*\\+)")

    fun canonicalFor(surface: String): String? = replacements[surface]

    fun suggestions(source: String): List<CanonicalNumeralStemSuggestion> = buildList {
        var lineStart = 0
        source.lineSequence().forEach { line ->
            val code = line.substringBefore('#')
            candidate.findAll(code).forEach { match ->
                val surface = match.value
                add(
                    CanonicalNumeralStemSuggestion(
                        offset = lineStart + match.range.first,
                        surface = surface,
                        canonical = replacements.getValue(surface),
                    ),
                )
            }
            lineStart += line.length + 1
        }
    }
}
