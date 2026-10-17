package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.hasCurrentAffix
import dev.panini.derivation.term
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.insertAfterTerm
import dev.panini.core.SanadiAffix
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.29 ऋतेरीयङ्: īyaṅ after the consonant-final sautradhātu ṛt.
 * The i in the citation ṛti is not an operative root-final vowel.
 */
object RterIyangSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.29", text = "ऋतेरीयङ्",
    hindiExplanation = "ऋत् सौत्र धातु से स्वार्थ में ईयङ् प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310029,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private fun root(context: DerivationState): DerivationTerm? =
        context.allEffectiveTerms.firstOrNull { it.kind == TermKind.DHATU &&
            (it.upadesha == "ऋत्" || it.varnas == listOf(Svara.R, Vyanjana.TA)) }

    override fun matches(context: DerivationState): Boolean =
        sutra !in context.appliedSutras &&
        root(context) != null &&
        context.allEffectiveTerms.none { it.hasCurrentAffix(SanadiAffix.IYANG) }

    override fun apply(context: DerivationState): DerivationChange {
        val iyang = SanadiAffix.IYANG.term("iyang").copy(
            createdBySutra = sutra, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.insertAfterTerm(requireNotNull(root(context)).id, iyang),
            explanation = "3.1.29 prescribes raw ईयङ् after the sautradhātu ऋत्.",
        )
    }
}
