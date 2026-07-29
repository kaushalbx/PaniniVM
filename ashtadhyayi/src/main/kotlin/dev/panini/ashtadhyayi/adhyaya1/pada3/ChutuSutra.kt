package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.ashtadhyayi.runtime.ContextualSamjnaSutra
import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.ContextualSamjnaAssignmentArtha
import dev.panini.sutra.SamjnaAssignmentTarget
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.3.7: chu-ṭū.
 * Initial characters of ca-varga and ṭa-varga in an affix are it-markers.
 */
object ChutuSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.7",
    text = "चुटू",
    hindiExplanation = "प्रत्यय के आदि में स्थित च-वर्ग और ट-वर्ग की इत् संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130007,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra, ContextualSamjnaSutra {
    override val artha = ContextualSamjnaAssignmentArtha(
        target = SamjnaAssignmentTarget.PRATYAYA_INITIAL_CU_TTU,
        samjna = Samjna.IT,
    )

    override fun hasSamjnaTarget(state: DerivationState): Boolean {
        if (state.stage != DerivationStage.PRATYAYA_SELECTED) return false

        return state.terms.any { term ->
            term.kind == TermKind.PRATYAYA && term.surface.isNotEmpty() &&
            (isCu(term.surface.first()) || isTtu(term.surface.first()))
        }
    }

    override fun assignSamjna(state: DerivationState): DerivationChange {
        val newTerms = state.terms.map { term ->
            if (term.kind == TermKind.PRATYAYA && term.surface.isNotEmpty()) {
                val firstChar = term.surface.first()
                when {
                    isCu(firstChar) -> term.copy(itMarkers = term.itMarkers + ItMarker.J) // 'J' used for Ñ-it/Cu-it
                    isTtu(firstChar) -> term.copy(itMarkers = term.itMarkers + ItMarker.T) // 'T' used for Ṇ-it/Ṭu-it
                    else -> term
                }
            } else term
        }

        return DerivationChange(
            state = state.copy(terms = newTerms),
            explanation = "1.3.7: Assigned it-status to initial ca-varga or ṭa-varga."
        )
    }

    override fun matches(context: DerivationState): Boolean = hasSamjnaTarget(context)

    override fun apply(context: DerivationState): DerivationChange = assignSamjna(context)

    private fun isCu(c: Char): Boolean = c in setOf('च', 'छ', 'ज', 'झ', 'ञ')
    private fun isTtu(c: Char): Boolean = c in setOf('ट', 'ठ', 'ड', 'ढ', 'ण')
}
