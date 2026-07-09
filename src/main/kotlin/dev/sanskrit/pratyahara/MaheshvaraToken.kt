package dev.sanskrit.pratyahara

import dev.sanskrit.shiksha.Varna

sealed interface MaheshvaraToken {
    data class Sound(val varna: Varna) : MaheshvaraToken
    data class Marker(val marker: ItMarker) : MaheshvaraToken
}
