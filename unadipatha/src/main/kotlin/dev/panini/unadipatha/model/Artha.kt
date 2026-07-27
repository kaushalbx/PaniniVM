package dev.panini.unadipatha.model

/**
 * Pāṇinian Semantic Representation (Artha / Meaning).
 */
sealed interface Artha {
    /** Kāraka Meanings (3.4.68 "कर्तरि कृत्", 3.3.18 "भावे") */
    enum class Karaka : Artha {
        KARTA, KARMA, KARANA, SAMPRADANA, APADANA, ADHIKARANA, BHAVA
    }

    /** Contextual / Dispositional Conditions (उपाधि / स्वभाव) */
    enum class Context : Artha {
        TAATSIILYA, TADDHARMA, TATSADHUKARI, SHILPA, AASHIS
    }

    /** Lexical Conventional Meaning (Rūḍhi-Artha) */
    data class Rudhi(
        val devanagari: String,
        val english: String? = null
    ) : Artha

    /** Textual Explanation / Commentary */
    data class Explanation(
        val hindi: String,
        val english: String? = null
    ) : Artha
}
