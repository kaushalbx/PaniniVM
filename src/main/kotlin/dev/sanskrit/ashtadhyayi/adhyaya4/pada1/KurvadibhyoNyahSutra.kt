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

/** 4.1.151: कुर्वादिभ्यो ण्यः. */
object KurvadibhyoNyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.151", text = "कुर्वादिभ्यो ण्यः",
    hindiExplanation = "अपत्य के अर्थ में कुर्वादि शब्दों से ण्य प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410151,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.APATYA in context.semanticFeatures &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(66, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "ण्य" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("nyah-suffix", "य", TermKind.PRATYAYA, upadesha = "ण्य"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "4.1.151 introduces ण्य after an eligible कुर्वादि term in the अपत्य sense.",
    )
}
