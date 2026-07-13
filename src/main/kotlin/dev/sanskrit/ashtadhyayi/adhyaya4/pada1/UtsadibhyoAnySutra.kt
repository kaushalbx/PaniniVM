package dev.sanskrit.ashtadhyayi.adhyaya4.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationalEnvironment
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.HasDerivationalEnvironment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 4.1.86: उत्सादिभ्योऽञ्. */
object UtsadibhyoAnySutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.86", text = "उत्सादिभ्योऽञ्",
    hindiExplanation = "प्राग्दीव्यतीय अर्थों में उत्सादि प्रातिपदिकों से अञ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410086,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasDerivationalEnvironment(DerivationalEnvironment.PRAGDIVYATIYA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(54, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "अञ्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("any-suffix", "अ", TermKind.PRATYAYA, upadesha = "अञ्")),
        "4.1.86 introduces अञ् after an eligible उत्सादि term in a prāg-dīvyatīya sense.",
    )
}
