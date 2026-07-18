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

/** 4.1.154: तिकादिभ्यः फिञ्. */
object TikadibhyahPhinySutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.154", text = "तिकादिभ्यः फिञ्",
    hindiExplanation = "अपत्य के अर्थ में तिकादि शब्दों से फिञ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410154,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.APATYA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(67, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "फिञ्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("phiny-suffix", "आयनि", TermKind.PRATYAYA, upadesha = "फिञ्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "4.1.154 introduces फिञ् after an eligible तिकादि term in the अपत्य sense.",
    )
}
