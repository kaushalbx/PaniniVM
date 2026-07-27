package dev.panini.shiksha

/**
 * Central Pāṇinian Saṁjñās (Grammatical Labels).
 */
sealed interface Samjna {

    /** Morphological & Structural Units (अङ्ग/पद भेद - Aṣṭādhyāyī 1.3.1, 1.4.13, 1.4.14) */
    enum class Unit : Samjna {
        DHATU, PRATYAYA, ANGA, PADA, PRATIPADIKA, SAMASA, AVAYAVA
    }

    /** Affix & Suffix Classifications (प्रत्यय भेद - Aṣṭādhyāyī 3.1.93, 3.3.1, 4.1.76) */
    enum class Affix : Samjna {
        KRT, UNADI, TADDHITA, SARVADHATUKA, ARDHADHATUKA,
        GHAN, NVUL, TRC, KTA, SHATRU, SHANAC, GHINUN,
        AN, IN, CHHA, MATUP, MAYAT, TAL, KTVA, LYAP, TUMUN, TAVYA,
        ANIYAR, NYAT, YAT, KTAVATU, NISHTHA, NIC, SAN, YAN, KYAC,
        YAN_LUK, CAN, TAP, NIP, NIS, NIN, TI_PRATYAYA, TVA, TARAP,
        TAMAP, KVASU, KANAC, YANG, SANADYANTA_DHATU, AN_PRATYAYA,
        IN_PRATYAYA, YAN_PRATYAYA, APATYA, ASUN, USI
    }

    /** Phonological & Vowel Grade Classifications (स्वर/गुण/वृद्धि - Aṣṭādhyāyī 1.1.1, 1.1.2, 1.1.7) */
    enum class Phono : Samjna {
        VRDDHI, GUNA, IK, AC, HAL, SAMYOGA, ANUNASIKA, SAVARNA, IT, CONSONANT_STEM
    }

    /** Nominal & Gender Stem Attributes (अङ्ग विशेषण - Aṣṭādhyāyī 1.4.3, 1.4.7, 1.4.18) */
    enum class Stem : Samjna {
        NADI, GHI, BHA, GHU, PRAGRHYA, SARVANAMA, APRUKTA, SAMBUDDHI, SARVANAMASTHANA, SANKHYA, PURANA, ABHYASA
    }

    /** Indeclinables & Particles (अव्यय/निपात/गति - Aṣṭādhyāyī 1.1.37, 1.4.56, 1.4.59) */
    enum class Avyaya : Samjna {
        AVYAYA, NIPATA, GATI, UPASARGA
    }

    /** Kāraka Roles (कारक संज्ञा - Aṣṭādhyāyī 1.4.23 - 1.4.55) */
    enum class Karaka : Samjna {
        KARTA, KARMA, KARANA, SAMPRADANA, APADANA, ADHIKARANA
    }

    /** Lexical Target Names (रूढि संज्ञा - e.g., "कर्ण", "वायु") */
    data class Rudhi(val word: String) : Samjna

    companion object {
        val DHATU: Samjna = Unit.DHATU
        val PRATYAYA: Samjna = Unit.PRATYAYA
        val ANGA: Samjna = Unit.ANGA
        val PADA: Samjna = Unit.PADA
        val PRATIPADIKA: Samjna = Unit.PRATIPADIKA
        val SAMASA: Samjna = Unit.SAMASA
        val AVAYAVA: Samjna = Unit.AVAYAVA

        val KRT: Samjna = Affix.KRT
        val UNADI: Samjna = Affix.UNADI
        val TADDHITA: Samjna = Affix.TADDHITA
        val SARVADHATUKA: Samjna = Affix.SARVADHATUKA
        val ARDHADHATUKA: Samjna = Affix.ARDHADHATUKA
        val GHAN: Samjna = Affix.GHAN
        val NVUL: Samjna = Affix.NVUL
        val TRC: Samjna = Affix.TRC
        val KTA: Samjna = Affix.KTA
        val SHATRU: Samjna = Affix.SHATRU
        val SATR: Samjna = Affix.SHATRU
        val SHANAC: Samjna = Affix.SHANAC
        val SANAC: Samjna = Affix.SHANAC
        val GHINUN: Samjna = Affix.GHINUN
        val AN: Samjna = Affix.AN
        val IN: Samjna = Affix.IN
        val CHHA: Samjna = Affix.CHHA
        val MATUP: Samjna = Affix.MATUP
        val MAYAT: Samjna = Affix.MAYAT
        val TAL: Samjna = Affix.TAL
        val KTVA: Samjna = Affix.KTVA
        val LYAP: Samjna = Affix.LYAP
        val TUMUN: Samjna = Affix.TUMUN
        val TAVYA: Samjna = Affix.TAVYA
        val ANIYAR: Samjna = Affix.ANIYAR
        val NYAT: Samjna = Affix.NYAT
        val YAT: Samjna = Affix.YAT
        val KTAVATU: Samjna = Affix.KTAVATU
        val NISHTHA: Samjna = Affix.NISHTHA
        val NIC: Samjna = Affix.NIC
        val SAN: Samjna = Affix.SAN
        val YAN: Samjna = Affix.YAN
        val KYAC: Samjna = Affix.KYAC
        val YAN_LUK: Samjna = Affix.YAN_LUK
        val CAN: Samjna = Affix.CAN
        val TAP: Samjna = Affix.TAP
        val NIP: Samjna = Affix.NIP
        val NIS: Samjna = Affix.NIS
        val NIN: Samjna = Affix.NIN
        val TI_PRATYAYA: Samjna = Affix.TI_PRATYAYA
        val TVA: Samjna = Affix.TVA
        val TARAP: Samjna = Affix.TARAP
        val TAMAP: Samjna = Affix.TAMAP
        val KVASU: Samjna = Affix.KVASU
        val KANAC: Samjna = Affix.KANAC
        val YANG: Samjna = Affix.YANG
        val SANADYANTA_DHATU: Samjna = Affix.SANADYANTA_DHATU
        val AN_PRATYAYA: Samjna = Affix.AN_PRATYAYA
        val IN_PRATYAYA: Samjna = Affix.IN_PRATYAYA
        val YAN_PRATYAYA: Samjna = Affix.YAN_PRATYAYA
        val APATYA: Samjna = Affix.APATYA
        val ASUN: Samjna = Affix.ASUN
        val USI: Samjna = Affix.USI

        val VRDDHI: Samjna = Phono.VRDDHI
        val GUNA: Samjna = Phono.GUNA
        val IK: Samjna = Phono.IK
        val AC: Samjna = Phono.AC
        val HAL: Samjna = Phono.HAL
        val SAMYOGA: Samjna = Phono.SAMYOGA
        val ANUNASIKA: Samjna = Phono.ANUNASIKA
        val SAVARNA: Samjna = Phono.SAVARNA
        val IT: Samjna = Phono.IT
        val CONSONANT_STEM: Samjna = Phono.CONSONANT_STEM

        val NADI: Samjna = Stem.NADI
        val GHI: Samjna = Stem.GHI
        val BHA: Samjna = Stem.BHA
        val GHU: Samjna = Stem.GHU
        val PRAGRHYA: Samjna = Stem.PRAGRHYA
        val SARVANAMA: Samjna = Stem.SARVANAMA
        val APRUKTA: Samjna = Stem.APRUKTA
        val SAMBUDDHI: Samjna = Stem.SAMBUDDHI
        val SARVANAMASTHANA: Samjna = Stem.SARVANAMASTHANA
        val SANKHYA: Samjna = Stem.SANKHYA
        val PURANA: Samjna = Stem.PURANA
        val ABHYASA: Samjna = Stem.ABHYASA

        val AVYAYA: Samjna = Avyaya.AVYAYA
        val NIPATA: Samjna = Avyaya.NIPATA
        val GATI: Samjna = Avyaya.GATI
        val UPASARGA: Samjna = Avyaya.UPASARGA

        val KARTA: Samjna = Karaka.KARTA
        val KARMA: Samjna = Karaka.KARMA
        val KARANA: Samjna = Karaka.KARANA
        val SAMPRADANA: Samjna = Karaka.SAMPRADANA
        val APADANA: Samjna = Karaka.APADANA
        val ADHIKARANA: Samjna = Karaka.ADHIKARANA
    }
}
