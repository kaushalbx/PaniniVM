package dev.sanskrit.ashtadhyayi.adhyaya4.pada3

import dev.sanskrit.derivation.*
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.sutra.*

/** 4.3.92: शण्डिकादिभ्यो ञ्यः. */
object ShandikadibhyoNyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.3.92", text = "शण्डिकादिभ्यो ञ्यः",
    hindiExplanation = "अभिजन के अर्थ में शण्डिकादि प्रातिपदिकों से ञ्य प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 3, optional = false, kramaValue = 430092,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = HasRequestedMeaning(DerivationalMeaning.ABHIJANA).matches(context) && context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(118, it.surface, it.lexicalUses) } && context.allEffectiveTerms.none { it.upadesha == "ञ्य" }
    override fun apply(context: DerivationState) = DerivationChange(context.addTerm(DerivationTerm("nya-suffix", "य", TermKind.PRATYAYA, upadesha = "ञ्य")), "4.3.92 introduces ञ्य after an eligible शण्डिकादि term in the abhijana sense.")
}
