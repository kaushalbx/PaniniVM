package dev.panini.analysis

import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.shiksha.Samjna

/**
 * A single member (pada) of a compound, carrying its base stem, upadesha, vibhakti, and saṃjñās.
 *
 * @param upadesha  Canonical base form of the stem (e.g. "राज", "पुरुष", "उप").
 * @param vibhakti  The grammatical case this pada carries in the laukika vigraha.
 * @param samjnas   Saṃjñās assigned to this pada (e.g. PRATIPADIKA, AVYAYA, UPASARGA).
 */
data class SamasaPada(
    val upadesha: String,
    val vibhakti: Vibhakti = Vibhakti.PRATHAMA,
    val samjnas: Set<Samjna> = emptySet(),
    val linga: Linga? = null,
    val vacana: Vacana? = null,
    /** Explicit masculine counterpart used by puṃvadbhāva rules; never guessed lexically. */
    val masculineCounterpart: String? = null,
    val morphologicalFeatures: Set<SamasaMorphologicalFeature> = emptySet(),
)

enum class SamasaMorphologicalFeature {
    FEMININE_UUNG,
    ORDINAL,
    PRIYADI,
    K_UPADHA,
    VRDDHI_CAUSING_TADDHITA,
    COLOR_OR_MODIFICATION_TADDHITA,
    BODY_PART_I_ENDING,
    JATI,
    FEMININE_NGI,
    FEMININE_AAP,
    NADI,
    UGIT_DERIVED,
    MONOSYLLABIC,
    KHIT_DERIVED,
    KRIT_DERIVED,
    TRC_AKA_DERIVED,
    PRONOUN,
    INDECLINABLE,
    KVI_DERIVED,
    GHAN_DERIVED,
    KAP_DERIVED,
    KOTARADI,
    KIMSULAKADI,
    SAMPRASARANA_FINAL,
    TADDHITA_LUK,
    IYAS_ENDING,
}

/** Semantic facts supplied by the vigraha, rather than guessed from its words. */
enum class SamasaSemanticRelation {
    SAMARTHYA,
    CASE_RELATION,
    QUALIFIER_QUALIFIED,
    EXTERNAL_REFERENT,
    COORDINATION,
    COLLECTIVE,
    INDECLINABLE_RELATION,
    UPAPADA_RELATION,
    NUMERAL_GROUP,
    /** The derivation belongs to the Vedic/chandas register. */
    VEDIC_REGISTER,
    /** A numeral denotes the external referent, not merely a lexical numeral. */
    NUMERICAL_REFERENT,
    /** The vigraha expresses praise or honour. */
    PRAISE,
    /** The vigraha uses a member in censure/reproach (kṣepa). */
    CENSURE,
    /** The vigraha expresses a measure or spatial dimension. */
    MEASURE_DIMENSION,
    /** The members are related through teaching or study (vidyā-sambandha). */
    STUDY_RELATION,
    /** The members are related by descent or blood (yoni-sambandha). */
    BLOOD_RELATION,
    /** The coordinated members are names of deities. */
    DEVATA_COORDINATION,
    /** The compound is used as a proper name (saṃjñā). */
    PROPER_NAME,
    /** Saha denotes extension through the end of a text. */
    TEXT_COMPLETION,
    /** Saha denotes excess or addition. */
    EXCESS,
    /** Saha accompanies an indirectly apprehended second object. */
    INDIRECT_SECOND,
    /** The uttarapada denotes time. */
    TIME_REFERENCE,
    /** The uttarapada denotes something that is to be filled with water. */
    WATER_FILLED,
    /** The compound is a benedictive expression. */
    BENEDICTION,
    /** The relevant referent is explicitly non-animate. */
    NON_ANIMATE_REFERENT,
    /** The members share a Vedic course or observance. */
    COMMON_VEDIC_OBSERVANCE,
    /** The expression denotes a locality or spatial destination. */
    LOCALITY,
    /** The uttarapada denotes a species (jāti). */
    SPECIES,
    /** Ku has the attenuative sense of īṣat, “slightly”. */
    SLIGHT_DEGREE,
    /** Selects the explicitly attributed opinion of Galava in 6.3.61. */
    GALAVA_OPINION,
    /** The compound denotes an ownership/identification mark. */
    OWNERSHIP_MARK,
    /** The resulting proper name is specifically that of a ṛṣi. */
    RISHI_NAME,
    NOT_LITERAL_EYE,
    COUNTRY_PERSON,
    BODY_PART,
    WOODEN_OBJECT,
    RECIPROCAL_ACTION,
    HUNTER_ASSOCIATION,
    SMALL_QUANTITY,
    AGE_STAGE,
    ANIMAL_CONDITION,
    PRAISED_REFERENT,
    /** Explicitly requests the residual optional kap branch of 5.4.154. */
    RESIDUAL_KAP_OPTION,
    /** Applicability depends on membership in a named lexical gaṇa. */
    LEXICAL_GANA_MEMBERSHIP,
    /** Genitive relation specifies one member selected from a group. */
    NIRDHARANA,
    /** A kta expression carries the sense of worship or reverence. */
    WORSHIP,
    /** A kta expression denotes its locus rather than an ordinary possessor. */
    LOCATIVE_RELATION,
    /** The genitive member is construed as the object of a derivative. */
    OBJECT_RELATION,
    /** The genitive member is construed as the agent of a derivative. */
    AGENT_RELATION,
}

/**
 * The input context provided to every Samāsa Sūtra (Adhyāyas 2.1–2.2).
 * Carries all information needed for principled Pāṇinian matching
 * without surface-string heuristics.
 *
 * @param padas       Ordered list of compound members (pūrvapada first).
 * @param samasaType  The macro compound type declared by the caller.
 */
data class SamasaRuleContext(
    val padas: List<SamasaPada>,
    val samasaType: SamasaType,
    val outputLinga: Linga? = null,
    val outputVacana: Vacana? = null,
    val semanticRelations: Set<SamasaSemanticRelation> = emptySet(),
    val strictSemantics: Boolean = false,
) {
    val purvaPada: SamasaPada get() = padas.first()
    val uttaraPada: SamasaPada get() = padas.last()
    val purvaPadaVibhakti: Vibhakti get() = purvaPada.vibhakti
}
