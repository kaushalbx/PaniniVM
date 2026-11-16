package dev.panini.pratyahara

import dev.panini.shiksha.Varna
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.Svara

class PratyaharaEngine(
    sutras: List<MaheshvaraSutra> = MaheshvaraSutras.all,
) {
    private val tokens = sutras.flatMap { it.tokens() }
    private val cache = java.util.concurrent.ConcurrentHashMap<Pratyahara, Set<Varna>>()

    fun derive(pratyahara: Pratyahara): Set<Varna> {
        return cache.getOrPut(pratyahara) {
            val startIndex = tokens.indexOfFirst {
                it is MaheshvaraToken.Sound && it.varna == pratyahara.start
            }
            if (startIndex == -1) return@getOrPut emptySet()

            val markerIndex = tokens.withIndex()
                .drop(startIndex + 1)
                .firstOrNull { (_, token) ->
                    token is MaheshvaraToken.Marker && token.marker == pratyahara.end
                }
                ?.index ?: return@getOrPut emptySet()

            tokens.subList(startIndex, markerIndex)
                .mapNotNull { (it as? MaheshvaraToken.Sound)?.varna }
                .toSet()
        }
    }

    /**
     * 1.1.71 आदिरन्त्येन सहेता: a pratyāhāra is named by its first sound and its closing it-letter,
     * as varṇas: the sound, अ to pronounce a consonant (ह + अ + ल् = हल्), then the it-letter. Each
     * must denote a range here; a name reached by two markers (अण्, इण्) is one name.
     */
    fun names(pratyaharas: List<Pratyahara> = Pratyahara.ATTESTED): List<List<Varna>> = pratyaharas.map { pratyahara ->
        require(derive(pratyahara).isNotEmpty()) { "No range closes ${pratyahara.start} on ${pratyahara.end}." }
        val sound = pratyahara.start
        (if (sound is Svara) listOf(sound) else listOf(sound, Svara.A)) + pratyahara.end.varna
    }.distinct()

    fun contains(pratyahara: Pratyahara, char: Char): Boolean {
        val normalizedChar = Varnamala.normalize(char)
        val varna = Varnamala.fromChar(normalizedChar) ?: return false
        return contains(pratyahara, varna)
    }

    /** Canonical phonological membership API; orthographic adapters delegate here. */
    fun contains(pratyahara: Pratyahara, varna: Varna): Boolean {
        val members = derive(pratyahara)
        return varna in members ||
            (varna is Svara && members.filterIsInstance<Svara>().any { Varnamala.areSavarna(it, varna) })
    }
}
