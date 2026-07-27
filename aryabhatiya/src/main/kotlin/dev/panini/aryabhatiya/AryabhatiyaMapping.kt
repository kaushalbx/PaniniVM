package dev.panini.aryabhatiya

import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana

/**
 * Mapping tables for Aryabhatiya alphanumerical notation (Āryabhaṭīya Geetika Pada 2).
 */
object AryabhatiyaMapping {

    enum class ConsonantType { VARGA, AVARGA }

    fun getConsonantValue(ch: Char): Pair<Long, ConsonantType>? {
        val vyanjana = Vyanjana.fromDevanagari(ch) ?: return null
        val ord = vyanjana.ordinal
        val value = if (ord <= 24) {
            ord + 1L
        } else {
            (ord - 22) * 10L
        }
        val type = if (ord <= 24) ConsonantType.VARGA else ConsonantType.AVARGA
        return Pair(value, type)
    }

    fun getVowelPower(ch: Char): Int? {
        val svara = Svara.fromIndependent(ch) ?: Svara.fromMatra(ch) ?: return null
        val p = svara.ordinal / 2
        return if (p <= 6) p else null
    }

    fun isConsonant(ch: Char): Boolean = Vyanjana.fromDevanagari(ch) != null
}
