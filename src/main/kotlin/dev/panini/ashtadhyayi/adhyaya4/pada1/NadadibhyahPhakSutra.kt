package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.99: नडादिभ्यः फक्. */
object NadadibhyahPhakSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.99",
    text = "नडादिभ्यः फक्",
    hindiExplanation = "अपत्य के अर्थ में नडादि शब्दों से फक् प्रत्यय होता है।",
    type = SutraType.APAVADA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410099,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.APATYA).matches(context) &&
            context.terms.any { term ->
                term.kind == TermKind.PRATIPADIKA &&
                    GanaPatha.isEligibleMember(57, term.surface, term.lexicalUses)
            } &&
            context.allEffectiveTerms.none { it.upadesha == "फक्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.addTerm(
            DerivationTerm("phak-suffix", "आयन", TermKind.PRATYAYA, upadesha = "फक्"),
        ).copy(stage = DerivationStage.PRATYAYA_SELECTED),
        explanation = "4.1.99 introduces फक् in the अपत्य sense after an eligible नडादि term.",
    )
}
