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
)

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
    /** Applicability depends on membership in a named lexical gaṇa. */
    LEXICAL_GANA_MEMBERSHIP,
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
