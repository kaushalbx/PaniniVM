package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignation
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
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
        val pendingIds = state.terms.filter { it.itProcessingPending }.mapTo(mutableSetOf()) { it.id }

        return state.terms.any { term ->
            if (pendingIds.isNotEmpty() && term.id !in pendingIds) return@any false
            term.kind == TermKind.PRATYAYA && term.varnas.isNotEmpty() &&
            (isCu(term.varnas.first()) || isTtu(term.varnas.first())) &&
                (term.itDesignations + term.deferredItDesignations).none { it.start == 0 }
        }
    }

    fun assignSamjna(state: DerivationState): DerivationChange {
        val pendingIds = state.terms.filter { it.itProcessingPending }.mapTo(mutableSetOf()) { it.id }
        val newTerms = state.terms.map { term ->
            if (pendingIds.isNotEmpty() && term.id !in pendingIds) return@map term
            if (term.kind == TermKind.PRATYAYA && term.varnas.isNotEmpty()) {
                val firstVarna = term.varnas.first()
                when {
                    isCu(firstVarna) -> designateInitial(term, ItMarker.J)
                    isTtu(firstVarna) -> designateInitial(term, if (firstVarna == Vyanjana.NNA) ItMarker.NIT else ItMarker.T)
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

    private fun isCu(varna: Varna): Boolean = varna in setOf(Vyanjana.CA, Vyanjana.CHA, Vyanjana.JA, Vyanjana.JHA, Vyanjana.NYA)
    private fun isTtu(varna: Varna): Boolean = varna in setOf(Vyanjana.TTA, Vyanjana.TTHA, Vyanjana.DDA, Vyanjana.DDHA, Vyanjana.NNA)

    private fun designateInitial(term: dev.panini.derivation.DerivationTerm, marker: ItMarker): dev.panini.derivation.DerivationTerm {
        val length = term.orthographicEndAfterInitialVarna()
        val designation = ItDesignation(0, length, marker, sutra, designatedText = term.orthographicDesignationText(0, length), varnaIndices = setOf(0))
        val awaitsJhaSubstitution = term.itProcessingPending && term.upadesha in setOf("झ", "झि")
        return term.copy(
            itMarkers = term.itMarkers + marker,
            itProcessingPhase = when {
                awaitsJhaSubstitution -> dev.panini.derivation.ItProcessingPhase.DEFERRED_SUBSTITUTION
                term.itProcessingPending -> dev.panini.derivation.ItProcessingPhase.DESIGNATED
                else -> term.itProcessingPhase
            },
            itDesignations = if (term.itProcessingPending && !awaitsJhaSubstitution) term.itDesignations + designation else term.itDesignations,
            deferredItDesignations = if (awaitsJhaSubstitution || !term.itProcessingPending) term.deferredItDesignations + designation else term.deferredItDesignations,
        )
    }
}
