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

/** 4.1.110: अश्वादिभ्यः फञ्. */
object AshvadibhyahPhanySutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.110", text = "अश्वादिभ्यः फञ्",
    hindiExplanation = "गोत्र के अर्थ में अश्वादि शब्दों से फञ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410110,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.GOTRA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(60, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "फञ्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("phany-suffix", "आयन", TermKind.PRATYAYA, upadesha = "फञ्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "4.1.110 introduces फञ् after an eligible अश्वादि term in the गोत्र sense.",
    )
}
