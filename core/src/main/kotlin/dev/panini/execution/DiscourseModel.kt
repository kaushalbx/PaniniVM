package dev.panini.execution

import dev.panini.shiksha.Samjna

/** Utterance polarity (affirmative vs negative). */
enum class Polarity { POSITIVE, NEGATIVE }

/** Intended pragmatic disposition of an utterance. */
enum class ExecutionDisposition {
    EXECUTE, REQUEST_EXECUTION, QUERY, DECLARE, CONSTRAIN, DESIRE, GRANT, OFFER,
}

/** Pragmatic sentence intention (Vākyaprayojana). */
enum class VakyaPrayojana {
    VIDHANA, PRASHNA, AJNA, PRARTHANA, NISHEDHA, ASHAMSA, ANUMATI, NIMANTRANA,
}

/** A stable, turn-qualified result retained in discourse memory. */
data class SmrtaPhala(
    val id: String,
    val turnNumber: Int,
    val invocationId: String,
    val value: String,
    val samjnas: Set<Samjna>,
    val typedValue: SanskritValue? = null,
)
