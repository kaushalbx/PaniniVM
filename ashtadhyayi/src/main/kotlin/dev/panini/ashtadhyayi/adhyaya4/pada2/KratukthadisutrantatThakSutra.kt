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

/** 4.2.60: क्रतूक्थादिसूत्रान्ताट्ठक्. */
object KratukthadisutrantatThakSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.60", text = "क्रतूक्थादिसूत्रान्ताट्ठक्",
    hindiExplanation = "तदधीते तद्वेद के अर्थ में उक्थादि प्रातिपदिकों से ठक् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420060,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.ADHYAYANA_VEDANA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(79, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "ठक्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("thak-suffix", "ठक्", TermKind.PRATYAYA, upadesha = "ठक्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)),
        "4.2.60 introduces ठक् after an eligible उक्थादि term in the study-or-knowledge sense.",
    )
}
