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

/** 4.3.92: शण्डिकादिभ्यो ञ्यः. */
object ShandikadibhyoNyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.3.92", text = "शण्डिकादिभ्यो ञ्यः",
    hindiExplanation = "अभिजन के अर्थ में शण्डिकादि प्रातिपदिकों से ञ्य प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 3, optional = false, kramaValue = 430092,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = HasRequestedMeaning(DerivationalMeaning.ABHIJANA).matches(context) && context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(118, it.surface, it.lexicalUses) } && context.allEffectiveTerms.none { it.upadesha == "ञ्य" }
    override fun apply(context: DerivationState) = DerivationChange(context.addTerm(DerivationTerm("nya-suffix", "ञ्य", TermKind.PRATYAYA, upadesha = "ञ्य", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)), "4.3.92 introduces ञ्य after an eligible शण्डिकादि term in the abhijana sense.")
}
