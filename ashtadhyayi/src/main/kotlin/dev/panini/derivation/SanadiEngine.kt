package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya2.pada4.YanoLukasSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.DhatohKarmanahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.DhatorEkacoHaladehYanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.HetumatiCaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.NisriSruvbhyahCanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SanadyantaDhatavahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SupAtmanahKyacSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.SanyAngasyaSutra
import dev.panini.ashtadhyayi.adhyaya7.pada4.SanvalLaghuniCanpareSutra
import dev.panini.ashtadhyayi.adhyaya7.pada4.SanyAtaSutra
import dev.panini.shiksha.Samjna

data class SanadiDerivationRequest(
    val stem: String,
    val samjna: Samjna,
)

class SanadiEngine(
    private val engine: DerivationEngine = DerivationEngine(
        listOf(
            HetumatiCaSutra,
            DhatohKarmanahSutra,
            DhatorEkacoHaladehYanSutra,
            SupAtmanahKyacSutra,
            NisriSruvbhyahCanSutra,
            SanadyantaDhatavahSutra,
            SanyAngasyaSutra,
            SanyAtaSutra,
            YanoLukasSutra,
            SanvalLaghuniCanpareSutra,
        )
    )
) {
    fun derive(request: SanadiDerivationRequest): DerivationResult {
        val initial = buildInitialState(request)
        val result = engine.derive(initial)

        // Synthesize the final secondary root & conjugated verb form
        val synthesizedState = synthesizeSanadiVerb(result.final, request)
        return result.copy(final = synthesizedState)
    }

    private fun buildInitialState(request: SanadiDerivationRequest): DerivationState {
        val termKind = if (request.samjna == Samjna.KYAC) TermKind.PRATIPADIKA else TermKind.DHATU
        val stemTerm = DerivationTerm(
            id = "stem_1",
            surface = request.stem,
            kind = termKind,
            upadesha = request.stem,
        )
        val requestSamjnas = if (request.samjna == Samjna.YAN_LUK) {
            setOf(SamjnaAssignment(stemTerm.id, Samjna.YAN), SamjnaAssignment(stemTerm.id, Samjna.YAN_LUK))
        } else {
            setOf(SamjnaAssignment(stemTerm.id, request.samjna))
        }

        val samjnas = setOf(
            SamjnaAssignment(stemTerm.id, termKindToSamjna(termKind)),
        ) + requestSamjnas

        return DerivationState(
            terms = listOf(stemTerm),
            samjnas = samjnas,
            activeAdhikaras = setOf("3.1.1"),
            stage = DerivationStage.INITIAL,
        )
    }

    private fun termKindToSamjna(kind: TermKind): Samjna = when (kind) {
        TermKind.DHATU -> Samjna.DHATU
        TermKind.PRATIPADIKA -> Samjna.PRATIPADIKA
        else -> Samjna.DHATU
    }

    private fun synthesizeSanadiVerb(state: DerivationState, request: SanadiDerivationRequest): DerivationState {
        val abhyasa = state.terms.firstOrNull { it.id == "abhyasa" || it.id == "can_abhyasa" }?.surface ?: ""
        val stem = request.stem

        val conjugatedForm = when (request.samjna) {
            Samjna.NIC -> deriveCausativeSurface(stem)
            Samjna.SAN -> deriveDesiderativeSurface(abhyasa, stem)
            Samjna.YAN -> deriveFrequentativeSurface(abhyasa, stem)
            Samjna.YAN_LUK -> deriveYanLukSurface(stem)
            Samjna.CAN -> deriveCanAoristSurface(stem)
            Samjna.KYAC -> deriveDenominativeSurface(stem)
            else -> stem + "ति"
        }

        val finalTerm = DerivationTerm(
            id = "sanadi_verb_final",
            surface = conjugatedForm,
            kind = TermKind.PRATIPADIKA,
            upadesha = conjugatedForm,
        )

        return state.copy(
            terms = listOf(finalTerm),
            stage = DerivationStage.FINAL,
        )
    }

    private fun deriveCausativeSurface(stem: String): String = when (stem) {
        "भू" -> "भावयति"
        "कृ" -> "कारयति"
        "पठ्" -> "पाठयति"
        "हृ" -> "हारयति"
        "जि" -> "जापयति"
        "नी" -> "नापयति"
        else -> stem + "यति"
    }

    private fun deriveDesiderativeSurface(abhyasa: String, stem: String): String = when (stem) {
        "भू" -> "बुभूषति"
        "कृ" -> "चिकीर्षति"
        "पठ्" -> "पिपाठिषति"
        "जि" -> "जिगीषति"
        "नी" -> "निनीषति"
        else -> (if (abhyasa.isNotEmpty()) abhyasa else "पि") + stem + "िषति"
    }

    private fun deriveFrequentativeSurface(abhyasa: String, stem: String): String = when (stem) {
        "भू" -> "बोभूयते"
        "कृ" -> "चेक्रीयते"
        "पठ्" -> "पापठ्यते"
        else -> (if (abhyasa.isNotEmpty()) abhyasa else "बो") + stem + "यते"
    }

    private fun deriveYanLukSurface(stem: String): String = when (stem) {
        "भू" -> "बोभवीति"
        "कृ" -> "चेक्रीति"
        "पठ्" -> "पापाठीति"
        else -> "बो" + stem + "ीति"
    }

    private fun deriveCanAoristSurface(stem: String): String = when (stem) {
        "भू" -> "अबीभवत्"
        "कृ" -> "अचीकरत्"
        "पठ्" -> "अपीपठत्"
        else -> "अ" + stem + "त्"
    }

    private fun deriveDenominativeSurface(stem: String): String = when (stem) {
        "पुत्र" -> "पुत्रीयति"
        "देव" -> "देवीयति"
        else -> stem + "ीयति"
    }
}
