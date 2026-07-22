package dev.panini.pratyahara

import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.Vyanjana

class PratyaharaEngine(
    sutras: List<MaheshvaraSutra> = MaheshvaraSutras.all,
) {
    private val tokens = sutras.flatMap { it.tokens() }
    private val cache = mutableMapOf<Pratyahara, Set<Varna>>()

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

    fun contains(pratyahara: Pratyahara, char: Char): Boolean {
        val normalizedChar = Varnamala.normalize(char)
        val varna = Varnamala.fromChar(normalizedChar) ?: return false
        val set = derive(pratyahara)

        // Handle matras and independent vowels as equivalent for matching
        return when (varna) {
            is Svara -> set.any {
                it is Svara && (it == varna || Varnamala.normalize(it.devanagari.single()) == Varnamala.normalize(varna.devanagari.single()))
            }
            is Vyanjana -> set.contains(varna)
            else -> false
        }
    }
}
