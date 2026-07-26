package dev.panini.sankhya

class SankhyaExpressionBuilder {

    fun build(value: Long): SankhyaExpression {
        require(value >= 0L) { "Negative numbers are not supported: $value" }

        val direct = PrimitiveSankhya.fromValue(value)
        if (direct != null) {
            return SankhyaExpression.Primitive(direct)
        }

        if (value < 20L) {
            val units = value - 10L
            val unitPrim = PrimitiveSankhya.fromValue(units)
                ?: error("Invalid unit value: $units")
            return SankhyaExpression.Add(
                lower = SankhyaExpression.Primitive(unitPrim),
                higher = SankhyaExpression.Primitive(PrimitiveSankhya.DASHAN)
            )
        }

        if (value < 100L) {
            val tensVal = (value / 10L) * 10L
            val unitsVal = value % 10L
            val tensPrim = PrimitiveSankhya.fromValue(tensVal)
                ?: error("Invalid tens value: $tensVal")

            if (unitsVal == 0L) {
                return SankhyaExpression.Primitive(tensPrim)
            }

            val unitsPrim = PrimitiveSankhya.fromValue(unitsVal)
                ?: error("Invalid units value: $unitsVal")

            return SankhyaExpression.Add(
                lower = SankhyaExpression.Primitive(unitsPrim),
                higher = SankhyaExpression.Primitive(tensPrim)
            )
        }

        val magnitudes = listOf(
            PrimitiveSankhya.KOTI,
            PrimitiveSankhya.PRAYUTA,
            PrimitiveSankhya.LAKSHA,
            PrimitiveSankhya.AYUTA,
            PrimitiveSankhya.SAHASRA,
            PrimitiveSankhya.SHATA
        )

        for (mag in magnitudes) {
            if (value >= mag.value) {
                val coeffVal = value / mag.value
                val remVal = value % mag.value

                val coeffExpr = build(coeffVal)
                val magExpr = SankhyaExpression.Primitive(mag)

                val multExpr = if (coeffVal == 1L) {
                    magExpr
                } else {
                    SankhyaExpression.Multiply(coeffExpr, magExpr)
                }

                return if (remVal == 0L) {
                    multExpr
                } else {
                    val remExpr = build(remVal)
                    SankhyaExpression.Adhika(remainder = remExpr, base = multExpr)
                }
            }
        }

        error("Unhandled value: $value")
    }
}
