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

/** 3.1.79: तनादिकृञ्भ्य उः. The उ vikaraṇa follows Tanādi roots and कृञ्. */
object TanadiKrnjbhyahUhSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.79",
    text = "तनादिकृञ्भ्य उः",
    hindiExplanation = "तनादि-गण के धातुओं और कृञ् से परे उ विकरण होता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310079,
    role = SutraRole.Apavada,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    blocks = setOf("3.1.68", "7.3.84"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == Gana.TANADI &&
            context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
            context.allEffectiveTerms.none { it.id == "tanadi-u" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val u = DerivationTerm("tanadi-u", "उ", TermKind.PRATYAYA, upadesha = "उ")
        return DerivationChange(
            state = context.copy(terms = context.terms.dropLast(1) + u + context.terms.last()),
            explanation = "3.1.79 introduces उ after a Tanādi root.",
        )
    }
}
