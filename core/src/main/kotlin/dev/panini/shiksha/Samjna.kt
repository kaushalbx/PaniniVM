package dev.panini.shiksha

/**
 * Core Pāṇinian Saṁjñās (Grammatical Labels).
 */
sealed interface Samjna {

    /** Technical Grammatical Tags in Aṣṭādhyāyī (1.3.1, 1.2.46, 3.1.93, 4.1.76) */
    enum class Technical : Samjna {
        VRDDHI, GUNA, IK, AC, HAL, SAMYOGA, ANUNASIKA, SAVARNA,
        DHATU, PRATYAYA, ANGA, PADA, PRAGRHYA, SARVANAMA, BHA, GHI,
        NADI, APRUKTA, SAMBUDDHI, SARVANAMASTHANA, GHU, PRATIPADIKA,
        AVYAYA, NIPATA, GATI, UPASARGA, ABHYASA, SANKHYA, PURANA,
        SARVADHATUKA, ARDHADHATUKA, KRT, KTVA, LYAP, TUMUN, TAVYA,
        ANIYAR, NYAT, YAT, KTA, KTAVATU, NISHTHA, NVUL, TRC, GHAN,
        NIC, SAN, YAN, KYAC, YAN_LUK, CAN,
        TAP, NIP, NIS, NIN, TI_PRATYAYA,
        MATUP, TVA, TAL, TARAP, TAMAP,
        SATR, SANAC, KVASU, KANAC, UNADI, TADDHITA,
        YANG, SANADYANTA_DHATU,
        AN_PRATYAYA, IN_PRATYAYA, YAN_PRATYAYA, APATYA,
        ASUN, USI, CONSONANT_STEM, SAMASA, AVAYAVA,
        GHINUN, AN, IN, CHHA, MAYAT
    }

    /** Kāraka Saṁjñās (Aṣṭādhyāyī 1.4.23 - 1.4.55) */
    enum class Karaka : Samjna {
        KARTA, KARMA, KARANA, SAMPRADANA, APADANA, ADHIKARANA
    }

    /** Lexical Target Names (Rūḍhi-saṁjñās in ...इति संज्ञायाम्) e.g., "कर्ण", "फलग्रहि" */
    data class Rudhi(val word: String) : Samjna

    companion object {
        val VRDDHI: Samjna = Technical.VRDDHI
        val GUNA: Samjna = Technical.GUNA
        val IK: Samjna = Technical.IK
        val AC: Samjna = Technical.AC
        val HAL: Samjna = Technical.HAL
        val SAMYOGA: Samjna = Technical.SAMYOGA
        val ANUNASIKA: Samjna = Technical.ANUNASIKA
        val SAVARNA: Samjna = Technical.SAVARNA
        val DHATU: Samjna = Technical.DHATU
        val PRATYAYA: Samjna = Technical.PRATYAYA
        val ANGA: Samjna = Technical.ANGA
        val PADA: Samjna = Technical.PADA
        val PRAGRHYA: Samjna = Technical.PRAGRHYA
        val SARVANAMA: Samjna = Technical.SARVANAMA
        val BHA: Samjna = Technical.BHA
        val GHI: Samjna = Technical.GHI
        val NADI: Samjna = Technical.NADI
        val APRUKTA: Samjna = Technical.APRUKTA
        val SAMBUDDHI: Samjna = Technical.SAMBUDDHI
        val SARVANAMASTHANA: Samjna = Technical.SARVANAMASTHANA
        val GHU: Samjna = Technical.GHU
        val PRATIPADIKA: Samjna = Technical.PRATIPADIKA
        val AVYAYA: Samjna = Technical.AVYAYA
        val NIPATA: Samjna = Technical.NIPATA
        val GATI: Samjna = Technical.GATI
        val UPASARGA: Samjna = Technical.UPASARGA
        val ABHYASA: Samjna = Technical.ABHYASA
        val SANKHYA: Samjna = Technical.SANKHYA
        val PURANA: Samjna = Technical.PURANA
        val SARVADHATUKA: Samjna = Technical.SARVADHATUKA
        val ARDHADHATUKA: Samjna = Technical.ARDHADHATUKA
        val KRT: Samjna = Technical.KRT
        val KTVA: Samjna = Technical.KTVA
        val LYAP: Samjna = Technical.LYAP
        val TUMUN: Samjna = Technical.TUMUN
        val TAVYA: Samjna = Technical.TAVYA
        val ANIYAR: Samjna = Technical.ANIYAR
        val NYAT: Samjna = Technical.NYAT
        val YAT: Samjna = Technical.YAT
        val KTA: Samjna = Technical.KTA
        val KTAVATU: Samjna = Technical.KTAVATU
        val NISHTHA: Samjna = Technical.NISHTHA
        val NVUL: Samjna = Technical.NVUL
        val TRC: Samjna = Technical.TRC
        val GHAN: Samjna = Technical.GHAN
        val NIC: Samjna = Technical.NIC
        val SAN: Samjna = Technical.SAN
        val YAN: Samjna = Technical.YAN
        val KYAC: Samjna = Technical.KYAC
        val YAN_LUK: Samjna = Technical.YAN_LUK
        val CAN: Samjna = Technical.CAN
        val TAP: Samjna = Technical.TAP
        val NIP: Samjna = Technical.NIP
        val NIS: Samjna = Technical.NIS
        val NIN: Samjna = Technical.NIN
        val TI_PRATYAYA: Samjna = Technical.TI_PRATYAYA
        val MATUP: Samjna = Technical.MATUP
        val TVA: Samjna = Technical.TVA
        val TAL: Samjna = Technical.TAL
        val TARAP: Samjna = Technical.TARAP
        val TAMAP: Samjna = Technical.TAMAP
        val SATR: Samjna = Technical.SATR
        val SANAC: Samjna = Technical.SANAC
        val KVASU: Samjna = Technical.KVASU
        val KANAC: Samjna = Technical.KANAC
        val UNADI: Samjna = Technical.UNADI
        val TADDHITA: Samjna = Technical.TADDHITA
        val YANG: Samjna = Technical.YANG
        val SANADYANTA_DHATU: Samjna = Technical.SANADYANTA_DHATU
        val AN_PRATYAYA: Samjna = Technical.AN_PRATYAYA
        val IN_PRATYAYA: Samjna = Technical.IN_PRATYAYA
        val YAN_PRATYAYA: Samjna = Technical.YAN_PRATYAYA
        val APATYA: Samjna = Technical.APATYA
        val ASUN: Samjna = Technical.ASUN
        val USI: Samjna = Technical.USI
        val CONSONANT_STEM: Samjna = Technical.CONSONANT_STEM
        val SAMASA: Samjna = Technical.SAMASA
        val AVAYAVA: Samjna = Technical.AVAYAVA
    }
}
