package dev.panini.vyakaranam.analysis

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha

enum class SemanticRelation {
    RECIPIENT,
    INSTRUMENT,
    SOURCE,
    LOCATION,
    DESIRED_OBJECT,
    INDEPENDENT_AGENT,
    PROMPTER_CAUSE,
    INDIFFERENT_OBJECT,
    ACCOMPANIMENT,
    BODY_DEFORMITY,
    CAUSE_HETU,
    GROUP_SELECTION,
    CHARACTERISTIC_MARK,
    EXCLUSION_VINA,
    OWNERSHIP_SWAMIN,
    DISREGARD_ANADARA,
    DIRECTIONAL_EXCLUSION,
    ACTION_MARKING,
    MOTION_GOAL,
    PRESENT_PARTICIPLE_AGENT,
    ENGROSSED_ATTACHMENT,
    MEMORY_OR_RULING_OBJECT,
    BETWEEN_OR_WITHOUT,
    KARMAPRAVACANIYA_GOVERNANCE,
    ENAPA_SUFFIX,
    EXCLUSION_LIMIT,
    REPRESENTATIVE_EXCHANGE,
    IMPLIED_PURPOSE_OBJECT,
    PURPOSE_ACTION,
    NON_FEMININE_QUALITY_CAUSE,
    EXPLICIT_HETU_USE,
    PRONOMINAL_HETU,
    SPATIAL_DIRECTION,
    DISTANCE_OR_PROXIMITY,
    COMPARATIVE_DISTINCTION,
    EQUAL_COMPARISON,
    BENEDICTION_WELLBEING,
    INTERVENING_DURATION_DISTANCE,
    GAMBLING_INSTRUMENT,
    ASTROLOGICAL_TIME,
    DISEASE_PAIN_OBJECT,
    BLESSING_HOPE_OBJECT,
    INJURY_VIOLENCE_OBJECT,
    LOCATION_PARTICIPLE_RELATION,
    INQUIRY_DESTINY_TARGET,
    INDETERMINATE_QUANTITY,
    TRANSFORMATION_ENDOWMENT,
    TRANSACTION_GAMBLING_OBJECT,
    HATRED_PARTICIPLE_OBJECT,
}

data class DhatuKarakaProfile(
    val surfaces: Set<String>,
    val relations: Set<SemanticRelation>,
)

/** Semantic valency facts consumed by kāraka-saṃjñā rules. */
object DhatuKarakaProfiles {
    private val profiles = listOf(
        DhatuKarakaProfile(setOf("दा"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("लिख्", "लिख"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("पलाय्", "पलाय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("भू", "प्रभू"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("शी", "स्था", "आस्", "अधिशी", "अधिस्था", "अध्यास्"), setOf(SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("जन्", "जन", "जाय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("क्रुध्", "क्रुध", "द्रुह", "ईर्ष्या", "असूया", "अभिक्रुध", "अभिद्रुह"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("भी", "बिभ", "त्रा", "त्राय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("रुच", "रोच"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("वस", "उपवस", "अनुवस", "अधिवस", "आवस"), setOf(SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("पराजि", "पराजय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("अधी", "पठ"), setOf(SemanticRelation.SOURCE, SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(setOf("स्पृह", "स्पृहय"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("अभिनिविश"), setOf(SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("वारय", "वार"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("निली", "तिरोभू"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("ধারय", "धारय"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("श्लाघ", "ह्नु", "शप"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("दिव", "दीव्"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("परिक्री", "क्री"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("प्रतिश्रु", "आश्रु"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("अनुगृ", "प्रतिगृ"), setOf(SemanticRelation.RECIPIENT)),
        // Additional Dhātus
        DhatuKarakaProfile(setOf("कृ", "करोति", "करो", "कुर्व", "कारय", "अकृ"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.PROMPTER_CAUSE, SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("गम्", "गच्छति", "गच्छ", "आगम्", "आगच्छ", "अनुगम्", "अनुगच्छ", "उपागम्", "उपागच्छ"), setOf(SemanticRelation.MOTION_GOAL, SemanticRelation.DESIRED_OBJECT, SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("पा", "पिबति", "पिब", "पाति"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("दृश्", "पश्यति", "पश्य", "द्रक्ष्य"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.INDIFFERENT_OBJECT)),
        DhatuKarakaProfile(setOf("लभ्", "लभते", "लभ", "लप्स्य"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("ज्ञा", "जानाति", "जाना", "ज्ञाय"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.MEMORY_OR_RULING_OBJECT)),
        DhatuKarakaProfile(setOf("श्रु", "शृणोति", "शृणु", "श्रोष्य"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("वच्", "वक्ति", "वक्", "ब्रू", "कथ", "कथय", "भाष्"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("हृ", "हरति", "हर", "हरय"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("गण", "गणयति", "गणय"), setOf(SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(setOf("युज्", "योजयति", "युञ्ज", "योजय"), setOf(SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(setOf("इष्", "इच्छति", "इच्छ"), setOf(SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(setOf("नी", "नयति", "नय"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.MOTION_GOAL)),
    )

    fun forDhatu(dhatu: Dhatu): DhatuKarakaProfile? {
        return forSurface(dhatu.sourceSurface) ?: forSurface(dhatu.upadesha)
    }

    fun forSurface(surface: String): DhatuKarakaProfile? {
        val registeredProfile = profiles.firstOrNull { profile ->
            profile.surfaces.contains(surface) || profile.surfaces.any { entry -> surface.startsWith(entry) }
        }
        val dhatuEntries = DhatuPatha.all.filter { dhatu ->
            surface == dhatu.upadesha || surface == dhatu.sourceSurface || surface == dhatu.derivationalSurface
        }
        val dhatuRelations = dhatuEntries.flatMap { it.semanticRelations }.toSet()
        val combinedRelations = (registeredProfile?.relations.orEmpty() + dhatuRelations)
        if (combinedRelations.isEmpty() && registeredProfile == null) return null
        return DhatuKarakaProfile(
            surfaces = registeredProfile?.surfaces ?: setOf(surface),
            relations = combinedRelations,
        )
    }
}
