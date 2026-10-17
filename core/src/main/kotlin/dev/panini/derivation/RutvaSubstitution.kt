package dev.panini.derivation

import dev.panini.shiksha.*

/** Introduces fresh रुँ at one final occurrence, not a fresh upadeśa for the
 * entire carrier word. Retains existing annotations, identity and it history. */
fun DerivationState.substituteFinalVarnaWithRutva(id: String, sutra: String): DerivationState {
    val term = terms.single { it.id == id }
    require(term.itProcessingPhase == ItProcessingPhase.PROCESSED &&
        term.itDesignations.isEmpty() && term.deferredItDesignations.isEmpty()) {
        "$sutra requires completed it processing on $id: ${term.itProcessingPhase}, " +
            "${term.itDesignations}, ${term.deferredItDesignations}"
    }
    val tokens = term.phonologicalText.effectiveVarnas
    val index = tokens.lastIndex
    require(index >= 0)
    val replacement = listOf(
        VarnaToken(VarnaTokenId("rutva:$id:$sutra:r"), Vyanjana.RA),
        VarnaToken(VarnaTokenId("rutva:$id:$sutra:u"), Svara.U, nasalized = true),
    )
    val signs = (term.orthographicSigns + term.phonologicalText.sourceOrthographicSigns).distinct()
        .filterNot { it.sign == OrthographicSign.CHANDRABINDU && it.afterVarnaCount == index + 1 }
        .map { if (it.afterVarnaCount > index) it.copy(afterVarnaCount = it.afterVarnaCount + 1) else it }
    val rendered = SanskritText(tokens.take(index) + replacement).renderWithOrthographicSigns(signs)
    val changed = substituteTermSurface(id, rendered, tokens[index].varna.devanagari.single(),
        SanskritText(replacement).render(), sutra, sourceVarnaIndex = index)
    // A one-sound affix is wholly replaced; a carrier word is not. Keep the
    // original sup/tiṅ slot and sthānin in either case.
    val wholeAffix = term.kind == TermKind.PRATYAYA && index == 0
    return changed.replaceTerm(id, changed.terms.single { it.id == id }.copy(
        orthographicSigns = signs,
        itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
        pendingRutvaUpadeshaIndex = index,
        rutvaOccurrences = term.rutvaOccurrences.filterNot { it.varnaIndex == index } + RutvaOccurrence(index, sutra),
        sthaniProps = term.sthaniProps ?: SthaniProperties(term.upadesha, term.itMarkers),
        upadesha = if (wholeAffix) SanskritText(replacement).render() else term.upadesha,
        createdBySutra = if (wholeAffix) sutra else term.createdBySutra,
        establishedBySutras = term.establishedBySutras + sutra,
    ))
}
