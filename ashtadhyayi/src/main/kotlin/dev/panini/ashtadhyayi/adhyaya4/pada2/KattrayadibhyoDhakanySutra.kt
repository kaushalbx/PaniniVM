package dev.panini.ashtadhyayi.adhyaya4.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.2.95: कत्त्र्यादिभ्यो ढकञ्. */
object KattrayadibhyoDhakanySutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.95", text = "कत्त्र्यादिभ्यो ढकञ्",
    hindiExplanation = "चातुरर्थिक अर्थ में कत्त्र्यादि प्रातिपदिकों से ढकञ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420095,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = HasDerivationalEnvironment(DerivationalEnvironment.CHATURARTHIKA).matches(context) &&
        context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(105, it.surface, it.lexicalUses) } &&
        context.allEffectiveTerms.none { it.upadesha == "ढकञ्" }

    override fun apply(context: DerivationState) = DerivationChange(
        context.addTerm(DerivationTerm("dhakany-suffix", "ढकञ्", TermKind.PRATYAYA, upadesha = "ढकञ्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)),
        "4.2.95 introduces ढकञ् after an eligible कत्त्र्यादि term in a cāturarthika sense.",
    )
}
