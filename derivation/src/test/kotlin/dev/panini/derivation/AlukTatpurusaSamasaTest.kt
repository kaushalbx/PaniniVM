package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Aluk Tatpuruṣa compound derivations (Pāṇini 6.3.1 - 6.3.24).
 */
class AlukTatpurusaSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test atmanepadam aluk tatpurusa derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("आत्मन्", Vibhakti.SASTHI),
                SamasaPada("पद", Vibhakti.PRATHAMA),
            ),
            SamasaType.ALUK_TATPURUSA,
        )
        assertEquals("आत्मनेपदम्", result.final.surface)
        assertTrue(result.applications.isNotEmpty())
    }

    @Test
    fun `test parasmaipadam aluk tatpurusa derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("परस्मै", Vibhakti.CHATURTHI),
                SamasaPada("पद", Vibhakti.PRATHAMA),
            ),
            SamasaType.ALUK_TATPURUSA,
        )
        assertEquals("परस्मैपदम्", result.final.surface)
        assertTrue(result.applications.isNotEmpty())
    }
}
