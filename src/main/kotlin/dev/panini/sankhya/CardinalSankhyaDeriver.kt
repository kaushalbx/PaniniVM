package dev.panini.sankhya

import dev.panini.derivation.DerivationResult
import java.math.BigInteger

/** Builds and derives a cardinal numeral prātipadika from its numeric value. */
class CardinalSankhyaDeriver(
    private val expressionBuilder: SankhyaExpressionBuilder = SankhyaExpressionBuilder(),
    private val derivationFactory: SankhyaDerivationFactory = SankhyaDerivationFactory(),
    private val derivationEngine: SankhyaDerivationEngine = SankhyaDerivationEngine(),
) {
    fun derive(value: BigInteger): DerivationResult {
        require(value.signum() >= 0) { "Negative numbers are not supported: $value" }
        return derivationEngine.derive(derivationFactory.create(expressionBuilder.build(value)))
    }

    fun deriveVariants(value: BigInteger): List<DerivationResult> {
        require(value.signum() >= 0) { "Negative numbers are not supported: $value" }
        return derivationEngine.deriveAll(derivationFactory.create(expressionBuilder.build(value)))
    }
}
