package dev.panini.sutra

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada1.AdasoMatSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.NgitiHrasvashCaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.SvadisvAsarvanamasthaneSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.IdudbhyamSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.YadapahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.AtoYukCinKrtohSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.SarvanamnahSyadHrasvasCaSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.VibhasaDvitiyatritiyabhyamSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.SvamorNapumsakatSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.AdasAuSuLopahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.MaparyantasyaSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.TvahauSauSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.YuyavayauJasiSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.SheseLopahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.NgePrathamayorAmSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.SavAnaduhahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.AtashCaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.RtaUtSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.PrathamayohPurvaSavarnahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.TasmacChasoNahPumsiSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.AdAjadinamSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.NopadhayahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.AnNadyahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.EtaIdBahuvacaneSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.*
import dev.panini.ashtadhyayi.adhyaya8.pada4.NaPadantatTorAnamSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.HaloYamamYamiLopahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.YaroAnunasikeAnunasikoVaSutra
import kotlin.test.*

class SandhiMigrationRegistryTest {
    private val newlyAdded: List<Sutra<*, *>> = listOf(
        AdasoMatSutra, AtashCaSutra, AdAjadinamSutra, EtaIdBahuvacaneSutra, AnNadyahSutra,
        NgitiHrasvashCaSutra, IdudbhyamSutra, YadapahSutra, AtoYukCinKrtohSutra,
        SarvanamnahSyadHrasvasCaSutra,
        HaloYamamYamiLopahSutra,
        VibhasaDvitiyatritiyabhyamSutra,
        RtaUtSutra,
        SvamorNapumsakatSutra,
        SvadisvAsarvanamasthaneSutra,
        NopadhayahSutra,
        SavAnaduhahSutra,
        OtoGargyasyaSutra,
        AdasAuSuLopahSutra,
        MaparyantasyaSutra, TvahauSauSutra, YuyavayauJasiSutra, SheseLopahSutra, NgePrathamayorAmSutra,
        HeMapareVaSutra, NapareNahSutra, KupvohKapauCaSutra, DahSiDhutSutra,
        NashCaSutra, AtranunasikahPurvasyaTuVaSutra, AnunasikatParoAnusvarahSutra,
        NashChavyAprashanSutra, NgamoHrasvadAciNgamunNityamSutra, NgnohKuktukShariSutra,
        SharpareVisarjaniyahSutra, VaShariSutra, NaPadantatTorAnamSutra, YaroAnunasikeAnunasikoVaSutra,
    )

    @Test fun `all newly added canonical rules are registered exactly once and executable`() {
        for (rule in newlyAdded) {
            assertSame(rule, Ashtadhyayi.registry.require(rule.number), rule.number)
            assertEquals(1, Ashtadhyayi.cataloguedSutras.count { it.number == rule.number }, rule.number)
            assertTrue(Ashtadhyayi.executableSutras.any { it === rule }, rule.number)
            assertTrue(Ashtadhyayi.runtimeSutras.any { it === rule }, rule.number)
        }
    }

    @Test fun `canonical sas replacements stay registered and synthetic shortcuts stay excluded`() {
        for (rule in listOf(PrathamayohPurvaSavarnahSutra, TasmacChasoNahPumsiSutra)) {
            assertSame(rule, Ashtadhyayi.registry.require(rule.number))
            assertTrue(Ashtadhyayi.executableSutras.any { it === rule })
        }
        for (number in (121..158).map { "7.3.$it" }) {
            assertNull(Ashtadhyayi.registry.get(number), number)
        }
    }

    @Test fun `pada seven three has no invented out of range identities`() {
        assertTrue(Ashtadhyayi.registry.sutras.none {
            it.chapter == 7 && it.pada == 3 && it.number.substringAfterLast('.').toInt() > 120
        })
        Ashtadhyayi.registry.get("7.3.117")?.let { assertEquals("इदुद्भ्याम्", it.text) }
        Ashtadhyayi.registry.get("7.3.118")?.let { assertEquals("औत्", it.text) }
    }

    @Test fun `removed output lookup and combined word shortcuts are not executable`() {
        for (number in listOf("7.1.99", "7.2.86", "3.3.56", "6.3.76")) {
            assertNull(Ashtadhyayi.registry.get(number), number)
        }
    }
}
