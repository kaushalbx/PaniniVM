package dev.sanskrit.ashtadhyayi.adhyaya4.pada2

import dev.sanskrit.derivation.DerivationChange
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

/** 4.2.61: क्रमादिभ्यो वुन्. */
object KramadibhyoVunSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.61", text = "क्रमादिभ्यो वुन्",
    hindiExplanation = "तदधीते तद्वेद के अर्थ में क्रमादि प्रातिपदिकों से वुन् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420061,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.ADHYAYANA_VEDANA in context.semanticFeatures &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(80, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "वुन्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("vun-suffix", "इक", TermKind.PRATYAYA, upadesha = "वुन्")),
        "4.2.61 introduces वुन् after an eligible क्रमादि term in the study-or-knowledge sense.",
    )
}
