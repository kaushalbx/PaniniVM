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

/** 4.2.49: पाशादिभ्यो यः. */
object PashadibhyoYahSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.49", text = "पाशादिभ्यो यः",
    hindiExplanation = "समूह के अर्थ में पाशादि प्रातिपदिकों से य प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420049,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.SAMUHA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(74, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "य" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("ya-suffix", "य", TermKind.PRATYAYA, upadesha = "य")),
        "4.2.49 introduces य after an eligible पाशादि term in the collective sense.",
    )
}
