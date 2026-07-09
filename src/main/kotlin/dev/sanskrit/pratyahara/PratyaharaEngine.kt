package dev.sanskrit.pratyahara

import dev.sanskrit.shiksha.Varna

class PratyaharaEngine(
    sutras: List<MaheshvaraSutra> = MaheshvaraSutras.all,
) {
    private val tokens = sutras.flatMap { it.tokens() }

    fun derive(pratyahara: Pratyahara): Set<Varna> {
        val startIndex = tokens.indexOfFirst {
            it is MaheshvaraToken.Sound && it.varna == pratyahara.start
        }
        require(startIndex >= 0) { "Unknown pratyahara start: ${pratyahara.start}" }

        val markerIndex = tokens.withIndex()
            .drop(startIndex + 1)
            .firstOrNull { (_, token) ->
                token is MaheshvaraToken.Marker && token.marker == pratyahara.end
            }
            ?.index

        require(markerIndex != null) {
            "No marker ${pratyahara.end} found after ${pratyahara.start}"
        }

        return tokens.subList(startIndex, markerIndex)
            .mapNotNull { (it as? MaheshvaraToken.Sound)?.varna }
            .toSet()
    }
}
