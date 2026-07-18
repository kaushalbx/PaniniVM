package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
