package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UnspecifiedPadaSandhiTest {
    private val engine = SandhiEngine()

    @Test
    fun `string padas retain both sakalya choices with traces`() {
        for ((left, right, outputs) in listOf(
            Triple("हरे", "एहि", setOf("हरयेहि", "हर एहि")),
            Triple("विष्णो", "इह", setOf("विष्णविह", "विष्ण इह")),
            Triple("माले", "इति", setOf("मालयिति", "माल इति")),
        )) {
            val results = engine.joinAll(left, right)
            assertEquals(outputs, results.map(engine::render).toSet())
            assertTrue(results.all { it.initial.terms.all { term -> term.formedPadaRupa == Rupa() } })
            assertTrue(results.all { it.applications.any { a -> a.sutra == "6.1.78" } })
            assertTrue(results.any { it.applications.any { a -> a.sutra == "8.3.19" } })
            for (result in results.filter { it.applications.any { a -> a.sutra == "8.3.19" } }) {
                val lopaIndex = result.applications.indexOfFirst { it.sutra == "8.3.19" }
                assertFalse(result.applications.drop(lopaIndex + 1).any { it.sutra in setOf("6.1.77", "6.1.78", "6.1.87", "6.1.88", "6.1.101") })
                assertEquals(listOf("sandhi_left", "sandhi_right"), result.final.terms.map { it.id })
            }
        }
    }
}
