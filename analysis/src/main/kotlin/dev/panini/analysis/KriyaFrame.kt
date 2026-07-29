package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.dhatupatha.Dhatu
import dev.panini.vyakaranam.ast.AkhyataVakya

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
)

sealed interface FrameKarakaResolution {
    data class Resolved(val karaka: Karaka) : FrameKarakaResolution
    data class Ambiguous(val candidates: Set<Karaka>) : FrameKarakaResolution {
        init {
            require(candidates.size > 1) { "An ambiguous kāraka relation requires multiple candidates." }
        }
    }
    data class Unassigned(val reason: String) : FrameKarakaResolution
}

data class KarakaRelation(
    val kriyaId: KriyaId,
    val participant: SubantaAnalysis,
    val resolution: FrameKarakaResolution,
    val evidence: List<KarakaEvidence> = emptyList(),
)

enum class KriyaQualificationKind {
    MANNER,
    FREQUENCY,
    INTENSITY,
    NEGATION,
    COURTESY,
    TEMPORAL_EXTENT,
    OTHER,
}

data class KriyaQualification(
    val kriyaId: KriyaId,
    val source: PadaAnalysis,
    val kind: KriyaQualificationKind,
    val value: String,
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
    ) : KriyaLink
}

enum class FrameDiagnosticCode {
    UNKNOWN_DHATU,
    AMBIGUOUS_KARAKA,
    UNASSIGNED_PARTICIPANT,
    AGREEMENT_MISMATCH,
    UNCLASSIFIED_PADA,
}

data class FrameDiagnostic(
    val code: FrameDiagnosticCode,
    val message: String,
    val sourceText: String? = null,
)

data class KriyaFrame(
    val id: KriyaId,
    val vakya: AkhyataVakya,
    val kriya: KriyaHead,
    val prayoga: Prayoga,
    val relations: List<KarakaRelation>,
    val qualifications: List<KriyaQualification>,
    val links: List<KriyaLink> = emptyList(),
    val diagnostics: List<FrameDiagnostic> = emptyList(),
)
