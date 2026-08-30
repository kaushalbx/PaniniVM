package dev.panini.ashtadhyayi.adhyaya4.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.3.76: शुण्डिकादिभ्योऽण्. */
object ShundikadibhyoAnSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.3.76", text = "शुण्डिकादिभ्योऽण्",
    hindiExplanation = "ततः आगत के अर्थ में शुण्डिकादि प्रातिपदिकों से अण् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 3, optional = false, kramaValue = 430076,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = HasRequestedMeaning(DerivationalMeaning.TATAH_AGATA).matches(context) && context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(117, it.surface, it.lexicalUses) } && context.allEffectiveTerms.none { it.upadesha == "अण्" }
    override fun apply(context: DerivationState) = DerivationChange(context.addTerm(DerivationTerm("an-suffix", "अण्", TermKind.PRATYAYA, upadesha = "अण्", itProcessingPending = true)), "4.3.76 introduces अण् after an eligible शुण्डिकादि source term.")
}
