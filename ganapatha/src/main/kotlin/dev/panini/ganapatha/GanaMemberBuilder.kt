package dev.panini.ganapatha

import dev.panini.shiksha.Accent
import dev.panini.shiksha.Recension

/** Small DSL used by gana files to preserve Ganapatha member order. */
class GanaMemberBuilder {
    private val members = mutableListOf<GanaMember>()

    fun member(
        text: String,
        hindiArtha: String = "",
        englishArtha: String = "",
        upadesha: String? = null,
        condition: String? = null,
        ganaCondition: GanaCondition? = null,
        accent: Accent? = null,
        isVartika: Boolean = false,
        isInstruction: Boolean = false,
        recensions: Set<Recension> = emptySet(),
        examples: List<String> = emptyList(),
    ) {
        require(text.isNotBlank()) { "A gana member text is required." }
        val normalized = GanaNormalizer.normalize(text)
        require(normalized.isNotBlank()) { "A normalized gana member text is required." }
        members += GanaMember(
            text = text,
            upadesha = upadesha,
            condition = condition ?: ganaCondition?.sourceText,
            ganaCondition = ganaCondition,
            accent = accent,
            isVartika = isVartika,
            isInstruction = isInstruction,
            recensions = recensions,
            examples = examples,
            hindiArtha = hindiArtha,
            englishArtha = englishArtha
        )
    }

    fun build(): List<GanaMember> = members.toList()
}

fun ganaMembers(entries: GanaMemberBuilder.() -> Unit): List<GanaMember> =
    GanaMemberBuilder().apply(entries).build()
