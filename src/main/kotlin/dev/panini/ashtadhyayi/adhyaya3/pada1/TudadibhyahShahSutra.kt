package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.DhatuGana
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Svara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.1.77: तुदादिभ्यः शः. The श vikaraṇa follows Tudādi roots. */
object TudadibhyahShahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.77",
    text = "तुदादिभ्यः शः",
    hindiExplanation = "तुदादि-गण के धातुओं से परे श विकरण होता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310077,
    role = SutraRole.Apavada,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    blocks = setOf("3.1.68"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == DhatuGana.TUDADI &&
            context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
            context.allEffectiveTerms.none { it.upadesha == "श" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val sha = DerivationTerm("sha", Svara.A.devanagari, TermKind.PRATYAYA, upadesha = "श")
        return DerivationChange(
            state = context.insertBeforeTingOrLingAugment(sha),
            explanation = "3.1.77 introduces श after a Tudādi root.",
        )
    }
}
