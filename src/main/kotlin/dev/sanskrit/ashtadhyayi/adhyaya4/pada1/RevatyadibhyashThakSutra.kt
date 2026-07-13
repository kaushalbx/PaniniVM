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

/** 4.1.146: रेवत्यादिभ्यष्ठक्. */
object RevatyadibhyashThakSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.146", text = "रेवत्यादिभ्यष्ठक्",
    hindiExplanation = "अपत्य के अर्थ में रेवत्यादि शब्दों से ठक् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410146,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.APATYA in context.semanticFeatures &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(65, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "ठक्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("thak-suffix", "इक", TermKind.PRATYAYA, upadesha = "ठक्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "4.1.146 introduces ठक् after an eligible रेवत्यादि term in the अपत्य sense.",
    )
}
