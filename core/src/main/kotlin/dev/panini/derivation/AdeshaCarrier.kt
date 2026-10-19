package dev.panini.derivation

import dev.panini.core.ItMarker
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari

/** Explicit producer-selected targeting; ṅit alone is not a universal final-only rule. */
enum class AdeshaPlacement { NGIT_FINAL, SHIT_WHOLE }

data class AdeshaTarget(
    val termId: String,
    val originalVarnas: List<Varna>,
    val placement: AdeshaPlacement,
) {
    init { require(termId.isNotBlank() && originalVarnas.isNotEmpty()) }
}

/** Link to the archived carrier, whose own it-ledger must not become the stem's markers. */
data class AdeshaOrigin(
    val carrierId: String,
    val upadesha: String,
    val prescribingSutra: String,
    val targetingSutra: String,
    val sourceVarnaIndex: Int,
    val itMarkerProvenance: Set<ItMarkerProvenance>,
)

/** Introduce raw ādeśa material without pretending it is a pratyaya or āgama. */
fun DerivationState.introduceRawAdesha(targetId: String, rawVarnas: List<Varna>,
    placement: AdeshaPlacement, sutra: String, carrierId: String,
    upadesha: String = rawVarnas.toDevanagari()): DerivationState {
    val target = terms.single { it.id == targetId }
    require(!target.itProcessingPending && target.itDesignations.isEmpty() && target.deferredItDesignations.isEmpty())
    require(target.kind != TermKind.ADESHA && target.varnas.isNotEmpty())
    require(rawVarnas.isNotEmpty() && sutra.isNotBlank() && carrierId.isNotBlank())
    require(terms.none { it.id == carrierId } && droppedTerms.none { it.id == carrierId })
    require(terms.none { it.adeshaTarget?.termId == targetId }) { "An ādeśa already targets $targetId." }
    when (placement) {
        AdeshaPlacement.NGIT_FINAL -> require(rawVarnas.last() == Vyanjana.NGA) {
            "Final-only ṅit targeting requires a raw ṅ-marker, not inferred marker metadata."
        }
        AdeshaPlacement.SHIT_WHOLE -> require(rawVarnas.last() in setOf(Vyanjana.SHA, Vyanjana.SSA)) {
            "Whole-term śit targeting requires a raw ś-marker, not inferred marker metadata."
        }
    }
    val rawSurface = rawVarnas.toDevanagari()
    val carrier = DerivationTerm(carrierId, rawSurface, TermKind.ADESHA, upadesha = upadesha,
        createdBySutra = sutra, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
        adeshaTarget = AdeshaTarget(targetId, target.varnas.toList(), placement))
    val index = terms.indexOf(target) + 1
    return copy(terms = terms.take(index) + carrier + terms.drop(index))
}

fun DerivationState.processedNgitAdeshas(): List<DerivationTerm> = terms.filter { carrier ->
    carrier.kind == TermKind.ADESHA && carrier.adeshaTarget?.placement == AdeshaPlacement.NGIT_FINAL &&
        carrier.itProcessingPhase == ItProcessingPhase.PROCESSED &&
        carrier.itDesignations.isEmpty() && carrier.deferredItDesignations.isEmpty() &&
        carrier.itMarkerProvenance.any { it.marker == ItMarker.NGIT } && carrier.varnas.isNotEmpty()
}

fun DerivationState.processedShitWholeAdeshas(): List<DerivationTerm> = terms.filter { carrier ->
    carrier.kind == TermKind.ADESHA && carrier.adeshaTarget?.placement == AdeshaPlacement.SHIT_WHOLE &&
        carrier.itProcessingPhase == ItProcessingPhase.PROCESSED &&
        carrier.itDesignations.isEmpty() && carrier.deferredItDesignations.isEmpty() &&
        carrier.itMarkerProvenance.any { it.marker == ItMarker.SH } && carrier.varnas.isNotEmpty()
}

/** Execute the prescribed substitution under explicit 1.1.53 targeting. */
fun DerivationState.integrateNgitAdesha(carrierId: String, targetingSutra: String): DerivationState {
    val carrier = processedNgitAdeshas().single { it.id == carrierId }
    val targetProof = requireNotNull(carrier.adeshaTarget)
    val target = terms.single { it.id == targetProof.termId }
    require(target.varnas == targetProof.originalVarnas) { "Stale ādeśa target ${target.id}; refusing to guess a new occurrence." }
    val sourceIndex = target.varnas.lastIndex
    val prescribingSutra = requireNotNull(carrier.createdBySutra)
    val substituted = replaceTermVarna(target.id, sourceIndex, carrier.varnas, prescribingSutra)
    val changed = substituted.terms.single { it.id == target.id }
    val origin = AdeshaOrigin(carrier.id, carrier.upadesha, prescribingSutra, targetingSutra,
        sourceIndex, carrier.itMarkerProvenance.toSet())
    val recorded = substituted.replaceTerm(target.id, changed.copy(
        adeshaOrigins = changed.adeshaOrigins + origin,
        establishedBySutras = changed.establishedBySutras + prescribingSutra))
    val archived = recorded.removeTerm(carrier.id, prescribingSutra)
    return archived.copy(droppedTerms = archived.droppedTerms.map {
        if (it.id == carrier.id) it.copy(mergedIntoTermId = target.id) else it
    })
}

/** Execute a śit substitute over the complete sthānin under 1.1.55. */
fun DerivationState.integrateShitWholeAdesha(carrierId: String, targetingSutra: String): DerivationState {
    val carrier = processedShitWholeAdeshas().single { it.id == carrierId }
    val targetProof = requireNotNull(carrier.adeshaTarget)
    val target = terms.single { it.id == targetProof.termId }
    require(target.varnas == targetProof.originalVarnas) {
        "Stale ādeśa target ${target.id}; refusing to guess a replacement range."
    }
    val prescribingSutra = requireNotNull(carrier.createdBySutra)
    val substituted = replaceTermVarnaRange(target.id, 0, target.varnas.size, carrier.varnas,
        annotationSources = emptyMap(), sutra = prescribingSutra)
    val changed = substituted.terms.single { it.id == target.id }
    val origin = AdeshaOrigin(carrier.id, carrier.upadesha, prescribingSutra, targetingSutra,
        sourceVarnaIndex = 0, itMarkerProvenance = carrier.itMarkerProvenance.toSet())
    val recorded = substituted.replaceTerm(target.id, changed.copy(
        adeshaOrigins = changed.adeshaOrigins + origin,
        establishedBySutras = changed.establishedBySutras + prescribingSutra,
    ))
    val archived = recorded.removeTerm(carrier.id, prescribingSutra)
    return archived.copy(droppedTerms = archived.droppedTerms.map {
        if (it.id == carrier.id) it.copy(mergedIntoTermId = target.id) else it
    })
}
