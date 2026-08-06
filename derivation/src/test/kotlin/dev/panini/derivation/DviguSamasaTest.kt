package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Dvigu compound derivations (Pāṇini 2.1.51 - 2.1.52).
 */
class DviguSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test tribhuvanam derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("त्रि", Vibhakti.PRATHAMA),
                SamasaPada("भुवन", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVIGU,
        )
        assertEquals("त्रिभुवनम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.1.52" })
    }

    @Test
    fun `test trilokam derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("त्रि", Vibhakti.PRATHAMA),
                SamasaPada("लोक", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVIGU,
        )
        assertEquals("त्रिलोकम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.1.52" })
    }

    @Test
    fun `test caturyugam derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("चतुर्", Vibhakti.PRATHAMA),
                SamasaPada("युग", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVIGU,
        )
        assertEquals("चतुर्युगम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.1.52" })
    }

    @Test
    fun `test pancapatram derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पञ्च", Vibhakti.PRATHAMA),
                SamasaPada("पात्र", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVIGU,
        )
        assertEquals("पञ्चपात्रम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.1.52" })
    }
}
