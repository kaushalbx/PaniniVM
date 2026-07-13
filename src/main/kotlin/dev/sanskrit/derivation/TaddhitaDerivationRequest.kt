package dev.sanskrit.derivation

/** Typed entry point for a taddhita derivation. */
data class TaddhitaDerivationRequest(
    val pratipadika: String,
    val meaning: DerivationalMeaning,
    val environments: Set<DerivationalEnvironment> = emptySet(),
) {
    init { require(pratipadika.isNotBlank()) { "A prātipadika is required." } }

    fun initialState(): DerivationState = DerivationState(
        terms = listOf(DerivationTerm("pratipadika", pratipadika, TermKind.PRATIPADIKA)),
        context = DerivationalContext(requestedMeaning = meaning, environments = environments),
    )
}
