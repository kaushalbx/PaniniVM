package dev.sanskrit.ganapatha

/** Interprets the compact pattern-instructions embedded in some Gaṇapāṭha lists. */
object GanaInstructionMatcher {
    fun matches(instruction: String, text: String): Boolean =
        matches(instruction, GanaInstructionContext(text))

    /**
     * Some instruction entries constrain the pratyaya rather than the visible word.
     * Callers that have a derivation term should supply its upadeśa in [suffixUpadeshas].
     */
    fun matches(instruction: String, context: GanaInstructionContext): Boolean {
        val value = GanaNormalizer.normalize(context.text)
        val suffixes = context.suffixUpadeshas.map { GanaNormalizer.normalize(it) }.toSet()
        return when (GanaNormalizer.normalize(instruction)) {
            "वत्" -> value.endsWith("वत्")
            "क्तवातोसुन्कसुनः" -> value.matches(Regex(".*(त्वा|तोसुन्|कसुन्)$"))
            "तसिलादयस्तद्धित एधाच्चपर्यन्ताः" -> "तसिल्" in suffixes
            "कृन्मकारसन्ध्यक्षरान्तोऽव्ययीभावश्च" ->
                value.endsWith("कृत्") || (value.endsWith("म्") && value !in setOf("इदम्", "किम्", "अयम्", "इयम्")) || value.endsWith("सन्धि") || value.endsWith("अक्षर")
            "शस्तसी" -> value.endsWith("शस्") || value.endsWith("तस्") || suffixes.any { it == "शस्" || it == "तस्" }
            "कृत्वसुच्" -> value.endsWith("कृत्वस्") || "कृत्वसुच्" in suffixes
            "सुच्" -> value.endsWith("सुच्") || "सुच्" in suffixes
            else -> false
        }
    }
}

data class GanaInstructionContext(
    val text: String,
    val suffixUpadeshas: Set<String> = emptySet(),
)
