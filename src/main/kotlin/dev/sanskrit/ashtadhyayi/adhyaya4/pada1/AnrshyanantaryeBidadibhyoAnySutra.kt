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

/** 4.1.104: अनृष्यानन्तर्ये बिदादिभ्योऽञ्. */
object AnrshyanantaryeBidadibhyoAnySutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.104", text = "अनृष्यानन्तर्ये बिदादिभ्योऽञ्",
    hindiExplanation = "गोत्र अथवा अनृषि के अनन्तर अपत्य के अर्थ में बिदादि से अञ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410104,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        (SemanticFeature.GOTRA in context.semanticFeatures || SemanticFeature.ANANTARA_APATYA in context.semanticFeatures) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(58, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "अञ्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("any-suffix", "अ", TermKind.PRATYAYA, upadesha = "अञ्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "4.1.104 introduces अञ् after an eligible बिदादि term.",
    )
}
