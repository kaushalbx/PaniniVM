package dev.panini.sankhya

import dev.panini.execution.SankhyaResultRenderer

/** Renders the conventional counting form required by VM result values; this is not a full sup paradigm. */
class SankhyaCountingFormRenderer(
    private val cardinalDeriver: CardinalSankhyaDeriver = CardinalSankhyaDeriver(),
    private val expressionBuilder: SankhyaExpressionBuilder = SankhyaExpressionBuilder(),
) : SankhyaResultRenderer {

    companion object {
        @JvmStatic
        fun init() {
            if (SankhyaResultRenderer.defaultRenderer !is SankhyaCountingFormRenderer) {
                SankhyaCountingFormRenderer()
            }
        }
    }

    init {
        SankhyaResultRenderer.defaultRenderer = this
    }

    override fun render(value: Long): String {
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
                0L -> "शून्यम्"
                1L -> "एकम्"
                2L -> "द्वे"
                3L -> "त्रीणि"
                4L -> "चत्वारि"
                else -> error("No VM counting form for $value")
            }
        }
    }
}
