package dev.panini.vyakaranam.ast

import dev.panini.core.Lakara
import dev.panini.core.KrtAffix
import dev.panini.core.SupLopa
import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.core.SanadiAffix
import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import dev.panini.core.Upasarga
import dev.panini.core.StriAffix
import dev.panini.core.TaddhitaAffix
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

/** Common shape for semantic failures produced at any analysis phase. */
interface SemanticDiagnostic<out C> {
    val code: C
    val message: String
    val span: DocumentSourceSpan?
    val offendingText: String
}

/** Orthographic provenance kept separate from grammatical equality. */
interface SourceLocatedNode {
    val sourceSpan: DocumentSourceSpan?
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
    fun grammaticalVakyas(): List<Vakya> = body.grammaticalInvocations().map(Invocation::vakya)
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
    /** Source-level nominal result; [vakya] retains the zero-copula nominal sentence. */
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
    /** Execute coordinated clauses independently without piping or reparsing them as one sentence. */
    val independentActions: Boolean = false,
) : ProgramNode {
    val connectorKinds: List<SequenceConnector> = connectors.map(SequenceConnector::fromSurface)
    /** Typed statement boundaries; consumers must not reinterpret connector text themselves. */
    val boundaries: List<SequenceBoundary> = connectorKinds.mapIndexed { index, connector ->
        SequenceBoundary(index, index + 1, connector, connector.resultFlow)
    }

    init {
        require(statements.isNotEmpty()) { "A sequence must contain at least one statement." }
        require(connectors.size <= statements.size - 1) {
            "A sequence cannot have more connectors than statement boundaries."
        }
    }
}

enum class SequenceResultFlow {
    NONE,
    /** The preceding successful value is available to an implicit target operand. */
    PREVIOUS_RESULT,
}

data class SequenceBoundary(
    val sourceStatementIndex: Int,
    val targetStatementIndex: Int,
    val connector: SequenceConnector,
    val resultFlow: SequenceResultFlow,
)

/** Typed discourse relation between adjacent executable clauses. */
enum class SequenceConnector(
    val surface: String,
    val resultFlow: SequenceResultFlow = SequenceResultFlow.NONE,
) {
    SAMUCCAYA("च"),
    /** A following independent sentence in the same selected branch. */
    VAKYANTARA("अथ"),
    /** A new sentence guarded by the nearest preceding conditional. */
    TADANIM("तदा"),
    ANANTARYA("ततः", SequenceResultFlow.PREVIOUS_RESULT),
    /** Ordering supplied by prior-action morphology, not a source connector word. */
    PURVAKALA(""),
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
    val relation: ConditionalRelation = ConditionalRelation.HYPOTHETICAL,
) : ProgramNode

enum class ConditionalRelation {
    /** यदि … तर्हि — a decision based on a proposition. */
    HYPOTHETICAL,
    /** यदा … तदा — an action selected when an event/state obtains now. */
    TEMPORAL,
}

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
        require(count >= 0) { "A repetition count must be nonnegative." }
    }
}

/** पुनः — execute this action again, licensed by a compatible earlier action. */
data class Recurrence(
    override val sourceText: String,
    val body: ProgramNode,
) : ProgramNode

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

/** A distributive प्रति clause; the member referent is scoped to its body. */
data class ForEach(
    override val sourceText: String,
    val collection: SubantaPada,
    val member: SubantaPada,
    val index: SubantaPada? = null,
    val body: ProgramNode,
    /** Anaphoric relations are retained so rendering preserves the author's gender and number. */
    val indexRelation: SubantaPada? = null,
    /** Retains agreement morphology such as feminine प्रत्येकाम्. */
    val quantifier: SubantaPada? = null,
    /** Typed collection operations; their meaning does not depend on the binding's variable name. */
    val relativeSelections: List<CollectionRelativeSelection> = emptyList(),
) : ProgramNode

enum class CollectionRelativeDirection { PREVIOUS, NEXT }
enum class CollectionRelativeCardinality { ITEM, ITEMS }

data class CollectionRelativeSelection(
    val direction: CollectionRelativeDirection,
    val cardinality: CollectionRelativeCardinality,
    val binding: SubantaPada,
    val relation: SubantaPada,
)

/** A grammatical relative operand, independent of any fabricated binding name. */
data class CollectionRelativeOperand(
    val relative: SubantaPada,
    /** Resolved member type; inferred from the current member when omitted in source. */
    val memberType: SubantaPada,
    val memberTypeWritten: Boolean,
    val direction: CollectionRelativeDirection,
    val cardinality: CollectionRelativeCardinality,
)

/** Apply one existing action between each member and its selected relative members. */
data class CollectionRelativeAction(
    override val sourceText: String,
    val collection: SubantaPada,
    val quantifier: SubantaPada,
    val member: SubantaPada,
    val operand: CollectionRelativeOperand,
    val action: TingantaPada,
    val continuation: ProgramNode? = null,
) : ProgramNode {
    /** The written finite action, projected as a grammatical clause for analysis. */
    fun actionInvocation(): Invocation = Invocation(
        AkhyataVakya(
            sourceText = action.sourceText,
            // The two operands are supplied by collection-relative semantics at execution time;
            // the member-type nouns describe them but are not additional runtime arguments.
            padas = listOf(action),
            tinganta = action,
        ),
    )
}

enum class CollectionQuantifier { ANY, ALL }

/** A truth-valued predicate evaluated over a scoped member of a collection. */
data class QuantifiedCollectionPredicate(
    override val sourceText: String,
    val collection: SubantaPada,
    val quantifier: CollectionQuantifier,
    val member: SubantaPada,
    val predicate: Invocation,
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
    /** Accusative product of a result-producing declaration predicate. */
    val declaredResult: SubantaPada? = null,
    val resultPredicate: TingantaPada? = null,
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

/** All grammatical clauses, including loop tests that orchestration evaluates separately. */
fun ProgramNode.grammaticalInvocations(): List<Invocation> =
    depthFirst(includeLoopConditions = true).filterIsInstance<Invocation>().toList()

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

/** A pada whose written morphology includes a sup ending, irrespective of value encoding. */
sealed interface SupBearingPada : Pada {
    val sup: SupPratyaya
}

data class SubantaPada(
    override val sourceText: String,
    val pratipadika: Pratipadika,
    override val sup: SupPratyaya,
) : SupBearingPada, SourceLocatedNode {
    /** Provenance is deliberately excluded from grammatical equality. */
    override var sourceSpan: DocumentSourceSpan? = null
        private set

    fun locatedAt(span: DocumentSourceSpan?): SubantaPada = apply { sourceSpan = span }
    fun inheritLocation(source: SubantaPada): SubantaPada = locatedAt(source.sourceSpan)
}

data class TingantaPada(
    override val sourceText: String,
    val upasargas: List<String>,
    val dhatu: DhatuPrakriti,
    val lakara: Lakara,
    val ting: TingPratyaya,
    /** Explicit gaṇa-vikaraṇa when the upadeśa alone is lexically ambiguous. */
    val vikarana: Vikarana? = null,
    /** Nonfinite source retained when this head is an execution-binding projection. */
    val priorAction: AvyayaKridantaDerivation? = null,
    /** Constraint retained by internal collection-declaration lowering. */
    val listMemberType: dev.panini.execution.ListMemberType? = null,
) : Pada, SourceLocatedNode {
    override var sourceSpan: DocumentSourceSpan? = null
        private set

    fun locatedAt(span: DocumentSourceSpan?): TingantaPada = apply { sourceSpan = span }
    fun inheritLocation(source: TingantaPada): TingantaPada = locatedAt(source.sourceSpan)

    val upasargaIdentities: List<Upasarga?> get() = upasargas.map(Upasarga::fromUpadesha)
}

/** Smallest source interval covering the written semantic relation. */
fun Invocation.relationSpan(): DocumentSourceSpan? {
    fun Pada.spans(): List<DocumentSourceSpan> = when (this) {
        is SourceLocatedNode -> listOfNotNull(sourceSpan)
        is SamuccitaSubanta -> members.flatMap { (it as Pada).spans() }
        is ParyantaRangePada -> lowerLimit.spans() + upperLimit.spans() + marker.spans()
        else -> emptyList()
    }
    val spans = vakya.padas.flatMap(Pada::spans)
    return if (spans.isEmpty()) null else DocumentSourceSpan(
        spans.minOf(DocumentSourceSpan::start),
        spans.maxOf(DocumentSourceSpan::endExclusive),
    )
}

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
    NAMING,
    REPETITION,
    ;

    companion object {
        fun fromForm(form: String): AvyayaFunction? = when (form.trim()) {
            "न", "मा" -> NISHEDHA
            "इति" -> QUOTATIVE
            "नाम" -> NAMING
            "पुनः", "पुनर्" -> REPETITION
            else -> null
        }
    }
}

data class SamuccitaSubanta(
    override val sourceText: String,
    val members: List<SupBearingPada>,
    /** Explicit च-delimited conjunct sizes; empty means a single final connective. */
    val conjunctSizes: List<Int> = emptyList(),
) : Pada {
    init {
        require(conjunctSizes.isEmpty() || conjunctSizes.all { it > 0 } &&
            conjunctSizes.sumOf { it.toLong() } == members.size.toLong()) {
            "Explicit coordination boundaries must cover every nominal occurrence."
        }
    }

    fun conjuncts(): List<List<SupBearingPada>> {
        if (conjunctSizes.isEmpty()) return listOf(members)
        var start = 0
        return conjunctSizes.map { size -> members.subList(start, start + size).also { start += size } }
    }
}

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
    /** Gender formation preserves ordinal rank; other derivation needs its own semantics. */
    val lexicalOrdinalValue: Long?
        get() = lexicalIdentity?.ordinalValue?.takeIf { vikaras.all { it.isRecognizedStriAffix } }
}

enum class MulaPratipadikaIdentity {
    ADHIKARA,
    ANTARANGA,
    ANTA,
    ANTIMA,
    APAVADA,
    ADHIKA,
    ASATYA,
    GUPTA,
    KSHETRA,
    MANA,
    MULYA,
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
            "अन्तिम" -> ANTIMA
            "अपवाद" -> APAVADA
            "अधिक" -> ADHIKA
            "असत्य" -> ASATYA
            "गुप्त" -> GUPTA
            "क्षेत्र" -> KSHETRA
            "मान" -> MANA
            "मूल्य" -> MULYA
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
    /** Shared grammatical identity, independent of the derived stem's ending. */
    val krtAffix: KrtAffix? = KrtAffix.fromUpadesha(krtPratyaya)
    /** Compatibility view of the same identity, not a second restricted affix catalog. */
    val krtPratyayaIdentity: KrtAffix? get() = krtAffix
    val upasargaIdentities: List<Upasarga?> get() = upasargas.map(Upasarga::fromUpadesha)
    val lexicalIdentity: KridantaLexicalIdentity? = if (dhatu.sanadiAffixes.isNotEmpty()) null else KridantaLexicalIdentity.fromStructure(
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
            krtPratyaya: KrtAffix?,
        ): KridantaLexicalIdentity? = when {
            upasargas.map(Upasarga::fromUpadesha) == listOf(Upasarga.ADHI) &&
                mulaDhatu == "कृ" &&
                krtPratyaya == KrtAffix.GHAN -> ADHIKARA
            upasargas.map(Upasarga::fromUpadesha) == listOf(Upasarga.APA) &&
                mulaDhatu == "वद्" &&
                krtPratyaya == KrtAffix.GHAN -> APAVADA
            else -> null
        }
    }
}

/** Source compatibility; [KrtAffix] is the single grammatical affix identity. */
typealias KrtPratyayaIdentity = KrtAffix

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

sealed interface SankhyaBoundaryPada : SupBearingPada {
    val stems: List<String>
    val value: Long?
    override val sup: SupPratyaya
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
    val striAffix: StriAffix? = null,
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
    override val sup: SupPratyaya,
) : SupBearingPada

data class AryabhatiyaPada(
    override val sourceText: String,
    val word: String,
    val value: Long? = null,
    override val sup: SupPratyaya,
) : SupBearingPada

data class BhutasamkhyaPada(
    override val sourceText: String,
    val terms: List<String>,
    val value: Long? = null,
    override val sup: SupPratyaya,
) : SupBearingPada

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
        fun fromUpadesha(upadesha: String): TaddhitaPratyayaClass? = when (TaddhitaAffix.fromUpadesha(upadesha)) {
            TaddhitaAffix.MATUP, TaddhitaAffix.VATUP, TaddhitaAffix.VAT -> POSSESSIVE
            TaddhitaAffix.AN, TaddhitaAffix.INY -> APATYA
            TaddhitaAffix.TVA, TaddhitaAffix.TAL -> BHAVA
            else -> null
        }
    }
}

data class TaddhitaVikara(
    override val sourceText: String,
    val pratyaya: String,
) : PratipadikaVikara {
    val affix: TaddhitaAffix? get() = TaddhitaAffix.fromUpadesha(pratyaya)
    val pratyayaClass: TaddhitaPratyayaClass? = TaddhitaPratyayaClass.fromUpadesha(pratyaya)
}

data class StriVikara(
    override val sourceText: String,
    val pratyaya: String,
) : PratipadikaVikara {
    val affix: StriAffix? get() = StriAffix.fromUpadesha(pratyaya)
}

/** An AST wrapper alone is not grammatical evidence of a feminine affix. */
val PratipadikaVikara.isRecognizedStriAffix: Boolean
    get() = this is StriVikara && affix != null

data class DhatuPrakriti(
    override val sourceText: String,
    val mulaDhatu: String,
    val sanadiPratyayas: List<String> = emptyList(),
) : VyakaranamNode {
    /** Preserve unknown affixes and their positions; never silently discard them. */
    val sanadiAffixes: List<SanadiAffix?> get() = sanadiPratyayas.map(SanadiAffix::fromUpadesha)
}

data class SupPratyaya(
    override val sourceText: String,
    val text: String,
) : VyakaranamNode {
    /** Syncretic endings retain every grammatical interpretation. */
    val affixCandidates: List<SupAffix> get() = SupAffix.candidates(text)

    /** Contextual compatibility, not a choice of one interpretation for the node. */
    fun supports(vibhakti: Vibhakti, vacana: Vacana? = null): Boolean =
        affixCandidates.any { it.vibhakti == vibhakti && (vacana == null || it.vacana == vacana) }

    /** Case and number must come from the same possible slot; unknown is not agreement. */
    fun sharesSlotWith(other: SupPratyaya): Boolean =
        affixCandidates.any { it in other.affixCandidates }
}

data class TingPratyaya(
    override val sourceText: String,
    val text: String,
) : VyakaranamNode {
    val affix: TingAffix? get() = TingAffix.fromUpadesha(text)
}

sealed interface AvyayaDerivation

data class AvyayaKridantaDerivation(
    val upasargas: List<String>,
    val dhatu: DhatuPrakriti,
    val pratyaya: String,
) : AvyayaDerivation {
    val krtAffix: KrtAffix? get() = KrtAffix.fromUpadesha(pratyaya)
    val upasargaIdentities: List<Upasarga?> get() = upasargas.map(Upasarga::fromUpadesha)
}

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
