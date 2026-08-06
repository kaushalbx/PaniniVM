package dev.panini.linganushasanam

import dev.panini.linganushasanam.adhyaya1.AbantahSutra
import dev.panini.linganushasanam.adhyaya1.KtinantahSutra
import dev.panini.linganushasanam.adhyaya1.NgibantahSutra
import dev.panini.linganushasanam.adhyaya1.SwangebhyahSutra
import dev.panini.linganushasanam.adhyaya1.TtalantahSutra
import dev.panini.linganushasanam.adhyaya1.UjantahSutra
import dev.panini.linganushasanam.adhyaya2.ErachantahSutra
import dev.panini.linganushasanam.adhyaya2.GhajantahSutra
import dev.panini.linganushasanam.adhyaya2.NranahSutra
import dev.panini.linganushasanam.adhyaya2.PumsiSutra
import dev.panini.linganushasanam.adhyaya3.AsunIsunUsunantahSutra
import dev.panini.linganushasanam.adhyaya3.LyudadyantahSutra
import dev.panini.linganushasanam.adhyaya3.NapumsakeSutra
import dev.panini.linganushasanam.adhyaya4.VisesyanighnaSutra
import dev.panini.linganushasanam.adhyaya5.AnehamAnyapadartheSutra
import dev.panini.linganushasanam.adhyaya5.ParavallingamDvandvaTatpurusayohSutra
import dev.panini.linganushasanam.adhyaya5.SaNapumsakamSutra

/**
 * Catalog of authentic Pāṇinian Liṅgānuśāsana Sūtras.
 */
object LinganushasanamRegistry {
    val sutras: List<LinganushasanaSutra> = listOf(
        // Chapter 1: Strīliṅgam
        SwangebhyahSutra,
        AbantahSutra,
        NgibantahSutra,
        KtinantahSutra,
        UjantahSutra,
        TtalantahSutra,

        // Chapter 2: Puṃliṅgam
        PumsiSutra,
        GhajantahSutra,
        ErachantahSutra,
        NranahSutra,

        // Chapter 3: Napuṃsakaliṅgam
        NapumsakeSutra,
        LyudadyantahSutra,
        AsunIsunUsunantahSutra,

        // Chapter 4: Viśeṣyanighnaliṅgam
        VisesyanighnaSutra,

        // Chapter 5: Samāsaliṅgam
        SaNapumsakamSutra,
        AnehamAnyapadartheSutra,
        ParavallingamDvandvaTatpurusayohSutra,
    ).sortedByDescending { it.priority }

    fun require(number: String): LinganushasanaSutra =
        sutras.firstOrNull { it.number == number } ?: error("Liṅgānuśāsana Sūtra $number not found in registry.")
}
