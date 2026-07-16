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

/** 3.1.73: स्वादिभ्यः श्नुः. The श्नु vikaraṇa follows Svādi roots. */
object SvadibhyahShnuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.73",
    text = "स्वादिभ्यः श्नुः",
    hindiExplanation = "स्वादि-गण के धातुओं से परे श्नु विकरण होता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310073,
    role = SutraRole.Apavada,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    blocks = setOf("3.1.68", "7.3.84"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == Gana.SVADI &&
            context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
            context.allEffectiveTerms.none { it.upadesha == "श्नु" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val shnu = DerivationTerm("shnu", "नु", TermKind.PRATYAYA, upadesha = "श्नु")
        return DerivationChange(
            state = context.copy(terms = context.terms.dropLast(1) + shnu + context.terms.last()),
            explanation = "3.1.73 introduces श्नु after a Svādi root.",
        )
    }
}
