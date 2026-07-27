package dev.panini.unadipatha.model

/**
 * Pāṇinian Saṁjñās (Grammatical Labels).
 */
sealed interface Samjna {
    /** Technical Grammatical Tags in Aṣṭādhyāyī (1.3.1, 1.2.46, 3.1.93, 4.1.76) */
    enum class Technical : Samjna {
        DHATU, PRATYAYA, ANGA, PADA, PRATIPADIKA, KRT, UNADI, TADDHITA,
        VRDDHI, GUNA, IT, SAMYOGA, NADI, GHI, BHA, SAMASA, AVAYAVA,
        // Kṛt affixes
        GHAN, NVUL, TRC, KTA, SHATRU, SHANAC, GHINUN,
        // Taddhita affixes
        AN, IN, CHHA, MATUP, MAYAT, TAL
    }

    /** Kāraka Saṁjñās (Aṣṭādhyāyī 1.4.23 - 1.4.55) */
    enum class Karaka : Samjna {
        KARTA, KARMA, KARANA, SAMPRADANA, APADANA, ADHIKARANA
    }

    /** Lexical Target Names (Rūḍhi-saṁjñās in ...इति संज्ञायाम्) e.g., "कर्ण", "फलग्रहि" */
    data class Rudhi(val word: String) : Samjna
}
