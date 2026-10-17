package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** Kit augments are preceding-pada final members, not following initial members. */
object NgnohKuktukShariSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.28", text = "ङ्णोः कुक्टुक् शरि",
    hindiExplanation = "पदान्त ङ् और ण् को शर् परे क्रमशः कुक् और टुक् आगम विकल्प से होते हैं।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = true, kramaValue = 830028,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private val augments = mapOf(Vyanjana.NGA to "कुँक्", Vyanjana.NNA to "टुँक्")
    private fun target(state: DerivationState): Pair<DerivationTerm, String>? {
        for (left in state.terms) {
            val augment = augments[left.varnas.lastOrNull()] ?: continue
            if (!state.isPadaFinalVarna(left, left.varnas.lastIndex) || sutra in left.establishedBySutras) continue
            val following = state.firstVarnaAfterTerm(left.id) ?: continue
            if (Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.SHAR, following)) return left to augment
        }
        return null
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (left, upadesha) = requireNotNull(target(context))
        val augment = DerivationTerm("${left.id}-kuk-tuk", upadesha, TermKind.AGAMA,
            upadesha = upadesha, createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA, augmentTargetId = left.id)
        return DerivationChange(context.addTerm(augment),
            "8.3.28 optionally introduces the corresponding kit augment as a final member of the preceding pada.")
    }
}
