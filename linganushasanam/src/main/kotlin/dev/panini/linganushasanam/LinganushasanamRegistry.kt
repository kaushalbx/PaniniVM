package dev.panini.linganushasanam

import dev.panini.linganushasanam.adhyaya1.AbantahSutra
import dev.panini.linganushasanam.adhyaya1.KtinantahSutra
import dev.panini.linganushasanam.adhyaya1.NgibantahSutra
import dev.panini.linganushasanam.adhyaya3.AsunIsunUsunantahSutra
import dev.panini.linganushasanam.adhyaya3.LyudadyantahSutra
import dev.panini.linganushasanam.adhyaya5.AnehamAnyapadartheSutra
import dev.panini.linganushasanam.adhyaya5.ParavallingamDvandvaTatpurusayohSutra
import dev.panini.linganushasanam.adhyaya5.SaNapumsakamSutra

/**
 * Catalog of authentic Pāṇinian Liṅgānuśāsana Sūtras.
 */
object LinganushasanamRegistry {
    val sutras: List<LinganushasanaSutra> = listOf(
        // Chapter 1: Strīliṅgam
        AbantahSutra,
        NgibantahSutra,
        KtinantahSutra,

        // Chapter 3: Napuṃsakaliṅgam
        LyudadyantahSutra,
        AsunIsunUsunantahSutra,

        // Chapter 5: Samāsaliṅgam
        SaNapumsakamSutra,
        AnehamAnyapadartheSutra,
        ParavallingamDvandvaTatpurusayohSutra,
    ).sortedByDescending { it.priority }

    fun require(number: String): LinganushasanaSutra =
        sutras.firstOrNull { it.number == number } ?: error("Liṅgānuśāsana Sūtra $number not found in registry.")
}
