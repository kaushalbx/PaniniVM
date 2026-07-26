package dev.panini.sankhya

/**
 * Reverse evaluator: reconstructs a [SankhyaExpression] from a sequence of primitive numeral stems or terms.
 * This is the exact inverse of [SankhyaDerivationFactory].
 */
class SankhyaEvaluator {

    /**
     * Evaluates a sequence of primitive stem strings (e.g. ["द्वि", "विंशति"] or ["द्वि", "शत"]) into a [SankhyaExpression].
     */
    fun evaluateStems(stems: List<String>): SankhyaExpression {
        require(stems.isNotEmpty()) { "Cannot evaluate an empty stem list" }

        // Split by "अधिक" if present as an internal marker
        val adhikaIndex = stems.indexOf("अधिक")
        if (adhikaIndex > 0 && adhikaIndex < stems.size - 1) {
            val rem = evaluateStems(stems.subList(0, adhikaIndex))
            val base = evaluateStems(stems.subList(adhikaIndex + 1, stems.size))
            return SankhyaExpression.Adhika(remainder = rem, base = base)
        }

        // Split by "ऊन" or "न्यून" if present as an internal marker
        val unaIndex = stems.indexOfFirst { it == "ऊन" || it == "न्यून" }
        if (unaIndex > 0 && unaIndex < stems.size - 1) {
            val sub = evaluateStems(stems.subList(0, unaIndex))
            val base = evaluateStems(stems.subList(unaIndex + 1, stems.size))
            return SankhyaExpression.Una(subtrahend = sub, base = base)
        }

        if (stems.size == 1) {
            val prim = PrimitiveSankhya.fromAnnotatedPratipadika(stems.single())
                ?: error("Unrecognized primitive numeral stem: '${stems.single()}'")
            return SankhyaExpression.Primitive(prim)
        }

        if (stems.size == 2) {
            val first = PrimitiveSankhya.fromAnnotatedPratipadika(stems[0])
                ?: error("Unrecognized primitive numeral stem: '${stems[0]}'")
            val second = PrimitiveSankhya.fromAnnotatedPratipadika(stems[1])
                ?: error("Unrecognized primitive numeral stem: '${stems[1]}'")

            val firstExpr = SankhyaExpression.Primitive(first)
            val secondExpr = SankhyaExpression.Primitive(second)

            return when {
                // Multiplicative: coefficient * magnitude (e.g. द्वि * शत = 200)
                isMagnitude(second) -> SankhyaExpression.Multiply(coefficient = firstExpr, magnitude = secondExpr)
                // Additive: unit + ten (e.g. द्वि + विंशति = 22)
                isTen(second) && isUnit(first) -> SankhyaExpression.Add(lower = firstExpr, higher = secondExpr)
                else -> error("Invalid 2-stem numeral sequence: [${stems[0]}, ${stems[1]}]")
            }
        }

        // For longer sequences, process recursively
        val lastStem = stems.last()
        val lastPrim = PrimitiveSankhya.fromAnnotatedPratipadika(lastStem)
        if (lastPrim != null && isMagnitude(lastPrim)) {
            val coeffExpr = evaluateStems(stems.dropLast(1))
            return SankhyaExpression.Multiply(coefficient = coeffExpr, magnitude = SankhyaExpression.Primitive(lastPrim))
        }

        error("Complex stem sequence evaluation failed for: $stems")
    }

    /**
     * Evaluates a sentence-level Adhika expression given a remainder expression and a base expression.
     */
    fun evaluateAdhika(remainder: SankhyaExpression, base: SankhyaExpression): SankhyaExpression {
        return SankhyaExpression.Adhika(remainder = remainder, base = base)
    }

    /**
     * Evaluates a sentence-level Una expression given a subtrahend expression and a base expression.
     */
    fun evaluateUna(subtrahend: SankhyaExpression, base: SankhyaExpression): SankhyaExpression {
        return SankhyaExpression.Una(subtrahend = subtrahend, base = base)
    }

    private fun isUnit(prim: PrimitiveSankhya): Boolean = prim.value in 1L..9L

    private fun isTen(prim: PrimitiveSankhya): Boolean = prim.value in listOf(10L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L)

    private fun isMagnitude(prim: PrimitiveSankhya): Boolean = prim.value in listOf(100L, 1_000L, 10_000L, 100_000L, 1_000_000L, 10_000_000L)
}
