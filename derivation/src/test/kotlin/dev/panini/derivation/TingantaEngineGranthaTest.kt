package dev.panini.derivation

import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.Purusha
import dev.panini.core.Vacana
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TingantaEngineGranthaTest {

    private val engine = TingantaEngine()

    @Test
    fun `test lat lakara verb derivation for bhu dhatu`() {
        val bhavati = engine.derive(TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LAT, pada = PadaType.PARASMAIPADA))
        assertEquals("भवति", bhavati.final.surface)
        assertTrue(bhavati.applications.any { it.sutra == "3.4.78" })
        assertTrue(bhavati.applications.any { it.sutra == "3.1.68" })
        assertTrue(bhavati.applications.any { it.sutra == "1.3.8" })
        assertTrue(bhavati.applications.any { it.sutra == "1.3.3" })
        assertTrue(bhavati.applications.any { it.sutra == "1.3.9" })
        assertTrue(bhavati.final.droppedTerms.any {
            it.upadesha == "शप्" && it.createdBySutra == "3.1.68"
        })

        val bhavatah = engine.derive(TingantaDerivationRequest("भू", Vacana.DVIVACANA, Purusha.PRATHAMA, Lakara.LAT, pada = PadaType.PARASMAIPADA))
        assertEquals("भवतः", bhavatah.final.surface)

        val bhavanti = engine.derive(TingantaDerivationRequest("भू", Vacana.BAHUVACANA, Purusha.PRATHAMA, Lakara.LAT, pada = PadaType.PARASMAIPADA))
        assertEquals("भवन्ति", bhavanti.final.surface)
    }

    @Test
    fun `test lat lakara verb derivation for div dhatu`() {
        val divyati = engine.derive(TingantaDerivationRequest("दिव्", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LAT, pada = PadaType.PARASMAIPADA))
        assertEquals("दीव्यति", divyati.final.surface)
        assertTrue(divyati.applications.any { it.sutra == "3.1.69" })
    }

    @Test
    fun `test lat lakara verb derivation for kr dhatu`() {
        val karoti = engine.derive(TingantaDerivationRequest("कृ", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LAT, pada = PadaType.PARASMAIPADA))
        assertEquals("कृणोति", karoti.final.surface)
    }

    @Test
    fun `test lrt lakara future tense verb derivation for bhu dhatu`() {
        val bhavisyati = engine.derive(TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LRT, pada = PadaType.PARASMAIPADA))
        assertEquals("भविष्यति", bhavisyati.final.surface)
    }

    @Test
    fun `test lang lakara past tense verb derivation for bhu dhatu`() {
        val abhavat = engine.derive(TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LANG, pada = PadaType.PARASMAIPADA))
        assertEquals("अभवत्", abhavat.final.surface)
        assertTrue(abhavat.applications.any { it.sutra == "3.4.100" })
    }

    @Test
    fun `test lot lakara imperative verb derivation for bhu dhatu`() {
        val bhavatu = engine.derive(TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LOT, pada = PadaType.PARASMAIPADA))
        assertEquals("भवतु", bhavatu.final.surface)
        assertTrue(bhavatu.applications.any { it.sutra == "3.4.86" })
    }

    @Test
    fun `test lat lakara atmanepada verb derivation for labh dhatu`() {
        val labhate = engine.derive(TingantaDerivationRequest("लभ्", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LAT, pada = PadaType.ATMANEPADA))
        assertEquals("लभते", labhate.final.surface)
        assertTrue(labhate.applications.any { it.sutra == "3.4.79" })
    }

    @Test
    fun `test sanadi pratyaya support for nic causative`() {
        val supportsNic = engine.supportsSanadi("चुर्", listOf("णिच्"))
        assertTrue(supportsNic)
    }
}
