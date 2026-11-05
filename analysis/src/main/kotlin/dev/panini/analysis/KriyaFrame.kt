package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.dhatupatha.Dhatu
import dev.panini.vyakaranam.ast.Vakya

@JvmInline
value class KriyaId(val value: String) {
    init {
        require(value.isNotBlank()) { "A kriyā requires a non-blank identity." }
    }

    override fun toString(): String = value
}

data class KriyaHead(
    val analysis: TingantaAnalysis,
    val dhatu: Dhatu?,
) {
    val nonfiniteDerivation: dev.panini.vyakaranam.ast.AvyayaKridantaDerivation?
        get() = analysis.pada.priorAction
    val isFinite: Boolean get() = nonfiniteDerivation == null
    /** Unknown morphological identities must not become a less-specific action. */
    val actionIdentity: dev.panini.core.ActionIdentity?
        get() {
            val lexical = dhatu ?: return null
            val prefixes = analysis.pada.upasargaIdentities
            val sanadi = analysis.pada.dhatu.sanadiAffixes
            if (prefixes.any { it == null } || sanadi.any { it == null }) return null
            return dev.panini.core.ActionIdentity(lexical.id, prefixes.filterNotNull(), sanadi.filterNotNull())
        }
}

sealed interface FrameKarakaResolution {
    data class Resolved(val karaka: Karaka) : FrameKarakaResolution
    data class Ambiguous(val candidates: Set<Karaka>) : FrameKarakaResolution {
        init {
            require(candidates.size > 1) { "An ambiguous kāraka relation requires multiple candidates." }
        }
    }
    data class Unassigned(
        val reason: String,
        /** Morphological hypotheses retained even though no saṃjñā rule resolved them. */
        val candidates: Set<Karaka> = emptySet(),
    ) : FrameKarakaResolution
}

data class KarakaRelation(
    val kriyaId: KriyaId,
    val participant: SubantaAnalysis,
    val resolution: FrameKarakaResolution,
    val evidence: List<KarakaEvidence> = emptyList(),
    val alternatives: List<KarakaAlternative> = emptyList(),
    val rejectedRules: List<RejectedKarakaRule> = emptyList(),
    val resolvedVibhakti: dev.panini.core.Vibhakti? = null,
    val nominalDesignations: List<NominalDesignationResult> = emptyList(),
    val additionalDesignations: Set<KarakaDesignation> = emptySet(),
    val unresolvedObjectMediators: Map<String, Set<Karaka>> = emptyMap(),
    val primaryOptionalDecision: KarakaEvidence? = null,
    val coexistingKarakas: Set<Karaka> = emptySet(),
)

enum class KriyaQualificationKind {
    MANNER,
    FREQUENCY,
    INTENSITY,
    NEGATION,
    COURTESY,
    TEMPORAL_EXTENT,
    OTHER,
    TIME,
    PLACE,
    DEGREE,
    DISTRIBUTION,
}

data class KriyaQualification(
    val kriyaId: KriyaId,
    val source: PadaAnalysis,
    val kind: KriyaQualificationKind,
    val value: String,
    val sourceIndex: Int? = null,
    val semantics: AdverbialSemantics? = null,
    val morphology: AdverbialMorphology = AdverbialMorphology.INDECLINABLE,
    /** Nominal readings remain possible for adverbially used case forms. */
    val competingNominalReading: Boolean = false,
)

sealed interface KriyaLink {
    val source: KriyaId
    val target: KriyaId

    data class Purvakalika(
        override val source: KriyaId,
        override val target: KriyaId,
    ) : KriyaLink

    data class Condition(
        override val source: KriyaId,
        override val target: KriyaId,
    ) : KriyaLink

    data class Alternative(
        override val source: KriyaId,
        override val target: KriyaId,
    ) : KriyaLink

    data class Coordination(
        override val source: KriyaId,
        override val target: KriyaId,
        val connector: String,
    ) : KriyaLink

    data class Purpose(
        override val source: KriyaId,
        override val target: KriyaId,
    ) : KriyaLink

    data class SharedParticipant(
        override val source: KriyaId,
        override val target: KriyaId,
        val participantSource: String,
        val karaka: Karaka,
        /** Frames in which this participant was morphologically explicit. */
        val explicitIn: Set<KriyaId>,
        /** Frames whose omitted participant is reconstructed by this link. */
        val omittedIn: Set<KriyaId>,
    ) : KriyaLink

    /** Re-evaluation of a loop test, not grammatical prior-action attachment. */
    data class LoopBack(override val source: KriyaId, override val target: KriyaId) : KriyaLink

    /** The bounded-loop exhaustion alternative. */
    data class Exhaustion(override val source: KriyaId, override val target: KriyaId) : KriyaLink

    /** Result-target clause after loop completion. */
    data class LoopResult(override val source: KriyaId, override val target: KriyaId) : KriyaLink
}

enum class FrameDiagnosticCode {
    PRIOR_ACTION_STRUCTURE,
    UNKNOWN_DHATU,
    AMBIGUOUS_KARAKA,
    MULTIPLE_INDEPENDENT_AGENTS,
    MULTIPLE_PRIMARY_OBJECTS,
    UNASSIGNED_PARTICIPANT,
    AGREEMENT_MISMATCH,
    UNCLASSIFIED_PADA,
    CONFLICTING_SHARED_AGENT,
    OPTIONAL_KARAKA,
    OPTIONAL_VIBHAKTI,
    AMBIGUOUS_VIBHAKTI,
    UNVALIDATED_VIBHAKTI,
    COMPETING_KARAKA_ANALYSIS,
    AMBIGUOUS_OBJECT_MEDIATION,
    /** A reading chosen by default where intention could select another. */
    DEFAULT_READING,
    /** A grammatical reading that an instruction may not take as an operand. */
    NON_BINDING_READING,
}

enum class FrameDiagnosticSeverity { INFO, WARNING, ERROR }

/** Central policy for whether a grammatical diagnostic may reach execution planning. */
object FrameDiagnosticPolicy {
    fun severity(code: FrameDiagnosticCode): FrameDiagnosticSeverity = when (code) {
        FrameDiagnosticCode.UNKNOWN_DHATU,
        FrameDiagnosticCode.PRIOR_ACTION_STRUCTURE -> FrameDiagnosticSeverity.ERROR
        FrameDiagnosticCode.UNCLASSIFIED_PADA -> FrameDiagnosticSeverity.INFO
        FrameDiagnosticCode.DEFAULT_READING,
        FrameDiagnosticCode.NON_BINDING_READING,
        FrameDiagnosticCode.OPTIONAL_KARAKA,
        FrameDiagnosticCode.OPTIONAL_VIBHAKTI -> FrameDiagnosticSeverity.INFO
        FrameDiagnosticCode.AMBIGUOUS_KARAKA,
        FrameDiagnosticCode.MULTIPLE_INDEPENDENT_AGENTS,
        FrameDiagnosticCode.MULTIPLE_PRIMARY_OBJECTS,
        FrameDiagnosticCode.AMBIGUOUS_VIBHAKTI,
        FrameDiagnosticCode.UNVALIDATED_VIBHAKTI,
        FrameDiagnosticCode.COMPETING_KARAKA_ANALYSIS,
        FrameDiagnosticCode.AMBIGUOUS_OBJECT_MEDIATION,
        FrameDiagnosticCode.UNASSIGNED_PARTICIPANT,
        FrameDiagnosticCode.AGREEMENT_MISMATCH,
        FrameDiagnosticCode.CONFLICTING_SHARED_AGENT,
        -> FrameDiagnosticSeverity.WARNING
    }

    fun blocksExecution(diagnostic: FrameDiagnostic): Boolean =
        severity(diagnostic.code) == FrameDiagnosticSeverity.ERROR
}

data class FrameDiagnostic(
    val code: FrameDiagnosticCode,
    val message: String,
    val sourceText: String? = null,
    /** Flattened nominal occurrence, when the diagnostic concerns one participant. */
    val nominalIndex: Int? = null,
) {
    val severity: FrameDiagnosticSeverity get() = FrameDiagnosticPolicy.severity(code)
}

data class KriyaFrame(
    val id: KriyaId,
    val vakya: Vakya,
    val kriya: KriyaHead?,
    val prayoga: Prayoga,
    val relations: List<KarakaRelation>,
    val qualifications: List<KriyaQualification>,
    val diagnostics: List<FrameDiagnostic> = emptyList(),
    val nominalRelations: NominalRelationAnalysis? = null,
    val nominalCaseLinks: NominalCaseLinkAnalysis? = null,
    val nominalKarakas: List<NominalKarakaRelation> = emptyList(),
    val purposeKarakas: List<PurposeKarakaRelation> = emptyList(),
    /** Original written clause when this is one action's grammatical projection. */
    val sourceVakya: Vakya = vakya,
)

/** Relations to a nominal action, separate from the finite head's participants. */
data class NominalKarakaRelation(val dependent: Int, val governor: Int, val relation: KarakaRelation)

data class PurposeKarakaRelation(val dependent: Int, val governor: PurposeActionGovernor, val relation: KarakaRelation)
