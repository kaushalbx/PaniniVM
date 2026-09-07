package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
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
    blocks = setOf("6.1.77", "6.1.78", "6.1.87", "6.1.88", "6.1.97", "6.1.101"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        val aoristAffixes = setOf("च्लि", "सिँच्", "क्स", "चङ्", "अङ्", "चिण्")
        return ending.matchesUpadesha("लुङ्") &&
            context.allEffectiveTerms.none { it.upadesha in aoristAffixes || it.id == "cli" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        // च्लि is the substituted locus of 3.1.44ff., not an independently
        // processable upadeśa.  A later rule must consume it and explicitly
        // choose the lifecycle of its replacement.
        val cli = DerivationTerm(
            id = "cli",
            surface = "च्लि",
            kind = TermKind.PRATYAYA,
            upadesha = "च्लि",
            createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.DEFERRED_SUBSTITUTION,
        )
        return DerivationChange(
            context.copy(
                terms = context.terms.dropLast(1) + cli + ending,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            "3.1.43 introduces च्लि before the लुङ् ending.",
        )
    }
}
