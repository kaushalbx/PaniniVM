package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Mayūravyamsakādi irregular Tatpuruṣa compounds (Pāṇini 2.1.72 मयूरव्यंसकादयश्च).
 */
class MayuravyamsakadiSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test mayuravyamsakah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("मयूर", Vibhakti.PRATHAMA),
                SamasaPada("व्यंसक", Vibhakti.PRATHAMA),
            ),
            SamasaType.MAYURAVYAMSAKADI,
        )
        assertEquals("मयूरव्यंसकः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.1.72" || it.sutra == "2.1.106" })
    }

    @Test
    fun `test uccavacam derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("उच्च", Vibhakti.PRATHAMA),
                SamasaPada("अवच", Vibhakti.PRATHAMA),
            ),
            SamasaType.MAYURAVYAMSAKADI,
        )
        assertEquals("उच्चावचम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.1.72" || it.sutra == "2.1.106" })
    }
}
