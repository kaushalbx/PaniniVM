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
        val bhavati = engine.derive(TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LAT, PadaType.PARASMAIPADA))
        assertEquals("भवति", bhavati.final.terms.last().surface)
        assertTrue(bhavati.applications.any { it.sutra == "3.4.78" })
        assertTrue(bhavati.applications.any { it.sutra == "3.1.68" })

        val bhavatah = engine.derive(TingantaDerivationRequest("भू", Vacana.DVIVACANA, Purusha.PRATHAMA, Lakara.LAT, PadaType.PARASMAIPADA))
        assertEquals("भवतः", bhavatah.final.terms.last().surface)

        val bhavanti = engine.derive(TingantaDerivationRequest("भू", Vacana.BAHUVACANA, Purusha.PRATHAMA, Lakara.LAT, PadaType.PARASMAIPADA))
        assertEquals("भवन्ति", bhavanti.final.terms.last().surface)
    }

    @Test
    fun `test lat lakara verb derivation for div dhatu`() {
        val divyati = engine.derive(TingantaDerivationRequest("दिव्", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LAT, PadaType.PARASMAIPADA))
        assertEquals("दीव्यति", divyati.final.terms.last().surface)
        assertTrue(divyati.applications.any { it.sutra == "3.1.69" })
    }

    @Test
    fun `test lat lakara verb derivation for kr dhatu`() {
        val karoti = engine.derive(TingantaDerivationRequest("कृ", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LAT, PadaType.PARASMAIPADA))
        assertEquals("करोति", karoti.final.terms.last().surface)
        assertTrue(karoti.applications.any { it.sutra == "3.1.79" })
    }
}
