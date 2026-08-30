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
 * 3.1.97: अचो यत्.
 * Introduces the kṛtya suffix 'यत्' (yat) after vowel-ending roots.
 */
object AcoYatSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.97",
    text = "अचो यत्",
    hindiExplanation = "अजन्त धातु से परे यत् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310097,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isYatRequested = context.samjnas.any { it.samjna == Samjna.YAT } || context.context.requestedMeaning == dev.panini.derivation.DerivationalMeaning.BHAVA
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isYatRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val yatTerm = DerivationTerm(
            id = "yat_pratyaya",
            surface = "यत्",
            kind = TermKind.PRATYAYA,
            upadesha = "यत्",
            createdBySutra = sutra,
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + yatTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.1.97 introduces suffix यत्."
        )
    }
}
