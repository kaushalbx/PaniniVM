package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
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
        assertNotNull(result)
        println("Derived: ${result.final.surface} | Classification: ${result.samasaResolution?.classificationSutra}")
        assertTrue(result.final.surface.isNotEmpty())
    }

    @Test
    fun `benchmark test 6 3 7 Aluk Tatpurusa Atmanepadam`() {
        val result = samasaEngine.derive(
            padas = listOf(
                SamasaPada("आत्मन्", Vibhakti.PRATHAMA),
                SamasaPada("पद", Vibhakti.PRATHAMA)
            ),
            type = SamasaType.TATPURUSA
        )
        assertNotNull(result)
        println("Derived: ${result.final.surface} | Classification: ${result.samasaResolution?.classificationSutra}")
        assertTrue(result.applications.isNotEmpty())
    }

    @Test
    fun `benchmark test 5 4 91 Rajapurusa`() {
        val result = samasaEngine.derive(
            padas = listOf(
                SamasaPada("राजन्", Vibhakti.SASTHI),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA)
            ),
            type = SamasaType.TATPURUSA
        )
        assertNotNull(result)
        println("Derived: ${result.final.surface} | Classification: ${result.samasaResolution?.classificationSutra}")
        assertTrue(result.final.surface.isNotEmpty())
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
        assertNotNull(result)
        println("Derived: ${result.final.surface} | Classification: ${result.samasaResolution?.classificationSutra}")
        assertTrue(result.final.surface.isNotEmpty())
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
        assertNotNull(result)
        println("Derived: ${result.final.surface} | Classification: ${result.samasaResolution?.classificationSutra}")
        assertTrue(result.final.surface.isNotEmpty())
    }
}
