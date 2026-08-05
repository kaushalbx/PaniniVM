package dev.panini.ashtadhyayi.adhyaya2.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.4.71: सुपो धातुप्रातिपदिकयोः.
 * Deletes internal case affixes (Sup-lopa) for compound and derived stems.
 */
object SupoDhatuPratipadikayohSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.4.71",
    text = "सुपो धातुप्रातिपदिकयोः",
    hindiExplanation = "धातु और प्रातिपदिक के अवयव सुप् प्रत्ययों का लुक् (लोप) होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 4,
    optional = false,
    kramaValue = 240071,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        val isSamasa = context.samjnas.any { it.samjna == Samjna.SAMASA }
        return isSamasa && context.terms.size >= 2
    }

    override fun apply(context: DerivationState): DerivationChange {
        // Pure Pāṇinian Sup-lopa: Deletes internal PRATYAYA terms from the compound context
        val pratyayaTerms = context.terms.filter { it.kind == TermKind.PRATYAYA }
        val newState = if (pratyayaTerms.isNotEmpty()) {
            context.terms.fold(context) { acc, term ->
                if (term.kind == TermKind.PRATYAYA) acc.removeTerm(term.id, number) else acc
            }
        } else {
            context.copy(stage = DerivationStage.ANGAKARYA)
        }
        return DerivationChange(
            state = newState,
            explanation = "2.4.71 deletes internal case affixes (Sup-lopa)."
        )
    }
}
