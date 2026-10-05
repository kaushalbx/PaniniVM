package dev.panini.vyakaranam.ast

import dev.panini.core.Lakara
import dev.panini.core.SupLopa
import dev.panini.execution.SanskritValue

sealed interface VyakaranamNode {
    val sourceText: String
}

/** Native source-order document; declaration processing must not reorder its items. */
data class ProgramDocument(
    override val sourceText: String,
    val items: List<VyakaranamNode>,
    val itemSpans: List<DocumentSourceSpan> = emptyList(),
    val prakriyaHeaderSpans: Map<Int, DocumentSourceSpan> = emptyMap(),
    val prakriyaBodySpans: Map<Int, List<DocumentSourceSpan>> = emptyMap(),
    val prakriyaNameSpans: Map<Int, DocumentSourceSpan> = emptyMap(),
    val scopeDomainSpans: Map<Int, DocumentSourceSpan> = emptyMap(),
) : VyakaranamNode

/** Half-open character offsets into the original document, including terminators. */
data class DocumentSourceSpan(val start: Int, val endExclusive: Int) {
    init { require(start >= 0 && endExclusive >= start) }
}

/** A named सीमा declaration retains its morphological bounds, not evaluated numbers. */
data class RangeDeclaration(
    override val sourceText: String,
    val boundary: ParyantaRangePada,
    val marker: SubantaPada,
) : VyakaranamNode

data class Ukti(
    override val sourceText: String,
    val sambodhana: Sambodhana? = null,
    val body: ProgramNode,
) : VyakaranamNode {
    /** Explicit grammatical query; execution order must be read from [body]. */
    fun grammaticalVakyas(): List<Vakya> = body.invocations().map(Invocation::vakya)
}

/**
 * Uniform, recursive representation of executable structure.
 *
 * Grammatical nodes such as [Vakya] remain leaves. Control-flow consumers must
 * inspect this tree instead of deriving structure from clause positions or text.
 */
sealed interface ProgramNode : VyakaranamNode

data class Invocation(
    val vakya: Vakya,
    /** Source-level nominal result whose understood return verb was lowered into [vakya]. */
    val implicitValue: String? = null,
    /** Parsed source nominal retained so renderers need not reconstruct it from text. */
    val implicitValuePada: Pada? = null,
) : ProgramNode {
    override val sourceText: String = vakya.sourceText
}

data class Sequence(
    override val sourceText: String,
    val statements: List<ProgramNode>,
    val connectors: List<String> = emptyList(),
) : ProgramNode {
    val connectorKinds: List<SequenceConnector> = connectors.map(SequenceConnector::fromSurface)

    init {
        require(statements.isNotEmpty()) { "A sequence must contain at least one statement." }
        require(connectors.size <= statements.size - 1) {
            "A sequence cannot have more connectors than statement boundaries."
        }
    }
}

/** Typed discourse relation between adjacent executable clauses. */
enum class SequenceConnector(val surface: String) {
    SAMUCCAYA("च"),
    ANANTARYA("ततः"),
    ;

    companion object {
        fun fromSurface(surface: String): SequenceConnector = entries.singleOrNull {
            it.surface == surface.trim()
        } ?: error("Unsupported executable sequence connector: '$surface'.")
    }
}

data class Conditional(
    override val sourceText: String,
    val condition: ProgramNode,
    val consequent: ProgramNode,
    val alternate: ProgramNode? = null,
    /** One source-written pipeline target lowered into both branches for execution. */
    val surfacePipelineTarget: ProgramNode? = null,
) : ProgramNode

/** A command mentioned with इति and supplied as data to a reporting command. */
data class Quotation(
    override val sourceText: String,
    val quoted: Invocation,
    val reporting: ProgramNode,
) : ProgramNode

data class Repeat(
    override val sourceText: String,
    val count: Int,
    val body: ProgramNode,
) : ProgramNode {
    init {
        require(count > 0) { "A repetition count must be positive." }
    }
}

/** A condition-controlled loop, optionally bounded by a Sanskrit repetition count. */
data class WhileLoop(
    override val sourceText: String,
    val condition: Invocation,
    val body: ProgramNode,
    val maximumIterationStems: List<String> = emptyList(),
    val maximumBoundaryPadas: List<Pada> = emptyList(),
    val exhausted: ProgramNode? = null,
    val resultTarget: ProgramNode? = null,
) : ProgramNode

data class PipelineStage(
    override val sourceText: String,
    val domainStem: String?,
    val operationStem: String,
) : VyakaranamNode

data class Pipeline(
    override val sourceText: String,
    /** Compatibility projection; new consumers should use [argumentPadas]. */
    val arguments: List<String>,
    val stages: List<PipelineStage>,
    /** Parsed arguments retain nominal identity independently of their case endings. */
    val argumentPadas: List<Pada> = emptyList(),
    val renderPadas: List<Pada> = emptyList(),
) : ProgramNode

enum class PrakriyaVisibility { PUBLIC, INTERNAL }

enum class PrakriyaPrecedence { DEFAULT, NITYA, ANTARANGA, APAVADA }

data class PrakriyaModifiers(
    val visibility: PrakriyaVisibility = PrakriyaVisibility.PUBLIC,
    val precedence: PrakriyaPrecedence = PrakriyaPrecedence.DEFAULT,
)

data class Prakriya(
    override val sourceText: String,
    val name: String,
    val domain: String? = null,
    val body: List<ProgramNode>,
    /** Case-independent operation identity built from the parsed header. */
    val nameIdentity: String = name,
    /** Case-independent governing domain identity, when explicitly declared. */
    val domainIdentity: String? = domain,
    val modifiers: PrakriyaModifiers = PrakriyaModifiers(),
) : ProgramNode

data class Scope(
    override val sourceText: String,
    /** Segmented declared nominal, retained for morphology such as taddhita inheritance. */
    val domain: String,
    /** Case-independent referent identity rebuilt from the parsed prātipadika. */
    val domainIdentity: String = domain,
    val body: List<ProgramNode> = emptyList(),
) : ProgramNode

fun ProgramNode.invocations(): List<Invocation> =
    depthFirst().filterIsInstance<Invocation>().toList()

/** Invocations in execution order, including copies introduced by [Repeat]. */
fun ProgramNode.expandedInvocations(): List<Invocation> =
    depthFirst(expandRepeats = true).filterIsInstance<Invocation>().toList()

data class Sambodhana(
    override val sourceText: String,
    val suchaka: String?,
    val subanta: SubantaPada,
) : VyakaranamNode

sealed interface Vakya : VyakaranamNode {
    val padas: List<Pada>
}

data class AkhyataVakya(
    override val sourceText: String,
    override val padas: List<Pada>,
    val tinganta: TingantaPada,
) : Vakya

data class NamaVakya(
    override val sourceText: String,
    override val padas: List<Pada>,
) : Vakya

sealed interface Pada : VyakaranamNode

data class SubantaPada(
    override val sourceText: String,
    val pratipadika: Pratipadika,
    val sup: SupPratyaya,
) : Pada

data class TingantaPada(
    override val sourceText: String,
    val upasargas: List<String>,
    val dhatu: DhatuPrakriti,
    val lakara: Lakara,
    val ting: TingPratyaya,
    /** Explicit gaṇa-vikaraṇa when the upadeśa alone is lexically ambiguous. */
    val vikarana: Vikarana? = null,
) : Pada

data class AvyayaPada(
    override val sourceText: String,
    val form: String,
    val derivation: AvyayaDerivation? = null,
) : Pada {
    val function: AvyayaFunction? = AvyayaFunction.fromForm(form)
}

enum class AvyayaFunction {
    NISHEDHA,
    QUOTATIVE,
    REPETITION,
    ;

    companion object {
        fun fromForm(form: String): AvyayaFunction? = when (form.trim()) {
            "न", "मा" -> NISHEDHA
            "इति" -> QUOTATIVE
            "पुनः", "पुनर्" -> REPETITION
            else -> null
        }
    }
}

data class SamuccitaSubanta(
    override val sourceText: String,
    val members: List<SubantaPada>,
) : Pada

/** The inclusive सीमा licensed by an ablative source and segmented पर्यन्तम्. */
data class ParyantaRangePada(
    override val sourceText: String,
    val lowerLimit: SankhyaBoundaryPada,
    val upperLimit: SankhyaBoundaryPada,
    val marker: SubantaPada,
) : Pada

sealed interface Pratipadika : VyakaranamNode

/** Case-independent semantic identity retained from parsed nominal morphology. */
fun Pratipadika.semanticKey(): String = when (this) {
    is SankhyaPratipadika -> sourceText
    is MulaPratipadika -> text
    is KridantaPratipadika -> buildList {
        addAll(upasargas)
        add(dhatu.mulaDhatu)
        addAll(dhatu.sanadiPratyayas)
        add(krtPratyaya)
    }.joinToString("+")
    is UnadyantaPratipadika -> sourceText
    is SamasaPratipadika -> angas.joinToString("-") { it.pratipadika.semanticKey() }
}

/** Canonical segmented morphology rebuilt from parsed fields, never [sourceText]. */
fun Pratipadika.morphologicalKey(includeTaddhita: Boolean = true): String {
    fun List<PratipadikaVikara>.suffixes(): List<String> = mapNotNull { vikara ->
        when (vikara) {
            is TaddhitaVikara -> vikara.pratyaya.takeIf { includeTaddhita }
            is StriVikara -> vikara.pratyaya
        }
    }
    fun List<String>.segmented(): String = joinToString(" + ")

    return when (this) {
        is MulaPratipadika -> (listOf(text) + vikaras.suffixes()).segmented()
        is KridantaPratipadika -> (
            upasargas + dhatu.mulaDhatu + dhatu.sanadiPratyayas +
                krtPratyaya + vikaras.suffixes()
            ).segmented()
        is UnadyantaPratipadika -> (
            upasargas + dhatu.mulaDhatu + dhatu.sanadiPratyayas +
                unadiPratyaya + vikaras.suffixes()
            ).segmented()
        is SamasaPratipadika -> {
            val base = angas.joinToString("-") { it.pratipadika.morphologicalKey(includeTaddhita) }
            (listOf(base) + vikaras.suffixes()).segmented()
        }
        is SankhyaPratipadika -> (listOf(semanticValue.word) + vikaras.suffixes()).segmented()
    }
}

data class MulaPratipadika(
    override val sourceText: String,
    val text: String,
    val vikaras: List<PratipadikaVikara> = emptyList(),
) : Pratipadika {
    val lexicalIdentity: MulaPratipadikaIdentity? = MulaPratipadikaIdentity.fromText(text)
}

enum class MulaPratipadikaIdentity {
    ADHIKARA,
    ANTARANGA,
    ANTA,
    APAVADA,
    ADHIKA,
    ASATYA,
    GUPTA,
    KSHETRA,
    MANA,
    NITYA,
    NYUNA,
    PHALA,
    PARINAMA,
    PURVA,
    ORDINAL_FIRST,
    ORDINAL_SECOND,
    ORDINAL_THIRD,
    PRAKRIYA,
    PRAYATNA,
    SAMJNA,
    SAMA,
    SAMAVAYA,
    SAMAPTA,
    SATYA,
    SANKHYA,
    SHABDA,
    SIMA,
    SUCHI,
    VIJAYA,
    ;

    companion object {
        fun fromText(text: String): MulaPratipadikaIdentity? = when (normalize(text)) {
            "अधिकार" -> ADHIKARA
            "अन्तरङ्ग", "अन्तरङ्गा", "अन्तर् + अङ्ग" -> ANTARANGA
            "अन्त" -> ANTA
            "अपवाद" -> APAVADA
            "अधिक" -> ADHIKA
            "असत्य" -> ASATYA
            "गुप्त" -> GUPTA
            "क्षेत्र" -> KSHETRA
            "मान" -> MANA
            "नित्य", "नि + त्य" -> NITYA
            "न्यून" -> NYUNA
            "फल" -> PHALA
            "परिणाम" -> PARINAMA
            "पूर्व" -> PURVA
            "प्रथम", "प्रथमा" -> ORDINAL_FIRST
            "द्वितीय", "द्वितीया" -> ORDINAL_SECOND
            "तृतीय", "तृतीया" -> ORDINAL_THIRD
            "प्रक्रिया" -> PRAKRIYA
            "प्रयत्न" -> PRAYATNA
            "संज्ञा" -> SAMJNA
            "सम" -> SAMA
            "समवाय" -> SAMAVAYA
            "समाप्त" -> SAMAPTA
            "सत्य" -> SATYA
            "सङ्ख्या" -> SANKHYA
            "शब्द" -> SHABDA
            "सीमा" -> SIMA
            "सूची" -> SUCHI
            "विजय" -> VIJAYA
            else -> null
        }

        private fun normalize(text: String): String =
            text.split('+').joinToString(" + ") { it.trim() }.trim()
    }

    val ordinalValue: Long?
        get() = when (this) {
            ORDINAL_FIRST -> 1L
            ORDINAL_SECOND -> 2L
            ORDINAL_THIRD -> 3L
            else -> null
        }
}

data class KridantaPratipadika(
    override val sourceText: String,
    val upasargas: List<String>,
    val dhatu: DhatuPrakriti,
    val krtPratyaya: String,
    val vikaras: List<PratipadikaVikara> = emptyList(),
) : Pratipadika {
    val krtPratyayaIdentity: KrtPratyayaIdentity? = KrtPratyayaIdentity.fromUpadesha(krtPratyaya)
    val lexicalIdentity: KridantaLexicalIdentity? = KridantaLexicalIdentity.fromStructure(
        upasargas = upasargas,
        mulaDhatu = dhatu.mulaDhatu,
        krtPratyaya = krtPratyayaIdentity,
    )
}

enum class KridantaLexicalIdentity {
    ADHIKARA,
    APAVADA,
    ;

    companion object {
        fun fromStructure(
            upasargas: List<String>,
            mulaDhatu: String,
            krtPratyaya: KrtPratyayaIdentity?,
        ): KridantaLexicalIdentity? = when {
            upasargas == listOf("अधि") &&
                mulaDhatu == "कृ" &&
                krtPratyaya == KrtPratyayaIdentity.GHAN -> ADHIKARA
            upasargas == listOf("अप") &&
                mulaDhatu == "वद्" &&
                krtPratyaya == KrtPratyayaIdentity.GHAN -> APAVADA
            else -> null
        }
    }
}

enum class KrtPratyayaIdentity {
    KTA,
    GHAN,
    ;

    companion object {
        fun fromUpadesha(upadesha: String): KrtPratyayaIdentity? = when (upadesha.trim()) {
            "क्त" -> KTA
            "घञ्" -> GHAN
            else -> null
        }
    }
}

data class UnadyantaPratipadika(
    override val sourceText: String,
    val upasargas: List<String>,
    val dhatu: DhatuPrakriti,
    val unadiPratyaya: String,
    val vikaras: List<PratipadikaVikara> = emptyList(),
) : Pratipadika

data class SamasaPratipadika(
    override val sourceText: String,
    val angas: List<SamasaAnga>,
    val vikaras: List<PratipadikaVikara> = emptyList(),
) : Pratipadika

data class SankhyaPratipadika(
    override val sourceText: String,
    val semanticValue: SanskritValue.Sankhya,
    val vikaras: List<PratipadikaVikara> = emptyList(),
) : Pratipadika {
    /** Numeric identity retained independently of the source-written surface. */
    val value: Long get() = semanticValue.value

    /** Compatibility constructor for callers that have not yet produced a typed value. */
    constructor(
        sourceText: String,
        value: Long,
        vikaras: List<PratipadikaVikara> = emptyList(),
    ) : this(
        sourceText = sourceText,
        semanticValue = SanskritValue.Sankhya(value, sourceText),
        vikaras = vikaras,
    )
}

sealed interface SankhyaBoundaryPada : Pada {
    val stems: List<String>
    val value: Long?
    val sup: SupPratyaya
}

data class SankhyaPada(
    override val sourceText: String,
    override val stems: List<String>,
    override val value: Long? = null,
    override val sup: SupPratyaya,
) : SankhyaBoundaryPada

data class SankhyaPuranaPada(
    override val sourceText: String,
    override val stems: List<String>,
    override val value: Long? = null,
    override val sup: SupPratyaya,
) : SankhyaBoundaryPada

data class SankhyaAbhyasaPada(
    override val sourceText: String,
    val stems: List<String>,
    val value: Long? = null,
) : Pada

data class KatapayadiPada(
    override val sourceText: String,
    val word: String,
    val value: Long? = null,
    val sup: SupPratyaya,
) : Pada

data class AryabhatiyaPada(
    override val sourceText: String,
    val word: String,
    val value: Long? = null,
    val sup: SupPratyaya,
) : Pada

data class BhutasamkhyaPada(
    override val sourceText: String,
    val terms: List<String>,
    val value: Long? = null,
    val sup: SupPratyaya,
) : Pada

data class SamasaAnga(
    override val sourceText: String,
    val pratipadika: Pratipadika,
    val sup: SupPratyaya? = null,
    val supLopa: SupLopa? = null,
) : VyakaranamNode

sealed interface PratipadikaVikara : VyakaranamNode

enum class TaddhitaPratyayaClass {
    POSSESSIVE,
    APATYA,
    BHAVA,
    ;

    companion object {
        fun fromUpadesha(upadesha: String): TaddhitaPratyayaClass? = when (upadesha.trim()) {
            "मतुप्", "वतुप्", "मत्", "वत्" -> POSSESSIVE
            "अण्", "इञ्" -> APATYA
            "त्व", "तल्" -> BHAVA
            else -> null
        }
    }
}

data class TaddhitaVikara(
    override val sourceText: String,
    val pratyaya: String,
) : PratipadikaVikara {
    val pratyayaClass: TaddhitaPratyayaClass? = TaddhitaPratyayaClass.fromUpadesha(pratyaya)
}

data class StriVikara(
    override val sourceText: String,
    val pratyaya: String,
) : PratipadikaVikara

data class DhatuPrakriti(
    override val sourceText: String,
    val mulaDhatu: String,
    val sanadiPratyayas: List<String> = emptyList(),
) : VyakaranamNode

data class SupPratyaya(
    override val sourceText: String,
    val text: String,
) : VyakaranamNode

data class TingPratyaya(
    override val sourceText: String,
    val text: String,
) : VyakaranamNode

sealed interface AvyayaDerivation

data class AvyayaKridantaDerivation(
    val upasargas: List<String>,
    val dhatu: DhatuPrakriti,
    val pratyaya: String,
) : AvyayaDerivation

data class AvyayaTaddhitaDerivation(
    val pratipadika: String,
    val pratyaya: String,
) : AvyayaDerivation

data class AvyayibhavaDerivation(
    val samasa: SamasaPratipadika,
) : AvyayaDerivation

data class SankhyaAvyayaDerivation(
    val kind: String, // "ADHIKA", "UNA", "KRITVAS", "DHA", "SHAS"
    val stems: List<String> = emptyList(),
) : AvyayaDerivation
