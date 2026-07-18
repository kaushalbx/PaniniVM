package dev.sanskrit.ashtadhyayi.adhyaya3.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.TingAffix
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.1.69: दिवादिभ्यः श्यन्. The श्यन् vikaraṇa follows Divādi roots. */
object DivadibhyahShyanSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.69",
    text = "दिवादिभ्यः श्यन्",
    hindiExplanation = "दिवादि-गण के धातुओं से परे श्यन् विकरण होता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310069,
    role = SutraRole.Apavada,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    blocks = setOf("3.1.68"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == Gana.DIVADI &&
            context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
            context.allEffectiveTerms.none { it.upadesha == "श्यन्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        // श् and final न् are it; retain the effective य् as the vikaraṇa surface.
        val shyan = DerivationTerm("shyan", "य", TermKind.PRATYAYA, upadesha = "श्यन्")
        return DerivationChange(
            state = context.insertBeforeTingOrLingAugment(shyan),
            explanation = "3.1.69 introduces श्यन् after a Divādi root.",
        )
    }
}
