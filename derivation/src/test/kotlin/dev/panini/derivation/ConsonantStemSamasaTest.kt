package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Consonant-ending stem transformations (Na-lopa, etc.) in Samāsa.
 */
class ConsonantStemSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test rajan stem drops final n in Shashthi Tatpurusa`() {
        val result = engine.derive(
            listOf(
                SamasaPada("राजन्", Vibhakti.SASTHI),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("राजपुरुषः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "8.2.7" })
    }

    @Test
    fun `test atman stem drops final n in Shashthi Tatpurusa`() {
        val result = engine.derive(
            listOf(
                SamasaPada("आत्मन्", Vibhakti.SASTHI),
                SamasaPada("ज्ञान", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("आत्मज्ञानम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "8.2.7" })
    }

    @Test
    fun `test karman stem drops final n in Shashthi Tatpurusa`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कर्मन्", Vibhakti.SASTHI),
                SamasaPada("फल", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("कर्मफलम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "8.2.7" })
    }

    @Test
    fun `test svamin stem drops final n in Shashthi Tatpurusa`() {
        val result = engine.derive(
            listOf(
                SamasaPada("स्वामिन्", Vibhakti.SASTHI),
                SamasaPada("भक्ति", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("स्वामिभक्तिः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "8.2.7" })
    }

    @Test
    fun `test hastin stem drops final n in Shashthi Tatpurusa`() {
        val result = engine.derive(
            listOf(
                SamasaPada("हस्तिन्", Vibhakti.SASTHI),
                SamasaPada("दन्त", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("हस्तिदन्तः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "8.2.7" })
    }
}
