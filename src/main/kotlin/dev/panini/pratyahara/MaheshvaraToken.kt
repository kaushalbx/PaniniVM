package dev.panini.pratyahara

import dev.panini.shiksha.Varna

sealed interface MaheshvaraToken {
    data class Sound(val varna: Varna) : MaheshvaraToken
    data class Marker(val marker: ItMarker) : MaheshvaraToken
}
