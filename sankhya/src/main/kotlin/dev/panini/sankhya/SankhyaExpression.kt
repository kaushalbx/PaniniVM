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
}
