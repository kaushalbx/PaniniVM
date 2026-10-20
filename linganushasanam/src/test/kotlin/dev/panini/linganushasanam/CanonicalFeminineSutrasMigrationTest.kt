package dev.panini.linganushasanam

import dev.panini.core.Linga
import dev.panini.linganushasanam.adhyaya1.AnyuPratyayantaDhatuSutra
import dev.panini.linganushasanam.adhyaya1.AsaniBharaniAraniSutra
import dev.panini.linganushasanam.adhyaya1.DundubhiAksesuSutra
import dev.panini.linganushasanam.adhyaya1.DundubhiNabhiAnyatraPumsiSutra
import dev.panini.linganushasanam.adhyaya1.IkarantaSutra
import dev.panini.linganushasanam.adhyaya1.KtinnantaCanonicalSutra
import dev.panini.linganushasanam.adhyaya1.LingamAdhikaraSutra
import dev.panini.linganushasanam.adhyaya1.MinyantaSutra
import dev.panini.linganushasanam.adhyaya1.NabhiAksatriyeSutra
import dev.panini.linganushasanam.adhyaya1.RkarantaMatrDuhitrSutra
import dev.panini.linganushasanam.adhyaya1.SroniYoniUrmiSutra
import dev.panini.linganushasanam.adhyaya1.StriAdhikaraSutra
import dev.panini.linganushasanam.adhyaya1.TalantaCanonicalSutra
import dev.panini.linganushasanam.adhyaya1.UngNgyabantaSutra
import dev.panini.linganushasanam.adhyaya1.VahniVrsniAgniSutra
import dev.panini.linganushasanam.adhyaya1.VimsatyadiAnavatiSutra
import dev.panini.linganushasanam.adhyaya1.YvantaEkaksaraSutra
import dev.panini.linganushasanam.adhyaya1.BhumiVidyutSaritLataVanitaSutra
import dev.panini.linganushasanam.adhyaya1.YadasNapumsakaSutra
import dev.panini.linganushasanam.adhyaya1.BhasSrucSrajDisDrsUsnihUpanahSutra
import dev.panini.linganushasanam.adhyaya1.SthunaUrnaNapumsakeCaSutra
import dev.panini.linganushasanam.adhyaya1.GrhaSasaKlibaSutra
import dev.panini.linganushasanam.adhyaya1.PravrsViprusRutVitTvisSutra
import dev.panini.linganushasanam.adhyaya1.DarviVidiVediKhaniSutra
import dev.panini.linganushasanam.adhyaya1.TithiNadiRuciViciSutra
import dev.panini.linganushasanam.adhyaya1.SaskuliRajiKutiAvantiSutra
import dev.panini.linganushasanam.adhyaya1.PratipadApadVipadSampadSutra
import dev.panini.linganushasanam.adhyaya1.AsisDhurPurGirDvarSutra
import dev.panini.linganushasanam.adhyaya1.ApSumanasVarsaBahutvaSutra
import dev.panini.linganushasanam.adhyaya1.SrajTvacJyokVacYavaguNauSphicSutra
import dev.panini.linganushasanam.adhyaya1.TatitSimaSambadhaSutra
import dev.panini.linganushasanam.adhyaya1.CulliVeniKhariSutra
import dev.panini.linganushasanam.adhyaya1.TaraDharaJyotsnaSutra
import dev.panini.linganushasanam.adhyaya1.SalakaStriyamNityamSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CanonicalFeminineSutrasMigrationTest {
    @Test
    fun `canonical feminine rules use their named implementations`() {
        val expected = listOf(
            LingamAdhikaraSutra,
            StriAdhikaraSutra,
            RkarantaMatrDuhitrSutra,
            AnyuPratyayantaDhatuSutra,
            AsaniBharaniAraniSutra,
            MinyantaSutra,
            VahniVrsniAgniSutra,
            SroniYoniUrmiSutra,
            KtinnantaCanonicalSutra,
            IkarantaSutra,
            UngNgyabantaSutra,
            YvantaEkaksaraSutra,
            VimsatyadiAnavatiSutra,
            DundubhiAksesuSutra,
            NabhiAksatriyeSutra,
            DundubhiNabhiAnyatraPumsiSutra,
            TalantaCanonicalSutra,
        )

        expected.forEachIndexed { index, sutra ->
            assertSame(sutra, LinganushasanamRegistry.require((index + 1).toString()))
        }

        val continuation = listOf(
            BhumiVidyutSaritLataVanitaSutra,
            YadasNapumsakaSutra,
            BhasSrucSrajDisDrsUsnihUpanahSutra,
            SthunaUrnaNapumsakeCaSutra,
            GrhaSasaKlibaSutra,
            PravrsViprusRutVitTvisSutra,
            DarviVidiVediKhaniSutra,
            TithiNadiRuciViciSutra,
            SaskuliRajiKutiAvantiSutra,
            PratipadApadVipadSampadSutra,
            AsisDhurPurGirDvarSutra,
            ApSumanasVarsaBahutvaSutra,
            SrajTvacJyokVacYavaguNauSphicSutra,
            TatitSimaSambadhaSutra,
            CulliVeniKhariSutra,
            TaraDharaJyotsnaSutra,
            SalakaStriyamNityamSutra,
        )
        continuation.forEachIndexed { index, sutra ->
            assertSame(sutra, LinganushasanamRegistry.require((index + 18).toString()))
        }
    }

    @Test
    fun `adhikaras scope rules without directly matching words`() {
        assertFalse(LingamAdhikaraSutra.matches(LingaRuleContext("मातृ")))
        assertFalse(StriAdhikaraSutra.matches(LingaRuleContext("मातृ")))
    }

    @Test
    fun `lexical affix and final varna rules match directly`() {
        assertTrue(RkarantaMatrDuhitrSutra.matches(LingaRuleContext("दुहितृ")))
        assertTrue(AnyuPratyayantaDhatuSutra.matches(LingaRuleContext("धातुज", pratyaya = "अनि", isDhatuDerivative = true)))
        assertTrue(MinyantaSutra.matches(LingaRuleContext("धातुज", pratyaya = "नि", isDhatuDerivative = true)))
        assertTrue(KtinnantaCanonicalSutra.matches(LingaRuleContext("भक्ति", pratyaya = "क्तिन्")))
        assertTrue(IkarantaSutra.matches(LingaRuleContext("अङ्गुली")))
        assertFalse(IkarantaSutra.matches(LingaRuleContext("अङ्गुलि")))
    }

    @Test
    fun `alternative gender scopes are retained`() {
        assertEquals(setOf(Linga.STRI, Linga.PUMS), AsaniBharaniAraniSutra.targetLingas)
        assertEquals(setOf(Linga.STRI, Linga.PUMS), SroniYoniUrmiSutra.targetLingas)
        assertEquals(setOf(Linga.PUMS), VahniVrsniAgniSutra.targetLingas)
    }
}
