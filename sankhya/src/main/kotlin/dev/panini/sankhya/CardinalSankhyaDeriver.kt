package dev.panini.sankhya

import dev.panini.derivation.DerivationResult

/** Builds and derives a cardinal numeral prātipadika from its numeric value. */
class CardinalSankhyaDeriver(
    private val expressionBuilder: SankhyaExpressionBuilder = SankhyaExpressionBuilder(),
    private val derivationFactory: SankhyaDerivationFactory = SankhyaDerivationFactory(),
    private val derivationEngine: SankhyaDerivationEngine = SankhyaDerivationEngine(),
) {
    fun derive(value: Long): DerivationResult {
        require(value >= 0L) { "Negative numbers are not supported: $value" }
        return derivationEngine.derive(derivationFactory.create(expressionBuilder.build(value)))
    }

    fun deriveVariants(value: Long): List<DerivationResult> {
        require(value >= 0L) { "Negative numbers are not supported: $value" }
        return derivationEngine.deriveAll(derivationFactory.create(expressionBuilder.build(value)))
    }
}
