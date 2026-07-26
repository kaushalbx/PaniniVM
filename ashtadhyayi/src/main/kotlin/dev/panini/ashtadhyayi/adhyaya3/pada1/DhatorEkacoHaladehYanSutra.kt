package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.1.22: धातोरेकाचो हलादेः क्रियासमभिहारे यङ्.
 * Introduces the frequentative / intensive suffix 'यङ्' (yaṅ) after a monosyllabic consonant-initial root.
 */
object DhatorEkacoHaladehYanSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.22",
    text = "धातोरेकाचो हलादेः क्रियासमभिहारे यङ्",
    hindiExplanation = "क्रियासमभिहार (पौणःपुन्य/भृशार्थ) में एकाच् हलादि धातु से परे यङ् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310022,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isYanRequested = context.samjnas.any { it.samjna == Samjna.YAN } || context.context.requestedMeaning == dev.panini.derivation.DerivationalMeaning.BHAVA
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isYanRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val yanTerm = DerivationTerm(
            id = "yan_pratyaya",
            surface = "य",
            kind = TermKind.PRATYAYA,
            itMarkers = setOf(ItMarker.NG),
            upadesha = "यङ्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + yanTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.1.22 introduces frequentative suffix यङ् (य)."
        )
    }
}
