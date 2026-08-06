package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Nañ Tatpuruṣa compound derivations (Pāṇini 2.2.6).
 */
class NanTatpurusaSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test abrahmana consonant initial nan tatpurusa`() {
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
    fun `test anasva vowel initial nan tatpurusa`() {
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
    fun `test adharma consonant initial nan tatpurusa`() {
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
}
