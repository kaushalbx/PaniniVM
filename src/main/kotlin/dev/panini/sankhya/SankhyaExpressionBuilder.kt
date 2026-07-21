package dev.panini.sankhya

import java.math.BigInteger

class SankhyaExpressionBuilder {

    fun build(value: BigInteger): SankhyaExpression {
        require(value.signum() >= 0) { "Negative numbers are not supported: $value" }

        val direct = PrimitiveSankhya.fromValue(value)
        if (direct != null) {
            return SankhyaExpression.Primitive(direct)
        }

        if (value < BigInteger.valueOf(20)) {
            val units = value.subtract(BigInteger.TEN)
            val unitPrim = PrimitiveSankhya.fromValue(units)
                ?: error("Invalid unit value: $units")
            return SankhyaExpression.Add(
                lower = SankhyaExpression.Primitive(unitPrim),
                higher = SankhyaExpression.Primitive(PrimitiveSankhya.DASHAN)
            )
        }

        if (value < BigInteger.valueOf(100)) {
            val tensVal = value.divide(BigInteger.TEN).multiply(BigInteger.TEN)
            val unitsVal = value.mod(BigInteger.TEN)
            val tensPrim = PrimitiveSankhya.fromValue(tensVal)
                ?: error("Invalid tens value: $tensVal")
            
            if (unitsVal == BigInteger.ZERO) {
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
                val coeffVal = value.divide(mag.value)
                val remVal = value.mod(mag.value)

                val coeffExpr = build(coeffVal)
                val magExpr = SankhyaExpression.Primitive(mag)

                val multExpr = SankhyaExpression.Multiply(coeffExpr, magExpr)

                return if (remVal == BigInteger.ZERO) {
                    multExpr
                } else {
                    val remExpr = build(remVal)
                    SankhyaExpression.Add(lower = remExpr, higher = multExpr)
                }
            }
        }

        error("Unhandled value: $value")
    }
}
