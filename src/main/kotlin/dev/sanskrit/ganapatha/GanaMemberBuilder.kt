package dev.sanskrit.ganapatha

/** Small DSL used by gana files to preserve Ganapatha member order. */
class GanaMemberBuilder {
    private val members = mutableListOf<GanaMember>()

    fun member(
        text: String,
        hindiArtha: String = "",
        englishArtha: String = "",
    ) {
        require(text.isNotBlank()) { "A gana member text is required." }
        val normalized = GanaNormalizer.normalize(text)
        require(normalized.isNotBlank()) { "A normalized gana member text is required." }
        members += GanaMember(text, hindiArtha, englishArtha)
    }

    fun build(): List<GanaMember> = members.toList()
}

fun ganaMembers(entries: GanaMemberBuilder.() -> Unit): List<GanaMember> =
    GanaMemberBuilder().apply(entries).build()
