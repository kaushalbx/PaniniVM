package dev.panini.sankhya

import dev.panini.derivation.DerivationResult

/** Public numeral API. Every method returns an auditable grammatical derivation. */
class SankhyaGenerator(
    private val cardinalDeriver: CardinalSankhyaDeriver = CardinalSankhyaDeriver(),
    private val puranaDeriver: PuranaSankhyaDeriver = PuranaSankhyaDeriver(cardinalDeriver),
) {
    /** Numeric identity of a canonical annotated prātipadika; surface forms are not accepted. */
    fun annotatedPratipadikaValue(pratipadika: String): Long? =
        PrimitiveSankhya.fromAnnotatedPratipadika(pratipadika)?.value

    fun cardinal(value: Long): DerivationResult = cardinalDeriver.derive(value)

    fun cardinalVariants(value: Long): List<DerivationResult> = cardinalDeriver.deriveVariants(value)

    fun ordinal(value: Long): DerivationResult = puranaDeriver.derive(value)

    fun ordinalVariants(value: Long): List<DerivationResult> = puranaDeriver.deriveVariants(value)
}
