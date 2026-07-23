package dev.panini.vyakaranam.analysis

enum class SemanticRelation {
    RECIPIENT,
    INSTRUMENT,
    SOURCE,
    LOCATION,
    DESIRED_OBJECT,
    INDEPENDENT_AGENT,
    PROMPTER_CAUSE,
    INDIFFERENT_OBJECT,
}

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
        DhatuKarakaProfile(setOf("भू", "प्रभू"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("शी", "स्था", "आस्", "अधिशी", "अधिस्था", "अध्यास्"), setOf(SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("जन", "जाय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("क्रुध", "द्रुह", "ईर्ष्या", "असूया", "अभिक्रुध", "अभिद्रुह"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("भी", "बिभ", "त्रा", "त्राय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("रुच", "रोच"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("वस", "उपवस", "अनुवस", "अधिवस", "आवस"), setOf(SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("पराजि", "पराजय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("अधी", "पठ"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("स्पृह", "स्पृहय"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("अभिनिविश"), setOf(SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("वारय", "वार"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("निली", "तिरोभू"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("धारय"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("श्लाघ", "ह्नु", "शप"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("दिव", "दीव्"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("परिक्री", "क्री"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("प्रतिश्रु", "आश्रु"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("अनुगृ", "प्रतिगृ"), setOf(SemanticRelation.RECIPIENT)),
    )

    fun forSurface(surface: String): DhatuKarakaProfile? {
        val normalized = surface.trimEnd('्', 'ँ')
        return profiles.firstOrNull { profile -> profile.surfaces.any(normalized::startsWith) }
    }
}
