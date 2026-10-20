package dev.panini.linganushasanam

import dev.panini.linganushasanam.adhyaya3.*
import kotlin.test.Test
import kotlin.test.assertSame

class CanonicalNeuterSutrasMigrationTest {
    @Test
    fun `canonical neuter rules through one hundred thirty six use named implementations`() {
        val expected = listOf(
            NapumsakamAdhikaraSutra, BhaveLyudantaSutra, NisthaBhaveSutra,
            TvaSyanTaddhitaSutra, KarmaniBrahmanadiGunavacanaSutra,
            YadYadhakBhavaKarmaniSutra, AvyayibhavaSutra, DvandvaEkatvaSutra,
            AbhasayamHemantaSisiraAhoratraSutra, AnanKarmadharayaTatpurusaAdhikaraSutra,
            AnalpeChayaSutra, RajaAmanusyaPurvaSabhaSutra, SuraSenaChayaSalaNisaSutra,
            ParavatSutra, RatraAhnaAhaPumsiSutra, SankhyaPurvaRatriSutra,
            ApathaPunyahaNapumsakaSutra, DviguStriCaSutra, IsUsantaSutra,
        )
        expected.forEachIndexed { index, sutra ->
            assertSame(sutra, LinganushasanamRegistry.require((index + 118).toString()))
        }

        val lexicalContinuation = listOf(
            ArcisStriCaSutra, ChadisStriyamEvaSutra, MukhaNayanaLohaVanaSutra,
            SiraArthaOdanaPumsiSutra, VaktraNetraAranyaGandivaSutra,
            AtaviStriyamSutra, LopadhaSutra, TulaUpalaTalaKusulaSutra,
            SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra,
        )
        lexicalContinuation.forEachIndexed { index, sutra ->
            assertSame(sutra, LinganushasanamRegistry.require((index + 137).toString()))
        }

        val finalNeuterRules = listOf(
            SatadiSankhyaSutra, SataAyutaPrayutaPumsiCaSutra, LaksaKotiStriyamSutra,
            SankuPumsiSutra, SahasraPumsiCaSutra, ManDvyacAkartariSutra,
            BrahmanPumsiCaSutra, NamanRomanNapumsakaSutra, AsantaDvyacSutra,
            ApsarasStriyamSutra, TrantaSutra, YatraMatraBhastraDamstraVastraSutra,
            BhrtrAmitraChatraPutraSutra, PatraPatraPavitraSutraChatraSutra,
            BalaKusumaSulbaPattanaRanaSutra, PadmaKamalaUtpalaPumsiCaSutra,
            AhavaSangramaPumsiSutra, AjiStriyamEvaSutra, PhalaJatiSutra,
            VrksaJatiStriyamEvaSutra, ViyatJagatSakrtSakanSutra,
            NavanitaAvanataAnrtaAmrtaSutra, SraddhaKulisaDaivaPithaSutra,
            DaivaPumsiCaSutra, DhanyaAjyaSasyaRupyaSutra,
            DvandvaBarhaDuhkhaBadisaSutra, AksaIndriyeSutra,
        )
        finalNeuterRules.forEachIndexed { index, sutra ->
            assertSame(sutra, LinganushasanamRegistry.require((index + 146).toString()))
        }

        val commonGenderRules = listOf(
            StriPumsayohAdhikaraSutra, GoManiYastiMustiSutra, MrtyuSidhuKarkandhuSutra,
            GunavacanaUkarantaNapumsakamCaSutra, ApatyarthaTaddhitaSutra,
            PumNapumsakayohAdhikaraSutra, DhrtaBhutaMustaKsvelitaSutra,
            SrngaArdhaNidaghaUdyamaSutra, VrajaKunjaKuthaKurcaSutra,
            KabandhaAusadhaAyudhantaSutra, DandaMandaKhandaSavaSaindhavaParsvaSutra,
            GrhaMehaDehaPattaSutra, AvisistaLingamAdhikaraSutra,
            AvyayaKatiYusmadAsmadSutra, SnaantaSankhyaSutra, GunavacanamCaSutra,
            KrtyasCaSutra, KaranaAdhikaranaLyutSutra, SarvadiniSarvanamaniSutra,
        )
        commonGenderRules.forEachIndexed { index, sutra ->
            assertSame(sutra, LinganushasanamRegistry.require((index + 173).toString()))
        }
    }
}
