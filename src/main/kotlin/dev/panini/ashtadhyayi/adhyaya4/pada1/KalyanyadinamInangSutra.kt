package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.126: कल्याण्यादीनामिनङ्. */
object KalyanyadinamInangSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.126", text = "कल्याण्यादीनामिनङ्",
    hindiExplanation = "अपत्य के अर्थ में कल्याण्यादि शब्दों से ढक् होता है और अन्त्य के स्थान पर इनङ् आदेश होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410126,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private fun bases(context: DerivationState) = context.terms.filter {
        it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(63, it.surface, it.lexicalUses)
    }

    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.APATYA).matches(context) && bases(context).isNotEmpty() &&
            context.allEffectiveTerms.none { it.upadesha == "ढक्" }

    override fun apply(context: DerivationState): DerivationChange {
        val replacements = bases(context).associate { term ->
            term.id to term.copy(surface = term.surface.dropLast(1) + "िन्")
        }
        return DerivationChange(
            context.copy(terms = context.terms.map { replacements[it.id] ?: it })
                .addTerm(DerivationTerm("kalyanyadi-dhak-suffix", "एय", TermKind.PRATYAYA, upadesha = "ढक्"))
                .copy(stage = DerivationStage.PRATYAYA_SELECTED),
            "4.1.126 replaces the final with इनङ् and introduces ढक् after an eligible कल्याण्यादि term.",
        )
    }
}
