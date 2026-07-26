package dev.panini.derivation

import dev.panini.core.Karaka
import dev.panini.core.Linga
import dev.panini.core.NominalCategory
import dev.panini.core.Prayoga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.analysis.SemanticRelation
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.analysis.ParticipantFacts

/** Typed input for a nominal (sup) derivation. */
data class SubantaDerivationRequest(
    val pratipadika: String,
    val vibhakti: Vibhakti,
    val vacana: Vacana,
    val stemClass: SubantaStemClass = SubantaStemClass.A_STEM_MASCULINE,
) {
    init {
        require(pratipadika.isNotBlank()) { "A prātipadika is required." }
        require(stemClass.accepts(pratipadika)) {
            "$pratipadika is not supported as ${stemClass.displayName}; select a supported stem class."
        }
    }

    fun initialState(): DerivationState = DerivationState(
        terms = listOf(DerivationTerm("pratipadika", pratipadika, TermKind.PRATIPADIKA)),
        context = DerivationalContext(
            rupa = Rupa(linga = stemClass.linga, vibhakti = vibhakti, vacana = vacana),
        ),
    )
}

/** The nominal morphology currently implemented by the executable patha. */
enum class SubantaStemClass(
    val displayName: String,
    val linga: Linga,
) {
    A_STEM_MASCULINE("a-stem masculine", Linga.PUMS),
    A_STEM_NEUTER("a-stem neuter", Linga.NAPUMSAKA),
    I_STEM_MASCULINE("i-stem masculine", Linga.PUMS),
    I_STEM_FEMININE("i-stem feminine", Linga.STRI),
    I_STEM_NEUTER("i-stem neuter", Linga.NAPUMSAKA),
    U_STEM_MASCULINE("u-stem masculine", Linga.PUMS),
    U_STEM_FEMININE("u-stem feminine", Linga.STRI),
    U_STEM_NEUTER("u-stem neuter", Linga.NAPUMSAKA),
    R_STEM_MASCULINE("ṛ-stem masculine", Linga.PUMS),
    N_STEM_MASCULINE("n-stem masculine", Linga.PUMS),
    S_STEM_NEUTER("s-stem neuter", Linga.NAPUMSAKA),
    II_STEM_FEMININE("ī-stem feminine", Linga.STRI),
    A_STEM_FEMININE("ā-stem feminine", Linga.STRI),
    UU_STEM_FEMININE("ū-stem feminine", Linga.STRI),
    R_STEM_FEMININE("ṛ-stem feminine", Linga.STRI),
    IN_STEM_MASCULINE("in-stem masculine", Linga.PUMS),
    MATUP_STEM("matup-stem masculine", Linga.PUMS),
    T_STEM("t-stem", Linga.PUMS),
    D_STEM("d-stem", Linga.STRI),
    C_STEM("c-stem", Linga.STRI),
    ANCH_STEM("añc-stem", Linga.PUMS),
    PRONOMINAL_STEM("pronominal stem", Linga.PUMS),
    PRONOMINAL_PERSONAL("personal pronoun stem", Linga.PUMS),
    J_STEM("j-stem", Linga.PUMS),
    SH_STEM("ṣ-stem", Linga.STRI),
    DIPHTHONG_STEM("diphthong-stem", Linga.PUMS),
    IRREGULAR_N_STEM("irregular n-stem", Linga.PUMS),
    PATHIN_STEM("pathin-stem", Linga.PUMS),
    NUMERAL_CARDINAL("cardinal numeral", Linga.PUMS),
    NUMERAL_ORDINAL("ordinal numeral", Linga.PUMS),
    S_STEM_MASCULINE("s-stem masculine", Linga.PUMS),
    R_CONSONANT_STEM("r-consonant stem", Linga.STRI),
    H_STEM("h-stem", Linga.PUMS),
    ;

    fun accepts(pratipadika: String): Boolean = when (this) {
        A_STEM_MASCULINE, A_STEM_NEUTER, PRONOMINAL_STEM, NUMERAL_ORDINAL -> pratipadika.last() !in independentVowelsOrMarks
        I_STEM_MASCULINE, I_STEM_FEMININE, I_STEM_NEUTER -> pratipadika.endsWith(Svara.I.matra!!) || pratipadika.endsWith(Svara.I.devanagari)
        U_STEM_MASCULINE, U_STEM_FEMININE, U_STEM_NEUTER -> pratipadika.endsWith(Svara.U.matra!!) || pratipadika.endsWith(Svara.U.devanagari)
        R_STEM_MASCULINE, R_STEM_FEMININE -> pratipadika.endsWith(Svara.R.matra!!) || pratipadika.endsWith(Svara.R.devanagari)
        N_STEM_MASCULINE, IN_STEM_MASCULINE, IRREGULAR_N_STEM, PATHIN_STEM -> pratipadika.endsWith("न्")
        S_STEM_NEUTER, S_STEM_MASCULINE -> pratipadika.endsWith("स्")
        II_STEM_FEMININE -> pratipadika.endsWith(Svara.II.matra!!) || pratipadika.endsWith(Svara.II.devanagari)
        A_STEM_FEMININE -> pratipadika.endsWith(Svara.AA.matra!!) || pratipadika.endsWith(Svara.AA.devanagari)
        UU_STEM_FEMININE -> pratipadika.endsWith(Svara.UU.matra!!) || pratipadika.endsWith(Svara.UU.devanagari)
        MATUP_STEM, T_STEM -> pratipadika.endsWith("त्")
        D_STEM, PRONOMINAL_PERSONAL -> pratipadika.endsWith("द्") || pratipadika.endsWith("ध्")
        C_STEM, ANCH_STEM -> pratipadika.endsWith("च्") || pratipadika.endsWith("ञ्च्")
        J_STEM -> pratipadika.endsWith("ज्")
        SH_STEM -> pratipadika.endsWith("ष्")
        DIPHTHONG_STEM -> pratipadika.endsWith("ो") || pratipadika.endsWith("ौ") || pratipadika.endsWith("ै") || pratipadika in setOf("गो", "द्यौ", "नौ")
        NUMERAL_CARDINAL, R_CONSONANT_STEM -> pratipadika.endsWith("र्") || pratipadika in setOf("द्वि", "त्रि", "चतुर्", "पञ्चन्", "षष्", "सप्तन्", "अष्टन्", "नवन्", "दशन्")
        H_STEM -> pratipadika.endsWith("ह्")
    }

    companion object {
        fun guess(pratipadika: String): SubantaStemClass {
            return when {
                pratipadika in setOf("चन्द्रमस्", "अङ्गिरस्", "वेधस्", "उशनस्", "पुरोधस्") -> S_STEM_MASCULINE
                pratipadika in setOf("द्वि", "त्रि", "चतुर्", "पञ्चन्", "षष्", "सप्तन्", "अष्टन्", "नवन्", "दशन्") -> NUMERAL_CARDINAL
                pratipadika in setOf("गो", "द्यौ", "नौ", "ग्लो") -> DIPHTHONG_STEM
                pratipadika in setOf("पथिन्", "मथिन्", "ऋभुक्षिन्") -> PATHIN_STEM
                pratipadika in setOf("अहन्", "श्वन्", "मघवन्", "युवन्") -> IRREGULAR_N_STEM
                pratipadika.endsWith("र्") -> R_CONSONANT_STEM
                pratipadika.endsWith("ह्") -> H_STEM
                pratipadika in setOf("युष्मद्", "अस्मद्", "तद्", "यद्", "एतद्", "किम्") -> PRONOMINAL_PERSONAL
                pratipadika in setOf("सर्व", "विश्व", "उभय", "अन्य", "अन्यतर", "इतर") -> PRONOMINAL_STEM
                pratipadika.endsWith("अञ्च्") || pratipadika.endsWith("ञ्च्") -> ANCH_STEM
                pratipadika.endsWith("िन्") -> IN_STEM_MASCULINE
                pratipadika.endsWith("न्") -> N_STEM_MASCULINE
                pratipadika.endsWith("स्") -> S_STEM_NEUTER
                pratipadika.endsWith("मत्") || pratipadika.endsWith("वत्") -> MATUP_STEM
                pratipadika.endsWith("त्") -> T_STEM
                pratipadika.endsWith("द्") || pratipadika.endsWith("ध्") -> D_STEM
                pratipadika.endsWith("च्") -> C_STEM
                pratipadika.endsWith("ज्") -> J_STEM
                pratipadika.endsWith("ष्") -> SH_STEM
                pratipadika.endsWith("ी") -> II_STEM_FEMININE
                pratipadika.endsWith("ू") -> UU_STEM_FEMININE
                pratipadika.endsWith("ा") -> A_STEM_FEMININE
                pratipadika.endsWith("ि") -> {
                    when (pratipadika) {
                        in setOf("वारि", "अक्षि", "दधि", "सक्थि", "अस्थि") -> I_STEM_NEUTER
                        in setOf("मति", "भूमि", "धूलि", "गति", "रात्रि", "कीर्ति", "जाति", "नीति", "शान्ति", "मुक्ति", "स्मृति", "प्रीति") -> I_STEM_FEMININE
                        else -> I_STEM_MASCULINE
                    }
                }
                pratipadika.endsWith("ु") -> {
                    when (pratipadika) {
                        in setOf("मधु", "अम्बु", "जानु", "वस्तु", "अश्रु", "दारु", "तालु") -> U_STEM_NEUTER
                        in setOf("धेनु", "तनु", "रेणु", "चञ्चु", "रज्जु") -> U_STEM_FEMININE
                        else -> U_STEM_MASCULINE
                    }
                }
                pratipadika.endsWith("ृ") -> {
                    if (pratipadika in setOf("मातृ", "स्वसृ", "दुहितृ", "यातृ", "ननान्दृ")) R_STEM_FEMININE
                    else R_STEM_MASCULINE
                }
                else -> {
                    if (pratipadika in setOf("फल", "वन", "गृह", "पुस्तक", "जल", "पुष्प", "मित्र", "पत्र", "कमल", "नेत्र", "ज्ञान", "सत्य", "धन")) A_STEM_NEUTER
                    else A_STEM_MASCULINE
                }
            }
        }

        val independentVowelsOrMarks: Set<Char> = buildSet {
            Svara.entries.forEach { svara ->
                add(svara.devanagari.single())
                svara.matra?.single()?.let(::add)
            }
            Ayogavaha.entries.forEach { add(it.devanagari.single()) }
            add(CANDRABINDU)
            add(Vyanjana.VIRAMA)
        }

        /** Candrabindu is a combining sign, not an independent varṇa enum entry. */
        const val CANDRABINDU: Char = 'ँ'
    }
}

/** Input request for deriving a nominal form from a semantic Kāraka relation. */
data class KarakaSubantaDerivationRequest(
    val pratipadika: String,
    val karaka: Karaka,
    val vacana: Vacana,
    val dhatu: String,
    val isSakarmaka: Boolean = true,
    val prayoga: Prayoga = Prayoga.KARTARI,
    val semanticRelations: Set<SemanticRelation>? = null,
    val categories: Set<NominalCategory>? = null,
    val upapada: String? = null,
    val otherParticipants: List<ParticipantFacts>? = null,
)
