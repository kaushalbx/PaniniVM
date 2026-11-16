package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShatSandhiTest {
    private val evaluator = SandhiSequenceEvaluator()

    @Test fun `8_4_44 keeps tu after sha while 8_4_40 still palatalises elsewhere`() {
        val protected = evaluator.evaluate(listOf("प्रश्ने", "च")).single()
        assertEquals("प्रश्नेच", protected.surface)
        assertTrue(protected.steps.none { step -> step.applications.any { it.sutra == "8.4.40" } })
        assertEquals("तच्च", evaluator.evaluate(listOf("तत्", "च")).single().surface)
    }
}
