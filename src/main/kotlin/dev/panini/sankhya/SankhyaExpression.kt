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

    data class Subtract(
        val subtrahend: SankhyaExpression,
        val minuend: SankhyaExpression
    ) : SankhyaExpression {
        override val value: BigInteger = minuend.value - subtrahend.value
    }

    data class Multiply(
        val coefficient: SankhyaExpression,
        val magnitude: SankhyaExpression
    ) : SankhyaExpression {
        override val value: BigInteger = coefficient.value * magnitude.value
    }
}
