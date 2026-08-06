package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Upapada Tatpuruṣa compound derivations (Pāṇini 2.2.19 उपपदमतिङ्).
 */
class UpapadaTatpurusaSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test kumbhakara upapada tatpurusa derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कुम्भ", Vibhakti.DVITIYA),
                SamasaPada("कार", Vibhakti.PRATHAMA),
            ),
            SamasaType.UPAPADA_TATPURUSA,
        )
        assertEquals("कुम्भकारः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.19" })
    }

    @Test
    fun `test nagarakara upapada tatpurusa derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("नगर", Vibhakti.DVITIYA),
                SamasaPada("कार", Vibhakti.PRATHAMA),
            ),
            SamasaType.UPAPADA_TATPURUSA,
        )
        assertEquals("नगरकारः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.19" })
    }

    @Test
    fun `test vedavid upapada tatpurusa derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("वेद", Vibhakti.DVITIYA),
                SamasaPada("विद्", Vibhakti.PRATHAMA),
            ),
            SamasaType.UPAPADA_TATPURUSA,
        )
        assertEquals("वेदवित्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.19" })
    }
}
