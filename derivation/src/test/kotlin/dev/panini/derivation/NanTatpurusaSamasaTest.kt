package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Nañ Tatpuruṣa compounds (Pāṇini 2.2.6 नञ्, 6.3.73 नलोपो नञः, 6.3.74 तस्मान्नुडचि).
 */
class NanTatpurusaSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test abrahmanah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("ब्राह्मण", Vibhakti.PRATHAMA),
            ),
            SamasaType.NAN_TATPURUSA,
        )
        assertEquals("अब्राह्मणः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.6" })
    }

    @Test
    fun `test adharmah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("धर्म", Vibhakti.PRATHAMA),
            ),
            SamasaType.NAN_TATPURUSA,
        )
        assertEquals("अधर्मः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.6" })
    }

    @Test
    fun `test ajnanam derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("ज्ञान", Vibhakti.PRATHAMA),
            ),
            SamasaType.NAN_TATPURUSA,
        )
        assertEquals("अज्ञानम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.6" })
    }

    @Test
    fun `test anasvah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("अश्व", Vibhakti.PRATHAMA),
            ),
            SamasaType.NAN_TATPURUSA,
        )
        assertEquals("अनश्वः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.6" })
    }

    @Test
    fun `test anisvarah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("ईश्वर", Vibhakti.PRATHAMA),
            ),
            SamasaType.NAN_TATPURUSA,
        )
        assertEquals("अनीश्वरः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.6" })
    }

    @Test
    fun `test anekah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("एक", Vibhakti.PRATHAMA),
            ),
            SamasaType.NAN_TATPURUSA,
        )
        assertEquals("अनेकः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.6" })
    }
}
