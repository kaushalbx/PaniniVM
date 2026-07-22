package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.core.TingAffix
import dev.panini.core.DhatuGana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        return dhatu.gana == DhatuGana.DIVADI &&
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
