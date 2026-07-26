package dev.panini.sankhya

sealed interface SankhyaExpression {

    val value: Long

    data class Primitive(
        val sankhya: PrimitiveSankhya
    ) : SankhyaExpression {
        override val value: Long = sankhya.value
    }

    data class Add(
        val lower: SankhyaExpression,
        val higher: SankhyaExpression
    ) : SankhyaExpression {
        override val value: Long = lower.value + higher.value
    }

    /** A remainder stated as being अधिक than a completed higher magnitude. */
    data class Adhika(
        val remainder: SankhyaExpression,
        val base: SankhyaExpression,
    ) : SankhyaExpression {
        init {
            require(remainder.value > 0L) { "Adhika requires a positive remainder" }
            require(remainder.value < base.value) { "Adhika remainder must be below its base" }
        }
        override val value: Long = remainder.value + base.value
    }

    data class Multiply(
        val coefficient: SankhyaExpression,
        val magnitude: SankhyaExpression
    ) : SankhyaExpression {
        override val value: Long = coefficient.value * magnitude.value
    }

    /** A subtrahend stated as being ऊन (defective/less) than a higher base magnitude. */
    data class Una(
        val subtrahend: SankhyaExpression,
        val base: SankhyaExpression,
    ) : SankhyaExpression {
        init {
            require(subtrahend.value > 0L) { "Una requires a positive subtrahend" }
            require(subtrahend.value < base.value) { "Una subtrahend must be below its base" }
        }
        override val value: Long = base.value - subtrahend.value
    }
    /** An ordinal numeral expression (पूरीयसङ्ख्या), e.g. 5th, 20th. */
    data class Purana(
        val base: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = base.value
    }

    /** A frequency / repetition numeral expression (अभ्याससङ्ख्या / कृत्वसुच्), e.g. 5 times. */
    data class Frequency(
        val count: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = count.value
    }

    /** A distributive / part numeral expression (प्रकारसङ्ख्या / धा-प्रत्यय), e.g. 5-fold / 5 parts. */
    data class Distribution(
        val parts: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = parts.value
    }

    /** A fractional numeral expression (भिन्नसङ्ख्या), e.g. 1/2, 1/4, 3/4, 2.5 (5/2). */
    data class RationalFraction(
        val numerator: Long,
        val denominator: Long = 1L,
    ) : SankhyaExpression {
        override val value: Long = if (denominator != 0L) numerator / denominator else 0L
    }

    data class Square(
        val operand: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = operand.value * operand.value
    }

    data class Cube(
        val operand: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = operand.value * operand.value * operand.value
    }

    data class SquareRoot(
        val operand: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = kotlin.math.sqrt(operand.value.toDouble()).toLong()
    }

    data class Sin(
        val degrees: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = kotlin.math.sin(degrees.value.toDouble() * Math.PI / 180.0).toLong()
    }

    data class Cos(
        val degrees: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = kotlin.math.cos(degrees.value.toDouble() * Math.PI / 180.0).toLong()
    }

    data class Tan(
        val degrees: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = kotlin.math.tan(degrees.value.toDouble() * Math.PI / 180.0).toLong()
    }

    data class Versin(
        val degrees: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = (1.0 - kotlin.math.cos(degrees.value.toDouble() * Math.PI / 180.0)).toLong()
    }

    data class Hypotenuse(
        val bhuja: SankhyaExpression,
        val koti: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = kotlin.math.sqrt((bhuja.value * bhuja.value + koti.value * koti.value).toDouble()).toLong()
    }

    data class CircleArea(
        val radius: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = (Math.PI * radius.value * radius.value).toLong()
    }

    data class CircleCircumference(
        val radius: SankhyaExpression,
    ) : SankhyaExpression {
        override val value: Long = (2 * Math.PI * radius.value).toLong()
    }
}

fun SankhyaExpression.headPrimitive(): PrimitiveSankhya = when (this) {
    is SankhyaExpression.Primitive -> sankhya
    is SankhyaExpression.Add -> higher.headPrimitive()
    is SankhyaExpression.Adhika -> base.headPrimitive()
    is SankhyaExpression.Una -> base.headPrimitive()
    is SankhyaExpression.Multiply -> magnitude.headPrimitive()
    is SankhyaExpression.Purana -> base.headPrimitive()
    is SankhyaExpression.Frequency -> count.headPrimitive()
    is SankhyaExpression.Distribution -> parts.headPrimitive()
    is SankhyaExpression.RationalFraction -> PrimitiveSankhya.EKA
    is SankhyaExpression.Square -> operand.headPrimitive()
    is SankhyaExpression.Cube -> operand.headPrimitive()
    is SankhyaExpression.SquareRoot -> operand.headPrimitive()
    is SankhyaExpression.Sin -> degrees.headPrimitive()
    is SankhyaExpression.Cos -> degrees.headPrimitive()
    is SankhyaExpression.Tan -> degrees.headPrimitive()
    is SankhyaExpression.Versin -> degrees.headPrimitive()
    is SankhyaExpression.Hypotenuse -> bhuja.headPrimitive()
    is SankhyaExpression.CircleArea -> radius.headPrimitive()
    is SankhyaExpression.CircleCircumference -> radius.headPrimitive()
}
