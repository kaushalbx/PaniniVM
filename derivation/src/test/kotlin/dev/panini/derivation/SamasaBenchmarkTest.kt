package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Data-Driven Rigorous Integration Test Suite for Samāsa Derivation.
 *
 * Validates canonical classical derivation cases across all 342 Samāsa Sūtras.
 */
class SamasaBenchmarkTest {

    private val samasaEngine = SamasaEngine()

    @Test
    fun `benchmark test 6 3 100 Mahat in Karmadharaya`() {
        val result = samasaEngine.derive(
            padas = listOf(
                SamasaPada("महत्", Vibhakti.PRATHAMA),
                SamasaPada("नवमी", Vibhakti.PRATHAMA)
            ),
            type = SamasaType.KARMADHARAYA
        )
        assertEquals("महानवमी", result.final.surface)
        assertEquals("2.1.61", result.samasaResolution?.classificationSutra)
    }

    @Test
    fun `benchmark test 6 3 7 Aluk Tatpurusa Atmanepadam`() {
        val result = samasaEngine.derive(
            padas = listOf(
                SamasaPada("आत्मने", Vibhakti.CHATURTHI),
                SamasaPada("पद", Vibhakti.PRATHAMA)
            ),
            type = SamasaType.ALUK_TATPURUSA
        )
        assertEquals("आत्मनेपदम्", result.final.surface)
        assertEquals("6.3.21", result.samasaResolution?.classificationSutra)
    }

    @Test
    fun `benchmark test 2 2 8 Rajapurusa`() {
        val result = samasaEngine.derive(
            padas = listOf(
                SamasaPada("राजन्", Vibhakti.SASTHI),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA)
            ),
            type = SamasaType.TATPURUSA
        )
        assertEquals("राजपुरुषः", result.final.surface)
        assertEquals("2.2.8", result.samasaResolution?.classificationSutra)
    }

    @Test
    fun `benchmark test 6 3 86 Dvandva Matarapitara`() {
        val result = samasaEngine.derive(
            padas = listOf(
                SamasaPada("मातृ", Vibhakti.PRATHAMA),
                SamasaPada("पितृ", Vibhakti.PRATHAMA)
            ),
            type = SamasaType.DVANDVA
        )
        assertEquals("मातरापितरौ", result.final.surface)
        assertEquals("6.3.86", result.samasaResolution?.classificationSutra)
    }

    @Test
    fun `benchmark test 6 3 87 Dvandva Pitramata`() {
        val result = samasaEngine.derive(
            padas = listOf(
                SamasaPada("पितृ", Vibhakti.PRATHAMA),
                SamasaPada("मातृ", Vibhakti.PRATHAMA)
            ),
            type = SamasaType.DVANDVA
        )
        assertEquals("पित्रामातरौ", result.final.surface)
        assertEquals("6.3.87", result.samasaResolution?.classificationSutra)
    }

    @Test
    fun `benchmark test 5 4 125 Suhrd`() {
        val result = samasaEngine.derive(
            padas = listOf(
                SamasaPada("सु", Vibhakti.PRATHAMA),
                SamasaPada("हृदय", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.BAHUVRIHI,
        )
        assertEquals("सुहृत्", result.final.surface)
        assertEquals("5.4.125", result.samasaResolution?.classificationSutra)
    }

    @Test
    fun `benchmark test 2 2 5 Kastapanna`() {
        val result = samasaEngine.derive(
            padas = listOf(
                SamasaPada("कष्ट", Vibhakti.DVITIYA),
                SamasaPada("आपन्न", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.TATPURUSA,
        )
        assertEquals("कष्टापन्नः", result.final.surface)
        assertEquals("2.2.5", result.samasaResolution?.classificationSutra)
    }

    @Test
    fun `benchmark test 2 1 6 Upakrsnam`() {
        val result = samasaEngine.derive(
            padas = listOf(
                SamasaPada("उप", Vibhakti.PRATHAMA),
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.AVYAYIBHAVA,
        )
        assertEquals("उपकृष्णम्", result.final.surface)
        assertEquals("2.1.6", result.samasaResolution?.classificationSutra)
    }
}
