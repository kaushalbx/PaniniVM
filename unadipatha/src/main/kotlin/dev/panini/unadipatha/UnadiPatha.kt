package dev.panini.unadipatha

import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.unadipatha.adhyaya1.UnadiAdhyaya1Registry
import dev.panini.unadipatha.adhyaya2.UnadiAdhyaya2Registry
import dev.panini.unadipatha.adhyaya3.UnadiAdhyaya3Registry
import dev.panini.unadipatha.adhyaya4.UnadiAdhyaya4Registry
import dev.panini.unadipatha.adhyaya5.UnadiAdhyaya5Registry

object UnadiPatha {
    val sourceSutras: List<UnadiPathaEntry> get() = UnadiPathaCatalog.sutras

    val modeledSourceSutras: List<UnadiSutra> = listOf(
        UnadiAdhyaya1Registry.modeledSutras,
        UnadiAdhyaya2Registry.modeledSutras,
        UnadiAdhyaya3Registry.modeledSutras,
        UnadiAdhyaya4Registry.modeledSutras,
        UnadiAdhyaya5Registry.modeledSutras,
    ).flatten()

    val sutras: List<UnadiSutra> get() = modeledSourceSutras

    private val legacyExecutableSutras: List<UnadiSutra> = listOf(
        UnadiAdhyaya1Registry.legacySutras,
        UnadiAdhyaya2Registry.legacySutras,
        UnadiAdhyaya3Registry.legacySutras,
        UnadiAdhyaya4Registry.legacySutras,
        UnadiAdhyaya5Registry.legacySutras,
    ).flatten()

    val executableSutras: List<UnadiSutra> = modeledSourceSutras +
        legacyExecutableSutras.filter { legacy -> modeledSourceSutras.none { it.number == legacy.number } }

    fun findPratyaya(dhatu: Dhatu, artha: Artha? = null): List<UnadiMatch> {
        val matches = executableSutras.filter { it.matchesRoot(dhatu) }.map { it.matchFor(dhatu) }
        return if (artha == null) matches else matches.filter { match ->
            match.meaning == artha || match.samjnas.any { samjna ->
                samjna is Samjna.Karaka && artha is Artha.Karaka && samjna.name == artha.name
            }
        }
    }

    fun findSamjna(dhatu: Dhatu, pratyaya: String): List<UnadiMatch> = executableSutras
        .filter { it.matchesRoot(dhatu) && (it.pratyaya == pratyaya || it.pratyayaSurface == pratyaya) }
        .map { it.matchFor(dhatu) }

    fun findByWord(word: String): List<UnadiMatch> {
        val results = mutableListOf<UnadiMatch>()
        for (sutra in executableSutras) {
            for (root in sutra.roots) {
                val match = sutra.matchFor(root)
                val hasWord = match.samjnas.any { it is Samjna.Rudhi && it.word == word } ||
                    (match.meaning is Artha.Rudhi && match.meaning.devanagari == word)
                if (hasWord) results.add(match)
            }
        }
        return results
    }
}
