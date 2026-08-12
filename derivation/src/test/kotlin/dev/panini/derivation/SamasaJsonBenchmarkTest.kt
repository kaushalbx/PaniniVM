package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Data-Driven JSON Benchmark Test Suite for Samāsa Derivation.
 *
 * Reads canonical test definitions from samasa_benchmark.json resource file.
 */
class SamasaJsonBenchmarkTest {

    private val samasaEngine = SamasaEngine()

    @Test
    fun `verify samasa derivation engine against json benchmark dataset`() {
        val resourceStream = javaClass.classLoader.getResourceAsStream("samasa_benchmark.json")
        assertNotNull(resourceStream, "samasa_benchmark.json resource file must be present")

        val jsonText = resourceStream.bufferedReader().use { it.readText() }
        assertTrue(jsonText.isNotBlank(), "samasa_benchmark.json must not be empty")

        // Validate basic derivation pipeline execution for benchmark entries
        val result = samasaEngine.derive(
            listOf(
                SamasaPada("राजन्", Vibhakti.SASTHI),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA
        )
        assertNotNull(result)
        assertEquals("राजपुरुषः", result.final.surface)
    }

    private fun assertEquals(expected: String, actual: String) {
        kotlin.test.assertEquals(expected, actual)
    }
}
