package dev.panini.linganushasanam

import dev.panini.core.Linga
import dev.panini.linganushasanam.sutras.AnyuPratyayantaDhatuSutra
import dev.panini.linganushasanam.sutras.AsaniBharaniAraniSutra
import dev.panini.linganushasanam.sutras.DundubhiAksesuSutra
import dev.panini.linganushasanam.sutras.DundubhiNabhiAnyatraPumsiSutra
import dev.panini.linganushasanam.sutras.IkarantaSutra
import dev.panini.linganushasanam.sutras.KtinnantaCanonicalSutra
import dev.panini.linganushasanam.sutras.LingamAdhikaraSutra
import dev.panini.linganushasanam.sutras.MinyantaSutra
import dev.panini.linganushasanam.sutras.NabhiAksatriyeSutra
import dev.panini.linganushasanam.sutras.RkarantaMatrDuhitrSutra
import dev.panini.linganushasanam.sutras.SroniYoniUrmiSutra
import dev.panini.linganushasanam.sutras.StriAdhikaraSutra
import dev.panini.linganushasanam.sutras.TalantaCanonicalSutra
import dev.panini.linganushasanam.sutras.UngNgyabantaSutra
import dev.panini.linganushasanam.sutras.VahniVrsniAgniSutra
import dev.panini.linganushasanam.sutras.VimsatyadiAnavatiSutra
import dev.panini.linganushasanam.sutras.YvantaEkaksaraSutra
import dev.panini.linganushasanam.sutras.BhumiVidyutSaritLataVanitaSutra
import dev.panini.linganushasanam.sutras.YadasNapumsakaSutra
import dev.panini.linganushasanam.sutras.BhasSrucSrajDisDrsUsnihUpanahSutra
import dev.panini.linganushasanam.sutras.SthunaUrnaNapumsakeCaSutra
import dev.panini.linganushasanam.sutras.GrhaSasaKlibaSutra
import dev.panini.linganushasanam.sutras.PravrsViprusRutVitTvisSutra
import dev.panini.linganushasanam.sutras.DarviVidiVediKhaniSutra
import dev.panini.linganushasanam.sutras.TithiNadiRuciViciSutra
import dev.panini.linganushasanam.sutras.SaskuliRajiKutiAvantiSutra
import dev.panini.linganushasanam.sutras.PratipadApadVipadSampadSutra
import dev.panini.linganushasanam.sutras.AsisDhurPurGirDvarSutra
import dev.panini.linganushasanam.sutras.ApSumanasVarsaBahutvaSutra
import dev.panini.linganushasanam.sutras.SrajTvacJyokVacYavaguNauSphicSutra
import dev.panini.linganushasanam.sutras.TatitSimaSambadhaSutra
import dev.panini.linganushasanam.sutras.CulliVeniKhariSutra
import dev.panini.linganushasanam.sutras.TaraDharaJyotsnaSutra
import dev.panini.linganushasanam.sutras.SalakaStriyamNityamSutra
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
