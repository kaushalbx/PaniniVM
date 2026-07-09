package dev.sanskrit.samjna

import dev.sanskrit.shiksha.Svara
import dev.sanskrit.shiksha.Varna
import dev.sanskrit.shiksha.Vyanjana

object VarnaSamjna {
    private val anunasikaVyanjana = setOf(Vyanjana.NYA, Vyanjana.MA, Vyanjana.NGA, Vyanjana.NNA, Vyanjana.NA)

    fun isSamyoga(value: String): Boolean {
        val compact = value.filterNot { it.isWhitespace() }
        if (compact.isBlank()) return false

        var vyanjanaCount = 0
        var index = 0
        val first = Vyanjana.fromDevanagari(compact[index]) ?: return false
        vyanjanaCount++
        index += first.devanagari.length

        while (index < compact.length) {
            if (compact[index] != Vyanjana.VIRAMA) return false
            index++

            if (index == compact.length) return vyanjanaCount >= 2

            val next = Vyanjana.fromDevanagari(compact[index]) ?: return false
            vyanjanaCount++
            index += next.devanagari.length
        }

        return vyanjanaCount >= 2
    }

    fun isAnunasika(varna: Varna): Boolean =
        varna in anunasikaVyanjana

    fun isSavarna(first: Varna, second: Varna): Boolean =
        when {
            first is Svara && second is Svara -> savarnaSvara(first) == savarnaSvara(second)
            first is Vyanjana && second is Vyanjana -> first == second
            else -> false
        }

    private fun savarnaSvara(svara: Svara): String =
        when (svara) {
            Svara.A, Svara.AA -> "अ"
            Svara.I, Svara.II -> "इ"
            Svara.U, Svara.UU -> "उ"
            Svara.R, Svara.RR -> "ऋ"
            Svara.L, Svara.LL -> "ऌ"
            Svara.E -> "ए"
            Svara.AI -> "ऐ"
            Svara.O -> "ओ"
            Svara.AU -> "औ"
        }
}
