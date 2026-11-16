package dev.panini.derivation

import dev.panini.core.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 2.4.77 (all its roots), 3.4.110 आतः, 6.1.96 उस्यपदान्तात्, and 6.1.68 not taking a root-final आ as आप्. */
class SicLukJusTest {
    private val engine = TingantaEngine()
    private fun form(root: String, lakara: Lakara, vacana: Vacana, pada: PadaType = PadaType.PARASMAIPADA) =
        engine.derive(TingantaDerivationRequest(root, vacana, Purusha.PRATHAMA, lakara, pada = pada))

    @Test fun `ghu and stha drop sic in parasmaipada luṅ`() {
        assertEquals("अदात्", form("दा", Lakara.LUNG, Vacana.EKAVACANA).final.surface)
        assertEquals("अस्थात्", form("स्था", Lakara.LUNG, Vacana.EKAVACANA).final.surface)
        assertEquals("अभूत्", form("भू", Lakara.LUNG, Vacana.EKAVACANA).final.surface)
    }

    @Test fun `aa-final roots take jus and pararupa in luṅ plural`() {
        val adu = form("दा", Lakara.LUNG, Vacana.BAHUVACANA)
        assertEquals("अदुः", adu.final.surface)
        assertTrue(adu.applications.any { it.sutra == "3.4.110" } && adu.applications.any { it.sutra == "6.1.96" })
        assertEquals("अस्थुः", form("स्था", Lakara.LUNG, Vacana.BAHUVACANA).final.surface)
        // भू is not आ-final: 3.4.110 does not apply, अन्ति gives अभूवन्.
        assertEquals("अभूवन्", form("भू", Lakara.LUNG, Vacana.BAHUVACANA).final.surface)
    }

    @Test fun `root-final aa keeps the ending of laṅ`() {
        assertEquals("अयात्", form("या", Lakara.LANG, Vacana.EKAVACANA).final.surface)
    }
}
