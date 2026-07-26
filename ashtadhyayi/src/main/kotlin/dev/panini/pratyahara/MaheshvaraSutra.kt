package dev.panini.pratyahara

import dev.panini.shiksha.Varna

class MaheshvaraSutra {
    private val tokens = mutableListOf<MaheshvaraToken>()

    fun add(varna: Varna): MaheshvaraSutra {
        tokens += MaheshvaraToken.Sound(varna)
        return this
    }

    fun marker(marker: ItMarker): MaheshvaraSutra {
        tokens += MaheshvaraToken.Marker(marker)
        return this
    }

    fun tokens(): List<MaheshvaraToken> = tokens.toList()
}
