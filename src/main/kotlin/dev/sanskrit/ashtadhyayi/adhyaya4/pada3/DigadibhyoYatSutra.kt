package dev.sanskrit.ashtadhyayi.adhyaya4.pada3

import dev.sanskrit.derivation.*
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.sutra.*

/** 4.3.54: दिगादिभ्यो यत्. */
object DigadibhyoYatSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.3.54", text = "दिगादिभ्यो यत्",
    hindiExplanation = "तत्र भव के अर्थ में दिगादि प्रातिपदिकों से यत् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 3, optional = false, kramaValue = 430054,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = HasRequestedMeaning(DerivationalMeaning.TATRA_BHAVA).matches(context) && context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(113, it.surface, it.lexicalUses) } && context.allEffectiveTerms.none { it.upadesha == "यत्" }
    override fun apply(context: DerivationState) = DerivationChange(context.addTerm(DerivationTerm("yat-suffix", "य", TermKind.PRATYAYA, upadesha = "यत्")), "4.3.54 introduces यत् after an eligible दिगादि term in the tatra-bhava sense.")
}
