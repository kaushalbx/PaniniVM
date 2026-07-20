package dev.panini.execution

/** Strongly-typed value hierarchy for the Pāṇinian execution runtime. */
sealed interface SanskritValue {
    val samjnas: Set<ExecutionSamjna>
    fun toDisplayText(): String

    data class Sankhya(
        val value: Long,
        val word: String,
    ) : SanskritValue {
        override val samjnas: Set<ExecutionSamjna> = setOf(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA)
        override fun toDisplayText(): String = word
    }

    data class Shabda(
        val text: String,
        override val samjnas: Set<ExecutionSamjna> = setOf(ExecutionSamjna.SHABDA),
    ) : SanskritValue {
        override fun toDisplayText(): String = text
    }

    data class Gana(
        val elements: List<SanskritValue>,
    ) : SanskritValue {
        override val samjnas: Set<ExecutionSamjna> = elements.flatMap { it.samjnas }.toSet() + ExecutionSamjna.GANA
        override fun toDisplayText(): String = elements.joinToString(" ") { it.toDisplayText() }
    }

    data class Satya(
        val boolean: Boolean,
    ) : SanskritValue {
        override val samjnas: Set<ExecutionSamjna> = setOf(ExecutionSamjna.SATYA, ExecutionSamjna.SHABDA)
        override fun toDisplayText(): String = if (boolean) "सत्यम्" else "असत्यम्"
    }

    companion object {
        fun of(text: String, samjnas: Set<ExecutionSamjna> = emptySet()): SanskritValue {
            val num = SanskritNumbers.valueOf(text)
            return if (num != null && (samjnas.isEmpty() || ExecutionSamjna.SANKHYA in samjnas)) {
                Sankhya(num.toLong(), text)
            } else if (text == "सत्यम्" || text == "असत्यम्") {
                Satya(text == "सत्यम्")
            } else {
                Shabda(text, if (samjnas.isEmpty()) setOf(ExecutionSamjna.SHABDA) else samjnas)
            }
        }
    }
}
