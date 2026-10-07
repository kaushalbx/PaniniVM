package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.Linga
import dev.panini.core.ItMarker
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SutraStage

data class StriPratyayaRequest(
    val stem: String,
    val samjna: Samjna = Samjna.TAP,
    /** Markers established by the actual stem-forming affix derivation. */
    val sourceAffixItMarkers: Set<ItMarker> = emptySet(),
)

class StriPratyayaEngine(
    private val pipeline: DerivationPipeline = DerivationPipeline(
        stages = listOf(SutraStage.PRATYAYA_SELECTION, SutraStage.IT_PROCESSING, SutraStage.ANGAKARYA, SutraStage.VOWEL_SANDHI, SutraStage.SANDHI),
        sutrasForStage = Ashtadhyayi::striPratyayaSutrasAt,
        computeSvaraAtCompletion = false,
    ),
) {
    fun derive(request: StriPratyayaRequest): DerivationResult {
        val initial = buildInitialState(request)
        val result = pipeline.derive(initial)
        val requestedUpadesha = when (request.samjna) {
            Samjna.TAP -> "टाप्"
            Samjna.NIP -> "ङीप्"
            Samjna.NIS -> "ङीष्"
            Samjna.NIN -> "ङीन्"
            Samjna.TI_PRATYAYA -> "ति"
            else -> throw IllegalArgumentException("Unsupported feminine affix: ${request.samjna}")
        }
        require(result.applications.any { application ->
            application.delta.addedTerms.any { it.upadesha == requestedUpadesha }
        }) { "No supported rule licenses $requestedUpadesha after ${request.stem}." }
        return result.completeSvara()
    }

    private fun buildInitialState(request: StriPratyayaRequest): DerivationState {
        val stemTerm = DerivationTerm(
            id = "pratipadika_1",
            surface = request.stem,
            kind = TermKind.PRATIPADIKA,
            upadesha = request.stem,
            itMarkers = request.sourceAffixItMarkers,
        )
        val samjnas = setOf(
            SamjnaAssignment(stemTerm.id, Samjna.PRATIPADIKA),
            SamjnaAssignment(stemTerm.id, request.samjna),
        )

        return DerivationState(
            terms = listOf(stemTerm),
            samjnas = samjnas,
            activeAdhikaras = setOf("4.1.1", "4.1.3"),
            stage = DerivationStage.INITIAL,
            context = DerivationalContext(rupa = Rupa(linga = Linga.STRI)),
        )
    }

}
