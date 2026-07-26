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
