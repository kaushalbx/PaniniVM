package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.Linga
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SutraStage

data class StriPratyayaRequest(
    val stem: String,
    val samjna: Samjna = Samjna.TAP,
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
        return pipeline.derive(initial).completeSvara()
    }

    private fun buildInitialState(request: StriPratyayaRequest): DerivationState {
        val stemTerm = DerivationTerm(
            id = "pratipadika_1",
            surface = request.stem,
            kind = TermKind.PRATIPADIKA,
            upadesha = request.stem,
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
