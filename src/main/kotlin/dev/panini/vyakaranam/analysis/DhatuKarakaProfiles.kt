package dev.panini.vyakaranam.analysis

enum class SemanticRelation { RECIPIENT, INSTRUMENT, SOURCE }

data class DhatuKarakaProfile(
    val surfaces: Set<String>,
    val relations: Set<SemanticRelation>,
)

/** Semantic valency facts consumed by kāraka-saṃjñā rules. */
object DhatuKarakaProfiles {
    private val profiles = listOf(
        DhatuKarakaProfile(setOf("दा"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("लिख"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("पलाय"), setOf(SemanticRelation.SOURCE)),
    )

    fun forSurface(surface: String): DhatuKarakaProfile? {
        val normalized = surface.trimEnd('्', 'ँ')
        return profiles.firstOrNull { profile -> profile.surfaces.any(normalized::startsWith) }
    }
}
