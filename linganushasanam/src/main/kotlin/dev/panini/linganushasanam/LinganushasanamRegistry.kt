package dev.panini.linganushasanam

import dev.panini.linganushasanam.adhyaya1.AbantahSutra
import dev.panini.linganushasanam.adhyaya1.KtinantahSutra
import dev.panini.linganushasanam.adhyaya1.NgibantahSutra
import dev.panini.linganushasanam.adhyaya1.SwangebhyahSutra
import dev.panini.linganushasanam.adhyaya1.TtalantahSutra
import dev.panini.linganushasanam.adhyaya1.UjantahSutra
import dev.panini.linganushasanam.adhyaya2.GhajantashCaSutra
import dev.panini.linganushasanam.adhyaya2.DevasuratmaSvargaGiriSamudraSutra
import dev.panini.linganushasanam.adhyaya2.GhanAbantahSutra
import dev.panini.linganushasanam.adhyaya2.PumsiSutra
import dev.panini.linganushasanam.adhyaya2.ShopadhahSutra
import dev.panini.linganushasanam.adhyaya2.TopadhahSutra
import dev.panini.linganushasanam.adhyaya2.UkarantahSutra
import dev.panini.linganushasanam.adhyaya2.RsiRasiDrtiGranthiKrimiDhvaniSutra
import dev.panini.linganushasanam.adhyaya2.DhvajaGajaMunjaPunjaSutra
import dev.panini.linganushasanam.adhyaya2.HastaKuntaAntaVataVrataDutaSutra
import dev.panini.linganushasanam.adhyaya2.HradaKandaKundaBudbudaSabdaSutra
import dev.panini.linganushasanam.adhyaya2.PallavaPalvalaKapharephaSutra
import dev.panini.linganushasanam.adhyaya3.AsunIsunUsunantahSutra
import dev.panini.linganushasanam.adhyaya3.LyudadyantahSutra
import dev.panini.linganushasanam.adhyaya3.NapumsakeSutra
import dev.panini.linganushasanam.adhyaya3.BhrtrAmitraChatraPutraSutra
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
        DevasuratmaSvargaGiriSamudraSutra,
        UkarantahSutra,
        TopadhahSutra,
        ShopadhahSutra,
        RsiRasiDrtiGranthiKrimiDhvaniSutra,
        DhvajaGajaMunjaPunjaSutra,
        HastaKuntaAntaVataVrataDutaSutra,
        HradaKandaKundaBudbudaSabdaSutra,
        PallavaPalvalaKapharephaSutra,
        GhanAbantahSutra,
        GhajantashCaSutra,

        // Chapter 3: Napuṃsakaliṅgam
        NapumsakeSutra,
        BhrtrAmitraChatraPutraSutra,
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
