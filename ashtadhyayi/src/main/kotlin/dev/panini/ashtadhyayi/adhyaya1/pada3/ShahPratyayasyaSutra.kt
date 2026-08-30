package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignation
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.3.6: ṣaḥ pratyayasya.
 * Initial 'ṣ' of an affix is an it-marker.
 */
object ShahPratyayasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.6",
    text = "षः प्रत्ययस्य",
    hindiExplanation = "प्रत्यय के आदि में स्थित षकार की इत् संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130006,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.PRATYAYA,
    stage = dev.panini.sutra.SutraStage.IT_PROCESSING,
), DerivationSutra {
    fun hasSamjnaTarget(state: DerivationState): Boolean {
        if (state.stage != DerivationStage.PRATYAYA_SELECTED && state.terms.none { it.itProcessingPending }) return false

        return state.terms.any { term ->
            term.kind == TermKind.PRATYAYA && term.surface.startsWith('ष') &&
                (term.itDesignations + term.deferredItDesignations).none { it.start == 0 }
        }
    }

    fun assignSamjna(state: DerivationState): DerivationChange {
        val newTerms = state.terms.map { term ->
            if (term.kind == TermKind.PRATYAYA && term.surface.startsWith('ष')) {
                val end = if (term.surface.getOrNull(1) == '्') 2 else 1
                val designation = ItDesignation(
                    start = 0,
                    endExclusive = end,
                    replacementAfterLopa = if (end == 1) "अ" else "",
                    marker = ItMarker.SH,
                    sutra = sutra,
                    designatedText = term.surface.substring(0, end),
                )
                term.copy(
                    itMarkers = term.itMarkers + ItMarker.SH,
                    itProcessingPhase = if (term.itProcessingPending) dev.panini.derivation.ItProcessingPhase.DESIGNATED else term.itProcessingPhase,
                    itDesignations = if (term.itProcessingPending) term.itDesignations + designation else term.itDesignations,
                    deferredItDesignations = if (term.itProcessingPending) term.deferredItDesignations else term.deferredItDesignations + designation,
                )
            } else term
        }

        return DerivationChange(
            state = state.copy(terms = newTerms),
            explanation = "1.3.6: Assigned it-status to initial 'ṣ' of the affix."
        )
    }

    override fun matches(context: DerivationState): Boolean = hasSamjnaTarget(context)

    override fun apply(context: DerivationState): DerivationChange = assignSamjna(context)
}
