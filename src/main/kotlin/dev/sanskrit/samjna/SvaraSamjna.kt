package dev.sanskrit.samjna

import dev.sanskrit.shiksha.Svara

object SvaraSamjna {
    val ik: Set<Svara> = setOf(Svara.I, Svara.II, Svara.U, Svara.UU, Svara.R, Svara.RR, Svara.L, Svara.LL)
    val vrddhi: Set<Svara> = setOf(Svara.AA, Svara.AI, Svara.AU)
    val guna: Set<Svara> = setOf(Svara.A, Svara.E, Svara.O)

    fun isIk(svara: Svara): Boolean = svara in ik

    fun isVrddhi(svara: Svara): Boolean = svara in vrddhi

    fun isGuna(svara: Svara): Boolean = svara in guna

    fun gunaForIk(svara: Svara): Svara? =
        when (svara) {
            Svara.I, Svara.II -> Svara.E
            Svara.U, Svara.UU -> Svara.O
            Svara.R, Svara.RR, Svara.L, Svara.LL -> Svara.A
            else -> null
        }

    fun vrddhiForIk(svara: Svara): Svara? =
        when (svara) {
            Svara.I, Svara.II -> Svara.AI
            Svara.U, Svara.UU -> Svara.AU
            Svara.R, Svara.RR, Svara.L, Svara.LL -> Svara.AA
            else -> null
        }
}
