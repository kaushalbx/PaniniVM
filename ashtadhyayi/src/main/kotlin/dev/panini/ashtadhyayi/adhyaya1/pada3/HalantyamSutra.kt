package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignation
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 1.3.3: hal antyam. The final consonant of an upadeśa is an it-marker. */
object HalantyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.3",
    text = "हलन्त्यम्",
    hindiExplanation = "उपदेश के अन्त में आने वाला हल् (व्यञ्जन) इत् संज्ञक होता है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130003,
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
            // A processed term whose effective surface already differs from its
            // recorded upadeśa is not a newly introduced raw upadeśa.
            if (!term.itProcessingPending && term.surface != term.upadesha) return@any false
            if (term.kind == TermKind.DHATU && !term.itProcessingPending) return@any false
            if (term.kind == TermKind.PRATIPADIKA) return@any false
            if (term.id in state.halantyamExemptTermIds) return@any false
            // These āgamas are already resolved to their effective surfaces;
            // their surviving final consonants are not new it-markers.
            if (!term.itProcessingPending && term.id in setOf("siyut", "yasut", "vuk", "nic")) return@any false
            term.varnas.lastOrNull() is Vyanjana &&
                (term.itDesignations + term.deferredItDesignations).none { it.endExclusive == term.surface.length }
        }
    }

    fun assignSamjna(state: DerivationState): DerivationChange {
        val pendingIds = state.terms.filter { it.itProcessingPending }.mapTo(mutableSetOf()) { it.id }
        val newTerms = state.terms.map { term ->
            if (pendingIds.isNotEmpty() && term.id !in pendingIds) return@map term
            if (!term.itProcessingPending && term.surface != term.upadesha) return@map term
            if (term.kind == TermKind.DHATU && !term.itProcessingPending) return@map term
            if (term.kind == TermKind.PRATIPADIKA) return@map term
            if (term.id in state.halantyamExemptTermIds) return@map term
            if (!term.itProcessingPending && term.id in setOf("siyut", "yasut", "vuk", "nic")) return@map term
            val last = term.varnas.lastOrNull()
            if (last is Vyanjana) {
                val isUndesignated = (term.itDesignations + term.deferredItDesignations).none { it.endExclusive == term.surface.length }
                if (isUndesignated) {
                    val marker = when (last) {
                        Vyanjana.KA -> ItMarker.KIT
                        Vyanjana.PA -> ItMarker.P
                        Vyanjana.NGA -> ItMarker.NGIT
                        Vyanjana.NNA -> ItMarker.NIT
                        Vyanjana.NYA -> ItMarker.NYIT
                        Vyanjana.SHA, Vyanjana.SSA -> ItMarker.SH
                        else -> ItMarker.GENERIC
                    }
                    val start = term.orthographicStartOfFinalVarna()
                    val designation = ItDesignation(
                        start,
                        term.surface.length,
                        marker = marker,
                        sutra = sutra,
                        designatedText = term.orthographicDesignationText(start, term.surface.length),
                    )
                    term.copy(
                        itMarkers = term.itMarkers + marker,
                        itProcessingPhase = if (term.itProcessingPending) dev.panini.derivation.ItProcessingPhase.DESIGNATED else term.itProcessingPhase,
                        itDesignations = if (term.itProcessingPending) term.itDesignations + designation else term.itDesignations,
                        deferredItDesignations = if (term.itProcessingPending) term.deferredItDesignations else term.deferredItDesignations + designation,
                    )
                } else {
                    term
                }
            } else {
                term
            }
        }

        return DerivationChange(
            state = state.copy(terms = newTerms),
            explanation = "1.3.3: Assigned it-status to final consonants."
        )
    }

    override fun matches(context: DerivationState): Boolean = hasSamjnaTarget(context)

    override fun apply(context: DerivationState): DerivationChange = assignSamjna(context)

}
