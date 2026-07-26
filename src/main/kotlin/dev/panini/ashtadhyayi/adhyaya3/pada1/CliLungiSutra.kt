package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.1.43: च्लि लुङि. Introduces च्लि between the root and a लुङ् ending. */
object CliLungiSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.43",
    text = "च्लि लुङि",
    hindiExplanation = "लुङ् परे होने पर धातु से च्लि प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310043,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return ending.matchesUpadesha("लुङ्") &&
            context.allEffectiveTerms.none { it.upadesha == "च्लि" || it.upadesha == "सिच्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val cli = DerivationTerm("cli", "च्लि", TermKind.PRATYAYA, upadesha = "च्लि")
        return DerivationChange(
            context.copy(
                terms = context.terms.dropLast(1) + cli + ending,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            "3.1.43 introduces च्लि before the लुङ् ending.",
        )
    }
}
