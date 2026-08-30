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

/** 4.2.75: संकलादिभ्यश्च. */
object SankaladibhyashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.75", text = "संकलादिभ्यश्च",
    hindiExplanation = "तस्य निवास के अर्थ में संकलादि प्रातिपदिकों से अण् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420075,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.NIVASA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(82, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "अण्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("an-suffix", "अण्", TermKind.PRATYAYA, upadesha = "अण्", itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)),
        "4.2.75 introduces अण् after an eligible संकलादि term in the residence sense.",
    )
}
