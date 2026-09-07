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
 * Sūtra 3.3.94 स्त्रियां क्तिन्.
 * Prescribes ktin feminine action affix after roots.
 */
object StriyamKtinSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.94", text = "स्त्रियां क्तिन्",
    hindiExplanation = "स्त्रीलिङ्ग भाव अर्थ में धातुओं से 'क्तिन्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330094,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha == "क्तिन्" }

    override fun apply(context: DerivationState): DerivationChange {
        val ktin = DerivationTerm("ktin", "क्तिन्", TermKind.PRATYAYA, upadesha = "क्तिन्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(ktin),
            explanation = "3.3.94 prescribes क्तिन् feminine action affix.",
        )
    }
}
