package dev.panini.execution

import dev.panini.sankhya.CardinalSankhyaDeriver
import dev.panini.sankhya.SankhyaExpressionBuilder
import dev.panini.sankhya.SankhyaInflectionClass
import dev.panini.sankhya.headPrimitive
import java.math.BigInteger

/** Renders the conventional counting form required by VM result values; this is not a full sup paradigm. */
internal class SankhyaCountingFormRenderer(
    private val cardinalDeriver: CardinalSankhyaDeriver = CardinalSankhyaDeriver(),
    private val expressionBuilder: SankhyaExpressionBuilder = SankhyaExpressionBuilder(),
) {
    fun render(value: BigInteger): String {
        val expression = expressionBuilder.build(value)
        val stem = cardinalDeriver.derive(value).final.surface
        return when (expression.headPrimitive().inflectionClass) {
            SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN -> when {
                stem.endsWith("न्") -> stem.dropLast(2)
                stem == "षष्" -> "षट्"
                else -> stem
            }
            SankhyaInflectionClass.FEMININE_I -> "$stemः"
            SankhyaInflectionClass.FEMININE_T -> stem
            SankhyaInflectionClass.NEUTER_A -> "${stem}म्"
            SankhyaInflectionClass.SPECIAL -> when (value) {
                BigInteger.ZERO -> "शून्यम्"
                BigInteger.ONE -> "एकम्"
                BigInteger.TWO -> "द्वे"
                BigInteger.valueOf(3) -> "त्रीणि"
                BigInteger.valueOf(4) -> "चत्वारि"
                else -> error("No VM counting form for $value")
            }
        }
    }
}
