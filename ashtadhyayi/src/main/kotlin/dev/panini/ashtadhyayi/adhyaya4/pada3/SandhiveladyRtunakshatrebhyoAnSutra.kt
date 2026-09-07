package dev.panini.ashtadhyayi.adhyaya4.pada3

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

/** 4.3.16: संधिवेलाद्यृतुनक्षत्रेभ्योऽण्. */
object SandhiveladyRtunakshatrebhyoAnSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.3.16", text = "संधिवेलाद्यृतुनक्षत्रेभ्योऽण्",
    hindiExplanation = "कालवृत्ति के शेष अर्थ में संधिवेलादि प्रातिपदिकों से अण् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 3, optional = false, kramaValue = 430016,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = HasDerivationalEnvironment(DerivationalEnvironment.KALAVRTTI).matches(context) && context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(112, it.surface, it.lexicalUses) } && context.allEffectiveTerms.none { it.upadesha == "अण्" }
    override fun apply(context: DerivationState) = DerivationChange(context.addTerm(DerivationTerm("an-suffix", "अण्", TermKind.PRATYAYA, upadesha = "अण्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)), "4.3.16 introduces अण् after an eligible संधिवेलादि time expression.")
}
