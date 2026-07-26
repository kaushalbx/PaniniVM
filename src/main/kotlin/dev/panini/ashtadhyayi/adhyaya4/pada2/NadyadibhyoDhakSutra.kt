package dev.panini.ashtadhyayi.adhyaya4.pada2

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

/** 4.2.97: नद्यादिभ्यो ढक्. */
object NadyadibhyoDhakSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.97", text = "नद्यादिभ्यो ढक्",
    hindiExplanation = "जात अर्थ में नद्यादि प्रातिपदिकों से ढक् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420097,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = HasRequestedMeaning(DerivationalMeaning.JATA).matches(context) &&
        context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(106, it.surface, it.lexicalUses) } &&
        context.allEffectiveTerms.none { it.upadesha == "ढक्" }

    override fun apply(context: DerivationState) = DerivationChange(
        context.addTerm(DerivationTerm("dhak-suffix", "एय", TermKind.PRATYAYA, upadesha = "ढक्")),
        "4.2.97 introduces ढक् after an eligible नद्यादि term in the birth-or-origin sense.",
    )
}
