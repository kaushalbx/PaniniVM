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

/** 4.1.80: क्रौड्यादिभ्यश्च. */
object KraudyadibhyashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.80", text = "क्रौड्यादिभ्यश्च",
    hindiExplanation = "गोत्र और स्त्रीत्व के अर्थ में क्रौड्यादि प्रातिपदिकों से ष्यङ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410080,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.STRI in context.semanticFeatures &&
            SemanticFeature.GOTRA in context.semanticFeatures &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(52, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "ष्यङ्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("shyang-suffix", "य", TermKind.PRATYAYA, upadesha = "ष्यङ्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "4.1.80 introduces ष्यङ् after an eligible क्रौड्यादि term in feminine gotra derivation.",
    )
}
