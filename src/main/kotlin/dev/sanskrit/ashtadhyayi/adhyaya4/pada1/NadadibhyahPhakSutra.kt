package dev.sanskrit.ashtadhyayi.adhyaya4.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.shiksha.SemanticFeature
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
        SemanticFeature.APATYA in context.semanticFeatures &&
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
