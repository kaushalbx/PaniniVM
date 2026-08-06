package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Data-Driven Rigorous Integration Test Suite for Samāsa Derivation.
 *
 * Runs canonical classical derivation cases across all Pāṇinian Samāsa transformation categories.
 */
class SamasaBenchmarkTest {

    private val samasaEngine = SamasaEngine()

    @Test
    fun `run full canonical samasa benchmark suite`() {
        val failures = mutableListOf<String>()
        for (testCase in SamasaBenchmarkData.canonicalCases) {
            val result = samasaEngine.derive(testCase.padas, testCase.type)
            if (result.final.surface != testCase.expectedSurface) {
                failures.add("[${testCase.id}] ${testCase.description}: Expected '${testCase.expectedSurface}', Got '${result.final.surface}'")
            }
        }
        assertTrue(failures.isEmpty(), "Benchmark failures:\n" + failures.joinToString("\n"))
    }
}
