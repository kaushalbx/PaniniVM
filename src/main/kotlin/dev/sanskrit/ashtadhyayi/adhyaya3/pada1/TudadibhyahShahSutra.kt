package dev.sanskrit.ashtadhyayi.adhyaya3.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.TingAffix
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.shiksha.Svara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
        return dhatu.gana == Gana.TUDADI &&
            context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
            context.allEffectiveTerms.none { it.upadesha == "श" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val sha = DerivationTerm("sha", Svara.A.devanagari, TermKind.PRATYAYA, upadesha = "श")
        return DerivationChange(
            state = context.copy(terms = context.terms.dropLast(1) + sha + context.terms.last()),
            explanation = "3.1.77 introduces श after a Tudādi root.",
        )
    }
}
