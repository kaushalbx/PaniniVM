package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** Ṭ-it places the corresponding nasal augment on the following vowel, not
 * on the preceding pada: its original nasal must remain pada-final. */
object NgamoHrasvadAciNgamunNityamSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.32", text = "ङमो ह्रस्वादचि ङमुण् नित्यम्",
    hindiExplanation = "ह्रस्व स्वर के बाद पदान्त ङ्, ण्, न् से परे स्वर को क्रमशः ङुट्, णुट्, नुट् आगम होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830032,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private val augments = mapOf(Vyanjana.NGA to "ङुँट्", Vyanjana.NNA to "णुँट्", Vyanjana.NA to "नुँट्")

    private fun target(context: DerivationState): Pair<DerivationTerm, String>? {
        for (index in 0 until context.terms.lastIndex) {
            val left = context.terms[index]
            val right = context.terms[index + 1]
            if (!context.isPadaFinalVarna(left, left.varnas.lastIndex)) continue
            if ((left.varnas.getOrNull(left.varnas.lastIndex - 1) as? Svara)?.isHrasva != true) continue
            val augment = augments[left.varnas.lastOrNull()] ?: continue
            if (right.varnas.firstOrNull() !is Svara || sutra in right.establishedBySutras) continue
            if (context.samjnas.none { it.targetId == right.id && it.samjna == Samjna.PADA }) continue
            return right to augment
        }
        return null
    }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (right, upadesha) = requireNotNull(target(context))
        val augment = DerivationTerm(
            id = "${right.id}-ngamun", surface = upadesha, upadesha = upadesha,
            kind = TermKind.AGAMA, createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = right.id, mergeIntoAugmentTarget = true,
        )
        return DerivationChange(context.addTerm(augment),
            "8.3.32 introduces the corresponding raw nasal augment for placement before the following vowel.")
    }
}
