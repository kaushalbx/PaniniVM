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
import dev.panini.linganushasanam.adhyaya2.SandaMandaKarandaSutra
import dev.panini.linganushasanam.adhyaya2.VamsaAmsaPurodasaSutra
import dev.panini.linganushasanam.adhyaya2.ArghaPathiMathiRbhuksiSutra
import dev.panini.linganushasanam.adhyaya2.SarathiAtithiKuksiSutra
import dev.panini.linganushasanam.adhyaya3.AsunIsunUsunantahSutra
import dev.panini.linganushasanam.adhyaya3.LyudadyantahSutra
import dev.panini.linganushasanam.adhyaya3.NapumsakeSutra
import dev.panini.linganushasanam.adhyaya3.BhrtrAmitraChatraPutraSutra
import dev.panini.linganushasanam.adhyaya3.SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra
import dev.panini.linganushasanam.adhyaya3.DandaMandaKhandaSavaSaindhavaParsvaSutra
import dev.panini.linganushasanam.adhyaya4.VisesyanighnaSutra
import dev.panini.linganushasanam.adhyaya5.AnehamAnyapadartheSutra
import dev.panini.linganushasanam.adhyaya5.ParavallingamDvandvaTatpurusayohSutra
import dev.panini.linganushasanam.adhyaya5.SaNapumsakamSutra

/**
 * Catalog of authentic Pāṇinian Liṅgānuśāsana Sūtras.
 */
object LinganushasanamRegistry {
    private val legacyExecutableSutras: List<LinganushasanaSutra> = listOf(
        // Earlier module-local numbering retained for API compatibility.
        SwangebhyahSutra,
        AbantahSutra,
        NgibantahSutra,
        KtinantahSutra,
        UjantahSutra,
        TtalantahSutra,

        PumsiSutra,
        DevasuratmaSvargaGiriSamudraSutra,
        UkarantahSutra,
        TopadhahSutra,
        ShopadhahSutra,
        RsiRasiDrtiGranthiKrimiDhvaniSutra,
        DhvajaGajaMunjaPunjaSutra,
        HastaKuntaAntaVataVrataDutaSutra,
        SandaMandaKarandaSutra,
        VamsaAmsaPurodasaSutra,
        HradaKandaKundaBudbudaSabdaSutra,
        ArghaPathiMathiRbhuksiSutra,
        PallavaPalvalaKapharephaSutra,
        SarathiAtithiKuksiSutra,
        GhanAbantahSutra,
        GhajantashCaSutra,

        NapumsakeSutra,
        BhrtrAmitraChatraPutraSutra,
        SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra,
        DandaMandaKhandaSavaSaindhavaParsvaSutra,
        LyudadyantahSutra,
        AsunIsunUsunantahSutra,

        VisesyanighnaSutra,

        SaNapumsakamSutra,
        AnehamAnyapadartheSutra,
        ParavallingamDvandvaTatpurusayohSutra,
    )

    private val canonicalImplementationsByNumber = legacyExecutableSutras
        .filterNot { '.' in it.number }
        .associateBy { it.number }

    internal val dedicatedCanonicalNumbers: Set<String> get() = canonicalImplementationsByNumber.keys

    /** All 191 canonical numbered sūtras, in textual order. */
    val canonicalSutras: List<LinganushasanaSutra> = CanonicalLinganushasanamCorpus.entries.map { entry ->
        canonicalImplementationsByNumber[entry.number]
            ?: CanonicalDeclarativeSutra(
                number = entry.number,
                text = entry.text,
                section = entry.section,
                prescribedLingas = entry.lingas,
                kind = entry.kind,
            )
    }

    /**
     * Complete canonical corpus plus the module's historical dotted-number
     * executable rules. Runtime evaluation remains priority ordered.
     */
    val sutras: List<LinganushasanaSutra> =
        (canonicalSutras + legacyExecutableSutras.filter { '.' in it.number })
            .sortedWith(compareByDescending<LinganushasanaSutra> { it.priority }
                .thenByDescending { it.number.substringBefore('.').toIntOrNull() ?: 0 })

    fun require(number: String): LinganushasanaSutra =
        sutras.firstOrNull { it.number == number } ?: error("Liṅgānuśāsana Sūtra $number not found in registry.")
}
