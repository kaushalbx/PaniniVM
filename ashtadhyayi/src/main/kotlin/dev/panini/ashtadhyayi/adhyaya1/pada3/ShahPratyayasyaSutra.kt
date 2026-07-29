package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.ashtadhyayi.runtime.ContextualSamjnaSutra
import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.ContextualSamjnaAssignmentArtha
import dev.panini.sutra.SamjnaAssignmentTarget
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.3.6: ṣaḥ pratyayasya.
 * Initial 'ṣ' of an affix is an it-marker.
 */
object ShahPratyayasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.6",
    text = "षः प्रत्ययस्य",
    hindiExplanation = "प्रत्यय के आदि में स्थित षकार की इत् संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130006,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.PRATYAYA,
    artha = ContextualSamjnaAssignmentArtha(
        target = SamjnaAssignmentTarget.PRATYAYA_INITIAL_SSA,
        samjna = Samjna.IT,
    ),
), DerivationSutra, ContextualSamjnaSutra {
    override fun hasSamjnaTarget(state: DerivationState): Boolean {
        if (state.stage != DerivationStage.PRATYAYA_SELECTED) return false

        return state.terms.any { term ->
            term.kind == TermKind.PRATYAYA && term.surface.startsWith('ष')
        }
    }

    override fun assignSamjna(state: DerivationState): DerivationChange {
        val newTerms = state.terms.map { term ->
            if (term.kind == TermKind.PRATYAYA && term.surface.startsWith('ष')) {
                term.copy(itMarkers = term.itMarkers + ItMarker.SH)
            } else term
        }

        return DerivationChange(
            state = state.copy(terms = newTerms),
            explanation = "1.3.6: Assigned it-status to initial 'ṣ' of the affix."
        )
    }

    override fun matches(context: DerivationState): Boolean = hasSamjnaTarget(context)

    override fun apply(context: DerivationState): DerivationChange = assignSamjna(context)
}
