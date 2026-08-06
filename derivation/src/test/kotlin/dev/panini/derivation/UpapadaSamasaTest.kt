package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Upapada Tatpuruṣa compounds (Pāṇini 2.2.19 उपपदमतिङ्).
 */
class UpapadaSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test kumbhakarah derivation`() {
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
    fun `test samagah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("साम", Vibhakti.DVITIYA),
                SamasaPada("ग", Vibhakti.PRATHAMA),
            ),
            SamasaType.UPAPADA_TATPURUSA,
        )
        assertEquals("सामगः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.19" })
    }

    @Test
    fun `test dharmajnah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("धर्म", Vibhakti.DVITIYA),
                SamasaPada("ज्ञ", Vibhakti.PRATHAMA),
            ),
            SamasaType.UPAPADA_TATPURUSA,
        )
        assertEquals("धर्मज्ञः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.19" })
    }

    @Test
    fun `test sukhadah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("सुख", Vibhakti.DVITIYA),
                SamasaPada("द", Vibhakti.PRATHAMA),
            ),
            SamasaType.UPAPADA_TATPURUSA,
        )
        assertEquals("सुखदः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.19" })
    }

    @Test
    fun `test mahidharah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("मही", Vibhakti.DVITIYA),
                SamasaPada("धर", Vibhakti.PRATHAMA),
            ),
            SamasaType.UPAPADA_TATPURUSA,
        )
        assertEquals("महीधरः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.19" })
    }

    @Test
    fun `test grihasthah derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("गृह", Vibhakti.SAPTAMI),
                SamasaPada("स्थ", Vibhakti.PRATHAMA),
            ),
            SamasaType.UPAPADA_TATPURUSA,
        )
        assertEquals("गृहस्थः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.19" })
    }
}
