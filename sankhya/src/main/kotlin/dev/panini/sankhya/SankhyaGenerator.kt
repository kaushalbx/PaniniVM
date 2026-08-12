package dev.panini.sankhya

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationResult
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
import dev.panini.shiksha.Samjna

/** Public numeral API. Every method returns an auditable grammatical derivation. */
class SankhyaGenerator(
    private val cardinalDeriver: CardinalSankhyaDeriver = CardinalSankhyaDeriver(),
    private val puranaDeriver: PuranaSankhyaDeriver = PuranaSankhyaDeriver(cardinalDeriver),
    private val expressionBuilder: SankhyaExpressionBuilder = SankhyaExpressionBuilder(),
    private val derivationFactory: SankhyaDerivationFactory = SankhyaDerivationFactory(),
    private val derivationEngine: SankhyaDerivationEngine = SankhyaDerivationEngine(),
) {
    /** Numeric identity of a canonical annotated prātipadika; surface forms are not accepted. */
    fun annotatedPratipadikaValue(pratipadika: String): Long? =
        PrimitiveSankhya.fromAnnotatedPratipadika(pratipadika)?.value

    fun cardinal(value: Long): DerivationResult = cardinalDeriver.derive(value)

    fun cardinalVariants(value: Long): List<DerivationResult> = cardinalDeriver.deriveVariants(value)

    /** Returns the cardinal surface requested by one sup case-and-number slot. */
    fun decline(
        value: Long,
        vibhakti: Vibhakti,
        vacana: Vacana,
        linga: Linga = Linga.NAPUMSAKA,
    ): String {
        val pratipadika = PrimitiveSankhya.fromValue(value)?.pratipadika
            ?: cardinal(value).final.surface
        return SubantaEngine().derive(
            SubantaDerivationRequest(pratipadika, vibhakti, vacana, linga),
        ).final.surface
    }

    fun ordinal(value: Long): DerivationResult = puranaDeriver.derive(value)

    fun ordinalVariants(value: Long): List<DerivationResult> = puranaDeriver.deriveVariants(value)

    fun frequency(value: Long, useSuc: Boolean = false): DerivationResult {
        val samjna = if (useSuc && value in 2L..4L) Samjna.SUC else Samjna.KRTVASUC
        return deriveTaddhita(value, samjna)
    }

    fun distribution(value: Long): DerivationResult = deriveTaddhita(value, Samjna.DHA)

    private fun deriveTaddhita(value: Long, samjna: Samjna): DerivationResult {
        require(value >= 0L) { "Negative numbers are not supported: $value" }
        val initial = derivationFactory.createTaddhitaBase(expressionBuilder.build(value))
        val target = initial.terms.firstOrNull() ?: error("Numeral derivation requires a term")
        return derivationEngine.derive(initial.withSamjnas(setOf(SamjnaAssignment(target.id, samjna))))
    }
}
