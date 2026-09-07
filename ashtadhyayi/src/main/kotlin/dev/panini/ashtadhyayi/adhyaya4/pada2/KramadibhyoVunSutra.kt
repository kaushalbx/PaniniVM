package dev.panini.ashtadhyayi.adhyaya4.pada2

import dev.panini.derivation.DerivationChange
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

/** 4.2.61: क्रमादिभ्यो वुन्. */
object KramadibhyoVunSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.61", text = "क्रमादिभ्यो वुन्",
    hindiExplanation = "तदधीते तद्वेद के अर्थ में क्रमादि प्रातिपदिकों से वुन् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420061,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.ADHYAYANA_VEDANA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(80, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "वुन्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("vun-suffix", "वुन्", TermKind.PRATYAYA, upadesha = "वुन्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)),
        "4.2.61 introduces वुन् after an eligible क्रमादि term in the study-or-knowledge sense.",
    )
}
