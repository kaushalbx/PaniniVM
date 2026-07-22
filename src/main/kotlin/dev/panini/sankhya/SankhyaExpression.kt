package dev.panini.sankhya

import java.math.BigInteger

sealed interface SankhyaExpression {

    val value: BigInteger

    data class Primitive(
        val sankhya: PrimitiveSankhya
    ) : SankhyaExpression {
        override val value: BigInteger = sankhya.value
    }

    data class Add(
        val lower: SankhyaExpression,
        val higher: SankhyaExpression
    ) : SankhyaExpression {
        override val value: BigInteger = lower.value + higher.value
    }

    /** A remainder stated as being अधिक than a completed higher magnitude. */
    data class Adhika(
        val remainder: SankhyaExpression,
        val base: SankhyaExpression,
    ) : SankhyaExpression {
        init {
            require(remainder.value > BigInteger.ZERO) { "Adhika requires a positive remainder" }
            require(remainder.value < base.value) { "Adhika remainder must be below its base" }
        }
        override val value: BigInteger = remainder.value + base.value
    }

    data class Multiply(
        val coefficient: SankhyaExpression,
        val magnitude: SankhyaExpression
    ) : SankhyaExpression {
        override val value: BigInteger = coefficient.value * magnitude.value
    }
}

fun SankhyaExpression.headPrimitive(): PrimitiveSankhya = when (this) {
    is SankhyaExpression.Primitive -> sankhya
    is SankhyaExpression.Add -> higher.headPrimitive()
    is SankhyaExpression.Adhika -> base.headPrimitive()
    is SankhyaExpression.Multiply -> magnitude.headPrimitive()
}
