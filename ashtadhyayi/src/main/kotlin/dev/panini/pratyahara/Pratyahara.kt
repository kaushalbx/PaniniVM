package dev.panini.pratyahara

import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana

data class Pratyahara(
    val start: Varna,
    val end: ItMarker,
) {
    companion object {
        val AC = Pratyahara(Svara.A, ItMarker.C)
        val AM = Pratyahara(Svara.A, ItMarker.M)
        val CHAV = Pratyahara(Vyanjana.CHA, ItMarker.V)
        val AK = Pratyahara(Svara.A, ItMarker.K)
        val IK = Pratyahara(Svara.I, ItMarker.K)
        val IC = Pratyahara(Svara.I, ItMarker.C)
        val EC = Pratyahara(Svara.E, ItMarker.C)
        val EN = Pratyahara(Svara.E, ItMarker.NG)
        val IN = Pratyahara(Svara.I, ItMarker.NN)
        val AT = Pratyahara(Svara.A, ItMarker.TT)
        val HAL = Pratyahara(Vyanjana.HA, ItMarker.L)
        val VAL = Pratyahara(Vyanjana.VA, ItMarker.L)
        val HAS = Pratyahara(Vyanjana.HA, ItMarker.SHA)
        val ASH = Pratyahara(Svara.A, ItMarker.SHA) // Vowels + Voiced consonants
        val YAN = Pratyahara(Vyanjana.YA, ItMarker.NN)
        val YAM = Pratyahara(Vyanjana.YA, ItMarker.M)
        val YAY = Pratyahara(Vyanjana.YA, ItMarker.Y)
        val YAR = Pratyahara(Vyanjana.YA, ItMarker.R)
        val YANN = Pratyahara(Vyanjana.YA, ItMarker.NY)
        val JHAY = Pratyahara(Vyanjana.JHA, ItMarker.Y)
        val JHAR = Pratyahara(Vyanjana.JHA, ItMarker.R)
        val JHAL = Pratyahara(Vyanjana.JHA, ItMarker.L)
        val KHAR = Pratyahara(Vyanjana.KHA, ItMarker.R)
        val JHASH = Pratyahara(Vyanjana.JHA, ItMarker.SHA)
        val SHAR = Pratyahara(Vyanjana.SHA, ItMarker.R)
        val JAS = Pratyahara(Vyanjana.JA, ItMarker.SHA)
        val CAR = Pratyahara(Vyanjana.CA, ItMarker.R)
    }
}
