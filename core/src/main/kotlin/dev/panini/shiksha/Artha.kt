package dev.panini.shiksha

/**
 * Central Pāṇinian Semantic Representation (Artha / Meaning).
 */
sealed interface Artha {

    /** Kāraka & Expressed Action Roles (कारक अर्थ - Aṣṭādhyāyī 3.4.68, 3.3.18) */
    enum class Karaka : Artha {
        KARTA, KARMA, KARANA, SAMPRADANA, APADANA, ADHIKARANA, BHAVA
    }

    /** Contextual, Dispositional & Habitual Conditions (उपाधि / स्वभाव - Aṣṭādhyāyī 3.2.134) */
    enum class Dispositional : Artha {
        TAATSIILYA, TADDHARMA, TATSADHUKARI, SHILPA, AASHIS
    }

    /** Secondary Affix Meanings (तद्धित अर्थ - Aṣṭādhyāyī 4.1.92, 4.2.1, 5.2.94) */
    enum class Taddhita : Artha {
        APATYA, RAGATA, SAMUHA, MATVARTHIYA, THAK, BHAVA_TADDHITA
    }

    /** Conventional Lexical Sense (रूढि अर्थ) */
    data class Rudhi(
        val devanagari: String,
        val english: String? = null
    ) : Artha

    /** Textual Commentary / Explanation */
    data class Explanation(
        val hindi: String,
        val english: String? = null
    ) : Artha
}
