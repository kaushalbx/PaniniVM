package dev.panini.sankhya

import dev.panini.derivation.DerivationResult
import java.math.BigInteger

/** Public numeral API. Every method returns an auditable grammatical derivation. */
class SankhyaGenerator(
    private val cardinalDeriver: CardinalSankhyaDeriver = CardinalSankhyaDeriver(),
    private val puranaDeriver: PuranaSankhyaDeriver = PuranaSankhyaDeriver(cardinalDeriver),
) {
    fun cardinal(value: BigInteger): DerivationResult = cardinalDeriver.derive(value)

    fun cardinalVariants(value: BigInteger): List<DerivationResult> = cardinalDeriver.deriveVariants(value)

    fun ordinal(value: BigInteger): DerivationResult = puranaDeriver.derive(value)

    fun ordinalVariants(value: BigInteger): List<DerivationResult> = puranaDeriver.deriveVariants(value)
}
