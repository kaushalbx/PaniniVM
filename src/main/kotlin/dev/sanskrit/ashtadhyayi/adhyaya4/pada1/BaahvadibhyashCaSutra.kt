package dev.sanskrit.ashtadhyayi.adhyaya4.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationalMeaning
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.HasRequestedMeaning
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 4.1.96: बाह्वादिभ्यश्च. */
object BaahvadibhyashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.96", text = "बाह्वादिभ्यश्च",
    hindiExplanation = "अपत्य के अर्थ में बाह्वादि शब्दों से इञ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410096,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.APATYA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(55, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "इञ्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("iny-suffix", "इ", TermKind.PRATYAYA, upadesha = "इञ्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "4.1.96 introduces इञ् after an eligible बाह्वादि term.",
    )
}
