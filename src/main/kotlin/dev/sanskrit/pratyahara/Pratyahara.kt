package dev.sanskrit.pratyahara

import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.shiksha.Varna
import dev.sanskrit.shiksha.Svara

data class Pratyahara(
    val start: Varna,
    val end: ItMarker,
) {
    companion object {
        val AC = Pratyahara(Svara.A, ItMarker.C)
        val IK = Pratyahara(Svara.I, ItMarker.K)
        val EC = Pratyahara(Svara.E, ItMarker.C)
        val HAL = Pratyahara(Vyanjana.HA, ItMarker.L)
        val YAN = Pratyahara(Vyanjana.YA, ItMarker.NN)
        val JHAL = Pratyahara(Vyanjana.JHA, ItMarker.L)
        val KHAR = Pratyahara(Vyanjana.KHA, ItMarker.R)
        val JHASH = Pratyahara(Vyanjana.JHA, ItMarker.SHA)
        val SHAR = Pratyahara(Vyanjana.SHA, ItMarker.R)
    }
}
