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

/** 4.2.90: उत्करादिभ्यश्छः. */
object UtkaradibhyashChahSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.90", text = "उत्करादिभ्यश्छः",
    hindiExplanation = "चातुरर्थिक अर्थ में उत्करादि प्रातिपदिकों से छ प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420090,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = HasDerivationalEnvironment(DerivationalEnvironment.CHATURARTHIKA).matches(context) &&
        context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(103, it.surface, it.lexicalUses) } &&
        context.allEffectiveTerms.none { it.upadesha == "छ" }

    override fun apply(context: DerivationState) = DerivationChange(
        context.addTerm(DerivationTerm("chha-suffix", "ईय", TermKind.PRATYAYA, upadesha = "छ")),
        "4.2.90 introduces छ after an eligible उत्करादि term in a cāturarthika sense.",
    )
}
