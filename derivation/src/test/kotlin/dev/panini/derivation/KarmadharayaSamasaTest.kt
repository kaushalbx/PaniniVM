package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Karmadhāraya compound derivations (Pāṇini 2.1.57 - 2.1.72).
 */
class KarmadharayaSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test mahanavami karmadharaya derivation via 6 3 100`() {
        val result = engine.derive(
            listOf(
                SamasaPada("महत्", Vibhakti.PRATHAMA),
                SamasaPada("नवमी", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("महानवमी", result.final.surface)
        assertTrue(result.applications.isNotEmpty())
    }

    @Test
    fun `test nilotdalam karmadharaya derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("नील", Vibhakti.PRATHAMA),
                SamasaPada("उत्पल", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("नीलोत्पलः", result.final.surface)
        assertTrue(result.applications.isNotEmpty())
    }
}
