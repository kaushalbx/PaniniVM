package dev.panini.execution

/** Discourse turn coupling a system response with conversation context. */
data class SambhashanaTurn(
    val response: Prativacana,
    val context: SambhashanaContext,
)

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

/** A language-facing response to an interpreted utterance. */
data class Prativacana(
    val text: String,
    val phala: Phala,
)

/** A stable, turn-qualified result retained in discourse memory. */
data class SmrtaPhala(
    val id: String,
    val turnNumber: Int,
    val invocationId: String,
    val value: String,
    val samjnas: Set<ExecutionSamjna>,
    val typedValue: SanskritValue? = null,
)
