package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

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
    }
}
