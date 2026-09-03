package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.3.57 ॠदोरप्.
 * Prescribes ap affix for ṛ-ending or u-ending roots.
 */
object RadorApsutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.57", text = "ॠदोरप्",
    hindiExplanation = "ऋकारान्त तथा उकारान्त धातुओं से 'अप्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330057,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha == "अप्" }

    override fun apply(context: DerivationState): DerivationChange {
        val ap = DerivationTerm("ap", "अप्", TermKind.PRATYAYA, upadesha = "अप्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(ap),
            explanation = "3.3.57 prescribes अप् affix for ṛ/u-ending roots.",
        )
    }
}
