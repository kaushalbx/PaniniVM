package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Karmadhāraya compound derivations (Pāṇini 2.1.57 - 2.1.72 & 6.3.46).
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

    @Test
    fun `test mahapurusah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("महत्", Vibhakti.PRATHAMA),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("महापुरुषः", result.final.surface)
        assertTrue(result.applications.isNotEmpty())
    }

    @Test
    fun `test mahadevah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("महान्", Vibhakti.PRATHAMA),
                SamasaPada("देव", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("महादेवः", result.final.surface)
        assertTrue(result.applications.isNotEmpty())
    }

    @Test
    fun `test mahatma derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("महत्", Vibhakti.PRATHAMA),
                SamasaPada("आत्मन्", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("महात्मा", result.final.surface)
        assertTrue(result.applications.isNotEmpty())
    }
}
