package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.ContextualProhibitionArtha
import dev.panini.sutra.ProhibitionTarget
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.sutra.runtime.SutraId

/**
 * 1.3.4: na vibhaktau tusmāḥ.
 */
object NaVibhaktauTusmahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.4",
    text = "न विभक्तौ तुस्माः",
    hindiExplanation = "विभक्ति के अन्त में आने वाले त-वर्ग, स् और म् इत् संज्ञक नहीं होते।",
    type = SutraType.NISHEDHA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130004,
    role = SutraRole.Nishedha,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.PRATYAYA,
    blocks = setOf("1.3.3"),
    artha = ContextualProhibitionArtha(
        target = ProhibitionTarget.VIBHAKTI_FINAL_TUSMA,
        prohibitedSutra = SutraId("1.3.3"),
    ),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.PRATYAYA_SELECTED) return false

        return context.terms.any { term ->
            val isVibhakti = context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.PRATYAYA }
            isVibhakti && isTuSMa(term.surface)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        var state = context
        context.terms.forEach { term ->
            if (isTuSMa(term.surface)) {
                state = state.blockSutra("1.3.3", sutra)
            }
        }

        return DerivationChange(
            state = state,
            explanation = "1.3.4: Blocked 1.3.3 for vibhakti-final dentals, 's', or 'm'."
        )
    }

    private fun isTuSMa(surface: String): Boolean {
        return surface.endsWith("त्") || surface.endsWith("थ्") || surface.endsWith("द्") ||
               surface.endsWith("ध्") || surface.endsWith("न्") || surface.endsWith("स्") ||
               surface.endsWith("म्") || surface.endsWith("त") || surface.endsWith("थ") ||
               surface.endsWith("द") || surface.endsWith("ध") || surface.endsWith("न") ||
               surface.endsWith("स") || surface.endsWith("म")
    }
}
