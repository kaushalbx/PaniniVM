package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignation
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
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
    stage = dev.panini.sutra.SutraStage.IT_PROCESSING,
), DerivationSutra {
    fun hasSamjnaTarget(state: DerivationState): Boolean {
        if (state.stage != DerivationStage.PRATYAYA_SELECTED && state.terms.none { it.itProcessingPending }) return false

        return state.terms.any { term ->
            term.kind == TermKind.PRATYAYA && term.surface.isNotEmpty() &&
            (isCu(term.surface.first()) || isTtu(term.surface.first())) && term.itDesignations.none { it.start == 0 }
        }
    }

    fun assignSamjna(state: DerivationState): DerivationChange {
        val newTerms = state.terms.map { term ->
            if (term.kind == TermKind.PRATYAYA && term.surface.isNotEmpty()) {
                val firstChar = term.surface.first()
                when {
                    isCu(firstChar) -> designateInitial(term, ItMarker.J)
                    isTtu(firstChar) -> designateInitial(term, if (firstChar == 'ण') ItMarker.NIT else ItMarker.T)
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

    private fun designateInitial(term: dev.panini.derivation.DerivationTerm, marker: ItMarker): dev.panini.derivation.DerivationTerm {
        val sign = term.surface.getOrNull(1)
        val vowel = when (sign) {
            '्' -> ""
            'ा' -> "आ"; 'ि' -> "इ"; 'ी' -> "ई"; 'ु' -> "उ"; 'ू' -> "ऊ"
            'ृ' -> "ऋ"; 'ॄ' -> "ॠ"; 'ॢ' -> "ऌ"; 'े' -> "ए"; 'ै' -> "ऐ"; 'ो' -> "ओ"; 'ौ' -> "औ"
            else -> "अ"
        }
        val length = if (sign == '्' || sign in setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')) 2 else 1
        return term.copy(
            itMarkers = term.itMarkers + marker,
            itDesignations = if (term.itProcessingPending) {
                term.itDesignations + ItDesignation(0, length, vowel, marker, sutra)
            } else term.itDesignations,
        )
    }
}
