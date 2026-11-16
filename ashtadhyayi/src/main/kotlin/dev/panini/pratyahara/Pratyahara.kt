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

        /**
         * The pratyāhāras the Aṣṭādhyāyī uses (the traditional forty-odd, as listed in the
         * Siddhāntakaumudī on 1.1.71). Each is a start varṇa and an it-marker; its name and its range
         * both come from the engine. अण् and इण् close on ण् of either sūtra; the name is the same.
         */
        val ATTESTED: List<Pratyahara> = listOf(
            AK, AC, AT, Pratyahara(Svara.A, ItMarker.N), AM, ASH, Pratyahara(Svara.A, ItMarker.L),
            IK, IC, IN, Pratyahara(Svara.U, ItMarker.K), EN, EC, Pratyahara(Svara.AI, ItMarker.C),
            Pratyahara(Vyanjana.HA, ItMarker.SHA), HAL, YAN, YAM, YANN, YAY, YAR,
            Pratyahara(Vyanjana.VA, ItMarker.SHA), VAL, Pratyahara(Vyanjana.RA, ItMarker.L),
            Pratyahara(Vyanjana.NYA, ItMarker.M), Pratyahara(Vyanjana.MA, ItMarker.Y), Pratyahara(Vyanjana.NGA, ItMarker.M),
            Pratyahara(Vyanjana.JHA, ItMarker.SH), JHASH, JHAY, JHAR, JHAL, Pratyahara(Vyanjana.BHA, ItMarker.SH),
            JAS, Pratyahara(Vyanjana.BA, ItMarker.SHA), CHAV, Pratyahara(Vyanjana.KHA, ItMarker.Y), KHAR,
            Pratyahara(Vyanjana.CA, ItMarker.Y), CAR, SHAR, Pratyahara(Vyanjana.SHA, ItMarker.L),
        )
    }
}
