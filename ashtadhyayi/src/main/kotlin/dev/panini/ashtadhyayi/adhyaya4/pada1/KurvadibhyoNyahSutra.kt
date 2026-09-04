package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.151: कुर्वादिभ्यो ण्यः. */
object KurvadibhyoNyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.151", text = "कुर्वादिभ्यो ण्यः",
    hindiExplanation = "अपत्य के अर्थ में कुर्वादि शब्दों से ण्य प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410151,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.APATYA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(66, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "ण्य" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("nyah-suffix", "ण्य", TermKind.PRATYAYA, upadesha = "ण्य", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "4.1.151 introduces ण्य after an eligible कुर्वादि term in the अपत्य sense.",
    )
}
