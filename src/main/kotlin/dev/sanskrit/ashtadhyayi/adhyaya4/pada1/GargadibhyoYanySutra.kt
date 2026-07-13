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

/** 4.1.105: गर्गादिभ्यो यञ्. */
object GargadibhyoYanySutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.105",
    text = "गर्गादिभ्यो यञ्",
    hindiExplanation = "अपत्य के अर्थ में गर्गादि शब्दों से यञ् प्रत्यय होता है।",
    type = SutraType.APAVADA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410105,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.APATYA in context.semanticFeatures &&
            context.terms.any { term ->
                term.kind == TermKind.PRATIPADIKA &&
                    GanaPatha.isEligibleMember(59, term.surface, term.lexicalUses)
            } &&
            context.allEffectiveTerms.none { it.upadesha == "यञ्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.addTerm(
            DerivationTerm("yany-suffix", "य", TermKind.PRATYAYA, upadesha = "यञ्"),
        ).copy(stage = DerivationStage.PRATYAYA_SELECTED),
        explanation = "4.1.105 introduces यञ् in the अपत्य sense after an eligible गर्गादि term.",
    )
}
