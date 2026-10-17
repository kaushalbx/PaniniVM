package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.isPadaFinalVarna
import dev.panini.derivation.firstVarnaAfterTerm
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.nasalParasavarna
import dev.panini.shiksha.NasalParasavarna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.4.59: vā padāntasya.
 * Word-final anusvāra optionally becomes the following sound's nasal counterpart:
 * the fifth member of a stop class, or a nasalized y/v/l. Ra has no counterpart.
 */
object VaPadantasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.59",
    text = "वा पदान्तस्य",
    hindiExplanation = "पदान्त अनुस्वार का ययि परे रहते विकल्प से परसवर्ण (वर्ग का ५वाँ अनुनासिक वर्ण) आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840059,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean = findTarget(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (targetTerm, replacement) = requireNotNull(findTarget(context))

        return DerivationChange(
            state = context.replaceTermVarna(targetTerm.id, targetTerm.varnas.lastIndex, listOf(replacement.varna), sutra,
                nasalizedReplacement = replacement.nasalized),
            explanation = "8.4.59: Replaced final Anusvāra with nasal parasavarṇa $replacement before yay sound."
        )
    }

    private fun findTarget(context: DerivationState): Pair<DerivationTerm, NasalParasavarna>? {
        for (term in context.terms) {
            if (term.varnas.lastOrNull() != Ayogavaha.ANUSVARA ||
                !context.isPadaFinalVarna(term, term.varnas.lastIndex)) continue
            // A consumed/zero affix has identity and provenance but no sound.
            val follower = context.firstVarnaAfterTerm(term.id) ?: continue
            if (!Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.YAY, follower)) continue
            val replacement = follower.nasalParasavarna() ?: continue
            return term to replacement
        }
        return null
    }
}
