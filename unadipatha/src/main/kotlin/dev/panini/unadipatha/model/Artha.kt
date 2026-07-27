package dev.panini.unadipatha.model

/**
 * Pāṇinian Semantic Representation (Artha / Meaning).
 */
sealed interface Artha {
    /** Kāraka Meanings (3.4.68 "कर्तरि कृत्", 3.3.18 "भावे") */
    enum class Karaka : Artha {
        KARTA, KARMA, KARANA, SAMPRADANA, APADANA, ADHIKARANA, BHAVA
    }

    /** Contextual / Dispositional / Taddhita Semantic Conditions (उपाधि / अर्थ) */
    enum class Context : Artha {
        TAATSIILYA, TADDHARMA, TATSADHUKARI, SHILPA, AASHIS,
        // Taddhita meanings (4.1.92 "तस्यापत्यम्", 4.2.1 "तेन रक्तम्", 5.2.94 "तदस्यास्त्यस्मिन्निति मतुप्")
        APATYA, RAGATA, SAMUHA, MATVARTHIYA, THAK
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
