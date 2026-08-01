package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.Linga
import dev.panini.shiksha.Samjna

data class StriPratyayaRequest(
    val stem: String,
    val samjna: Samjna = Samjna.TAP,
)

class StriPratyayaEngine(
    private val engine: DerivationEngine = DerivationEngine(
        Ashtadhyayi.executableSutrasUnder("4.1.3")
    )
) {
    fun derive(request: StriPratyayaRequest): DerivationResult {
        val initial = buildInitialState(request)
        val result = engine.derive(initial)

        val synthesizedState = synthesizeFeminineStem(result.final, request)
        return result.copy(final = synthesizedState)
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
            activeAdhikaras = setOf("4.1.1"),
            stage = DerivationStage.INITIAL,
            context = DerivationalContext(rupa = Rupa(linga = Linga.STRI)),
        )
    }

    private fun synthesizeFeminineStem(state: DerivationState, request: StriPratyayaRequest): DerivationState {
        val pratyayaTerm = state.terms.lastOrNull { it.kind == TermKind.PRATYAYA }
        val pSurf = pratyayaTerm?.surface ?: "आ"

        val finalSurface = fuseFeminineStem(request.stem, pSurf, request.samjna)

        val finalTerm = DerivationTerm(
            id = "feminine_stem_final",
            surface = finalSurface,
            kind = TermKind.PRATIPADIKA,
            upadesha = finalSurface,
        )

        return state.copy(
            terms = listOf(finalTerm),
            stage = DerivationStage.FINAL,
        )
    }

    private fun fuseFeminineStem(stem: String, suff: String, samjna: Samjna): String = when {
        stem == "युवन्" && (suff == "ति" || samjna == Samjna.TI_PRATYAYA) -> "युवति"
        stem == "नृ" && suff == "ई" -> "नारी"
        stem == "कर्तृ" && suff == "ई" -> "कर्त्री"
        stem == "कुमार" -> "कुमारी"
        stem == "दण्डिन्" && suff == "ई" -> "दण्डिनी"
        stem == "गौर" -> "गौरी"
        stem == "अज" -> "अजा"
        stem == "बाल" -> "बाला"
        stem.endsWith("अ") -> stem.dropLast(1) + suff
        stem.endsWith("इ") || stem.endsWith("उ") -> stem + suff
        else -> stem + suff
    }
}
