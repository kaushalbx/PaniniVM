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
}

fun SankhyaExpression.headPrimitive(): PrimitiveSankhya = when (this) {
    is SankhyaExpression.Primitive -> sankhya
    is SankhyaExpression.Add -> higher.headPrimitive()
    is SankhyaExpression.Adhika -> base.headPrimitive()
    is SankhyaExpression.Multiply -> magnitude.headPrimitive()
}
