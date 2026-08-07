package dev.panini.execution

import dev.panini.shiksha.Samjna

/** Strongly-typed value hierarchy for the Pāṇinian execution runtime. */
sealed interface SanskritValue {
    val samjnas: Set<Samjna>
    fun toDisplayText(): String

    data class Sankhya(
        val value: Long,
        val word: String,
    ) : SanskritValue {
        override val samjnas: Set<Samjna> = setOf(Samjna.SANKHYA, Samjna.SHABDA)
        override fun toDisplayText(): String = word
    }

    data class Rational(
        val numerator: Long,
        val denominator: Long,
        val word: String,
    ) : SanskritValue {
        override val samjnas: Set<Samjna> = setOf(Samjna.SANKHYA, Samjna.SHABDA)
        override fun toDisplayText(): String = "$numerator/$denominator ($word)"
    }

    data class Shabda(
        val text: String,
        override val samjnas: Set<Samjna> = setOf(Samjna.SHABDA),
    ) : SanskritValue {
        override fun toDisplayText(): String = text
    }

    data class Gana(
        val elements: List<SanskritValue>,
    ) : SanskritValue {
        override val samjnas: Set<Samjna> = elements.flatMap { it.samjnas }.toSet() + Samjna.GANA
        override fun toDisplayText(): String = elements.joinToString(" ") { it.toDisplayText() }
    }

    data class Suchi(
        val items: List<SanskritValue>,
    ) : SanskritValue {
        override val samjnas: Set<Samjna> = items.flatMap { it.samjnas }.toSet() + Samjna.GANA
        override fun toDisplayText(): String = "[${items.joinToString(", ") { it.toDisplayText() }}]"
    }

    data class Satya(
        val boolean: Boolean,
    ) : SanskritValue {
        override val samjnas: Set<Samjna> = setOf(Samjna.SATYA, Samjna.SHABDA)
        override fun toDisplayText(): String = if (boolean) "सत्यम्" else "असत्यम्"
    }

    data object Lopa : SanskritValue {
        override val samjnas: Set<Samjna> = setOf(Samjna.LOPA, Samjna.SHABDA)
        override fun toDisplayText(): String = "लोपः"
    }

    companion object {
        fun of(text: String, samjnas: Set<Samjna> = emptySet()): SanskritValue {
            return if (text == "सत्यम्" || text == "असत्यम्") {
                Satya(text == "सत्यम्")
            } else {
                Shabda(text, if (samjnas.isEmpty()) setOf(Samjna.SHABDA) else samjnas)
            }
        }
    }
}
