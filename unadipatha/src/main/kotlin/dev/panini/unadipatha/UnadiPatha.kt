package dev.panini.unadipatha

import dev.panini.dhatupatha.Dhatu
import dev.panini.unadipatha.adhyaya1.KrGrShrVrbhyahKuSutra
import dev.panini.unadipatha.adhyaya1.KrSrbhyamUnSutra
import dev.panini.unadipatha.adhyaya1.KrvaPajiMiSvadisadhyashubhyoUnSutra
import dev.panini.unadipatha.adhyaya1.TabhyamAnyatrapiDrshyaKartaryUnSutra
import dev.panini.unadipatha.adhyaya2.ShruVrDrPraVrbhyahSihSutra
import dev.panini.unadipatha.adhyaya3.PunsoAsunSutra
import dev.panini.unadipatha.adhyaya4.KrvrDaribhyahKaninSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

object UnadiPatha {
    val sutras: List<UnadiSutra> = listOf(
        KrvaPajiMiSvadisadhyashubhyoUnSutra,
        TabhyamAnyatrapiDrshyaKartaryUnSutra,
        KrSrbhyamUnSutra,
        KrGrShrVrbhyahKuSutra,
        ShruVrDrPraVrbhyahSihSutra,
        PunsoAsunSutra,
        KrvrDaribhyahKaninSutra
    )

    /**
     * Forward Lookup: Finds matching Uṇādi pratyayas for a given Dhātu and optional target Artha/Meaning.
     */
    fun findPratyaya(dhatu: Dhatu, artha: Artha? = null): List<UnadiMatch> {
        val matches = sutras.filter { it.matchesRoot(dhatu) }
        val filtered = if (artha != null) {
            matches.filter { sutra ->
                sutra.meaning == artha || sutra.baseSamjnas.any { s ->
                    s is Samjna.Karaka && artha is Artha.Karaka && s.name == artha.name
                }
            }
        } else {
            matches
        }
        return filtered.map { it.matchFor(dhatu) }
    }

    /**
     * Reverse Lookup: Given a Dhātu and Uṇādi Pratyaya (upadeśa or surface), returns all matching Saṁjñās & Sūtras.
     */
    fun findSamjna(dhatu: Dhatu, pratyaya: String): List<UnadiMatch> {
        return sutras
            .filter { it.matchesRoot(dhatu) && (it.pratyaya == pratyaya || it.pratyayaSurface == pratyaya) }
            .map { it.matchFor(dhatu) }
    }

    /**
     * Etymological / Nirukta Lookup: Given a conventional word (Saṁjñā), finds matching Dhātus & Uṇādi Sūtras.
     */
    fun findByWord(word: String): List<UnadiMatch> {
        val results = mutableListOf<UnadiMatch>()
        for (sutra in sutras) {
            for (root in sutra.roots) {
                val match = sutra.matchFor(root)
                val hasWord = match.samjnas.any { it is Samjna.Rudhi && it.word == word } ||
                        (match.meaning is Artha.Rudhi && (match.meaning as Artha.Rudhi).devanagari == word)
                if (hasWord) {
                    results.add(match)
                }
            }
        }
        return results
    }
}
