package dev.panini.unadipatha.analysis

import dev.panini.dhatupatha.Dhatu
import dev.panini.unadipatha.UnadiMatch
import dev.panini.unadipatha.UnadiPatha
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna

/**
 * Derivational & Semantic classification of a nominal stem.
 */
enum class StemClassification {
    RUDHI_PRATIPADIKA,      // Fixed conventional name (Avyutpatti-pakṣa)
    YAUGIKA_PRATIPADIKA,    // Compositional Kṛt derivative (Vyutpatti-pakṣa)
    UNKNOWN
}

/**
 * Result of analyzing a nominal stem or word through the Uṇādisūtra system.
 */
data class UnadiStemAnalysis(
    val stem: String,
    val isRudhi: Boolean,
    val matches: List<UnadiMatch>,
    val primaryMatch: UnadiMatch? = matches.firstOrNull(),
    val etymologicalRoot: Dhatu? = primaryMatch?.dhatu,
    val pratyaya: String? = primaryMatch?.pratyaya,
    val sutraNumber: String? = primaryMatch?.sutraNumber,
    val classification: StemClassification = when {
        isRudhi -> StemClassification.RUDHI_PRATIPADIKA
        matches.isNotEmpty() -> StemClassification.YAUGIKA_PRATIPADIKA
        else -> StemClassification.UNKNOWN
    }
)

/**
 * Post-parsing analyzer using Uṇādisūtra catalog for etymological & semantic analysis.
 */
object UnadiAnalyzer {

    /**
     * Analyzes a nominal stem (e.g. "वायु", "कर्ण", "पुमः") by checking the Uṇādisūtra catalog.
     */
    fun analyzeStem(stem: String): UnadiStemAnalysis {
        val matches = UnadiPatha.findByWord(stem)
        val isRudhi = matches.any { match ->
            match.samjnas.any { s -> s is Samjna.Rudhi && s.word == stem } ||
                    (match.meaning is Artha.Rudhi && (match.meaning as Artha.Rudhi).devanagari == stem)
        }
        return UnadiStemAnalysis(
            stem = stem,
            isRudhi = isRudhi,
            matches = matches
        )
    }

    /**
     * Analyzes a root and suffix pair to determine if it yields a Rūḍhi Saṁjñā or general Kṛt derivative.
     */
    fun analyzePair(dhatu: Dhatu, pratyaya: String): UnadiStemAnalysis? {
        val matches = UnadiPatha.findSamjna(dhatu, pratyaya)
        if (matches.isEmpty()) return null

        val rudhiMatch = matches.firstOrNull { match -> match.samjnas.any { s -> s is Samjna.Rudhi } }
        val primary = rudhiMatch ?: matches.first()
        val rudhiWord = (primary.samjnas.firstOrNull { it is Samjna.Rudhi } as? Samjna.Rudhi)?.word ?: ""

        return UnadiStemAnalysis(
            stem = rudhiWord,
            isRudhi = rudhiMatch != null,
            matches = matches
        )
    }
}
