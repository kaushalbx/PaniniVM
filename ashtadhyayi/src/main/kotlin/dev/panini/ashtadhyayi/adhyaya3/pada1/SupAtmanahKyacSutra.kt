package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.1.8: सुपात्मनः क्यच्.
 * Introduces the denominative suffix 'क्यच्' (kyac) after a nominal stem expressing desire for oneself.
 */
object SupAtmanahKyacSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.8",
    text = "सुपात्मनः क्यच्",
    hindiExplanation = "आत्मसम्बन्धी सुबन्त से इच्छा अर्थ में क्यच् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310008,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isKyacRequested = context.samjnas.any { it.samjna == Samjna.KYAC }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isKyacRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val kyacTerm = DerivationTerm(
            id = "kyac_pratyaya",
            surface = "क्यच्",
            kind = TermKind.PRATYAYA,
            upadesha = "क्यच्",
            createdBySutra = sutra,
            itProcessingPending = true,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + kyacTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.1.8 introduces denominative suffix क्यच् (य)."
        )
    }
}
