package dev.panini.sankhya

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationResult
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
import dev.panini.derivation.term
import dev.panini.shiksha.Samjna

/** Public numeral API. Every method returns an auditable grammatical derivation. */
class SankhyaGenerator(
    private val cardinalDeriver: CardinalSankhyaDeriver = CardinalSankhyaDeriver(),
    private val puranaDeriver: PuranaSankhyaDeriver = PuranaSankhyaDeriver(cardinalDeriver),
    private val expressionBuilder: SankhyaExpressionBuilder = SankhyaExpressionBuilder(),
    private val derivationFactory: SankhyaDerivationFactory = SankhyaDerivationFactory(),
    private val derivationEngine: SankhyaDerivationEngine = SankhyaDerivationEngine(),
) {

    fun cardinal(value: Long): DerivationResult = cardinalDeriver.derive(value)

    fun cardinalVariants(value: Long): List<DerivationResult> = cardinalDeriver.deriveVariants(value)

    /** Gender of the grammatical head of the canonical cardinal expression. */
    fun intrinsicLinga(value: Long): Linga? =
        intrinsicLinga(expressionBuilder.build(value))

    fun intrinsicLinga(expression: SankhyaExpression): Linga? =
        expression.headPrimitive().inflectionClass.intrinsicLinga

    /** Returns the cardinal surface requested by one sup case-and-number slot. */
    fun decline(
        value: Long,
        vibhakti: Vibhakti,
        vacana: Vacana,
        linga: Linga = Linga.NAPUMSAKA,
    ): String = decline(expressionBuilder.build(value), vibhakti, vacana, linga)

    /**
     * The prātipadika of the written construction (एक + ऊन + विंशति → एकोनविंशति, एक + दशन् → एकादशन्),
     * never a different same-value numeral.
     */
    fun pratipadika(expression: SankhyaExpression): String {
        val head = expression.headPrimitive()
        return (expression as? SankhyaExpression.Primitive)?.sankhya?.inflectionPratipadika
            ?: derivationEngine.derive(derivationFactory.create(expression)).final.surface.let { surface ->
                // Numeral-internal composition uses the surface दश, but sup
                // derivation must start from the lexical n-final head दशन्.
                // Retain the supplied expression; an una construction headed
                // by twenty must not be replaced by a same-value teen numeral.
                // The head's own surface may have changed (षोडश: ष्टुत्व of 6.3.109-v1), so only the
                // न् that composition dropped is restored: दश → दशन्, डश → डशन्.
                if (head == PrimitiveSankhya.DASHAN) {
                    val dropped = head.inflectionPratipadika.removePrefix(head.uttarapada)
                    require(dropped.isNotEmpty() && head.inflectionPratipadika.startsWith(head.uttarapada))
                    surface + dropped
                } else surface
            }
    }

    /** Declines the supplied construction without replacing it by a same-value numeral. */
    fun decline(
        expression: SankhyaExpression,
        vibhakti: Vibhakti,
        vacana: Vacana,
        linga: Linga = Linga.NAPUMSAKA,
    ): String {
        val value = expression.value
        val effectiveLinga = expression.headPrimitive().inflectionClass.intrinsicLinga ?: linga
        val base = pratipadika(expression)
        val pratipadika = if (value == 1L && effectiveLinga == Linga.STRI) {
            dev.panini.derivation.StriPratyayaEngine().derive(
                dev.panini.derivation.StriPratyayaRequest(base, dev.panini.shiksha.Samjna.TAP),
            ).final.surface
        } else base
        return SubantaEngine().derive(
            SubantaDerivationRequest(pratipadika, vibhakti, vacana, effectiveLinga,
                if (value == 1L && effectiveLinga == Linga.STRI) dev.panini.derivation.NominalStemFormation.AP
                else dev.panini.derivation.NominalStemFormation.UNSPECIFIED),
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
        val requested = initial.withSamjnas(setOf(SamjnaAssignment(target.id, samjna)))
        val stem = derivationEngine.deriveStem(requested)
        // An asarvavibhakti taddhitānta is an avyaya (1.1.38): it takes सुँ, which 2.4.82 elides, and by 1.1.62
        // the whole word is then a pada, so the Tripādī completes it (सकृत्, चतुः, द्विः, पञ्चकृत्वः, षड्धा).
        if (stem.final.samjnas.none { it.samjna == Samjna.AVYAYA }) return derivationEngine.derive(requested)
        // With a sup appended the word is no longer complete, so the stem pass's FINAL stage does not carry over.
        val withSup = stem.final.copy(terms = stem.final.terms + dev.panini.core.SupAffix.SU.term(),
            stage = dev.panini.derivation.DerivationStage.PADA_FORMED)
        val word = derivationEngine.deriveWord(withSup)
        return word.copy(initial = requested, applications = stem.applications + word.applications,
            events = stem.events + word.events)
    }
}
