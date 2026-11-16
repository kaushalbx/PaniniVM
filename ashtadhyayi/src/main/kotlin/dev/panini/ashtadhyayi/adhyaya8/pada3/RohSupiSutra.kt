package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 8.3.16 रोः सुपि — before सुप् (locative plural) only the र् of रु becomes visarga (पयःसु); a word's
 * own र् stays and takes 8.3.59 next: चतुर् + सुप् → चतुर्षु, not चतुःसु. A niyama on 8.3.15.
 */
object RohSupiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.16", text = "रोः सुपि",
    hindiExplanation = "सुप् परे होने पर रु के ही रेफ को विसर्ग होता है, अन्य रेफ को नहीं।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830016,
    role = SutraRole.Niyama, action = SutraAction.NIYAMA, scope = SutraScope.DERIVATION,
    stage = SutraStage.VISARJANIYA, blocks = setOf("8.3.15"),
), DerivationSutra {
    private val rutvaSources = setOf("8.2.66", "8.3.6", "8.3.7", "8.3.10", "8.3.12")
    private fun target(state: DerivationState): Pair<DerivationTerm, DerivationTerm>? {
        for (index in 0 until state.terms.lastIndex) {
            val left = state.terms[index]
            val right = state.terms[index + 1]
            if (left.varnas.lastOrNull() != Vyanjana.RA || right.upadesha != "सुप्") continue
            if (state.substitutions.any { it.targetId == left.id && it.sutra in rutvaSources }) continue
            if (state.isBlockedAtBoundary("8.3.15", left.id, right.id)) continue
            return left to right
        }
        return null
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (left, right) = requireNotNull(target(context))
        return DerivationChange(context.blockAtBoundary("8.3.15", left.id, right.id, sutra),
            "8.3.16 keeps the repha of ${left.surface} before सुप्: only रु takes visarga there.")
    }
}
