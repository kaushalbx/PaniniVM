package dev.sanskrit.sandhi

import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.pratyahara.PratyaharaEngine
import dev.sanskrit.shiksha.Svara
import dev.sanskrit.shiksha.Varna
import dev.sanskrit.shiksha.Vyanjana

object Shiksha {
    private val pratyaharaEngine = PratyaharaEngine()

    val svaras = Svara.entries.map { it.devanagari.single() }.toSet()
    val vyanjanas = Vyanjana.entries.map { it.devanagari.single() }.toSet()
    val khar = pratyaharaEngine.derive(Pratyahara.KHAR).toDevanagariChars()

    fun isSvara(value: Char): Boolean = Svara.fromIndependent(value) != null

    fun isVyanjana(value: Char): Boolean = Vyanjana.fromDevanagari(value) != null

    fun startingSvara(word: String): InitialSvara? {
        val first = word.firstOrNull() ?: return null
        val svara = Svara.fromIndependent(first) ?: return null
        return InitialSvara(svara, word.drop(1))
    }

    fun endingSvara(word: String): FinalSvara? {
        val last = word.lastOrNull() ?: return null
        Svara.fromIndependent(last)?.let { return FinalSvara(word.dropLast(1), it, FinalSvaraKind.INDEPENDENT) }
        Svara.fromMatra(last)?.let { return FinalSvara(word.dropLast(1), it, FinalSvaraKind.MATRA) }
        if (last == Vyanjana.VIRAMA) return null
        Vyanjana.fromDevanagari(last) ?: return null
        return FinalSvara(word, Svara.A, FinalSvaraKind.INHERENT)
    }

    fun savarnaDirgha(left: Svara, right: Svara): Svara? {
        return when {
            left in setOf(Svara.A, Svara.AA) && right in setOf(Svara.A, Svara.AA) -> Svara.AA
            left in setOf(Svara.I, Svara.II) && right in setOf(Svara.I, Svara.II) -> Svara.II
            left in setOf(Svara.U, Svara.UU) && right in setOf(Svara.U, Svara.UU) -> Svara.UU
            left in setOf(Svara.R, Svara.RR) && right in setOf(Svara.R, Svara.RR) -> Svara.RR
            left in setOf(Svara.L, Svara.LL) && right in setOf(Svara.L, Svara.LL) -> Svara.LL
            else -> null
        }
    }

    fun replaceFinalSvara(ending: FinalSvara, replacement: Svara): String {
        return when (ending.kind) {
            FinalSvaraKind.INDEPENDENT -> ending.stem + replacement.devanagari
            FinalSvaraKind.MATRA -> ending.stem + (replacement.matra ?: "")
            FinalSvaraKind.INHERENT -> ending.stem + (replacement.matra ?: "")
        }
    }

    fun replaceFinalSvaraWithSamyoga(ending: FinalSvara, vyanjana: Vyanjana): String {
        return when (ending.kind) {
            FinalSvaraKind.INDEPENDENT -> ending.stem + vyanjana.devanagari
            FinalSvaraKind.MATRA -> ending.stem + Vyanjana.VIRAMA + vyanjana.devanagari
            FinalSvaraKind.INHERENT -> ending.stem + Vyanjana.VIRAMA + vyanjana.devanagari
        }
    }

    fun halanta(vyanjana: Vyanjana): String = vyanjana.halanta

    private fun Set<Varna>.toDevanagariChars(): Set<Char> =
        map { it.devanagari.single() }.toSet()
}

data class InitialSvara(
    val svara: Svara,
    val rest: String,
)

data class FinalSvara(
    val stem: String,
    val svara: Svara,
    val kind: FinalSvaraKind,
)

enum class FinalSvaraKind {
    INDEPENDENT,
    MATRA,
    INHERENT,
}
