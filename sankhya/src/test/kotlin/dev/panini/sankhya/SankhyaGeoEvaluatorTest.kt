package dev.panini.sankhya

import kotlin.test.Test
import kotlin.test.assertEquals

class SankhyaGeoEvaluatorTest {

    private val evaluator = SankhyaEvaluator()

    @Test
    fun `evaluates Jya 90 to 1`() {
        val expr = evaluator.evaluateStems(listOf("ज्या", "नवति"))
        assertEquals(1L, expr.value)
    }

    @Test
    fun `evaluates Kotijya 0 to 1`() {
        val expr = evaluator.evaluateStems(listOf("कोटि", "ज्या", "शून्य"))
        assertEquals(1L, expr.value)
    }

    @Test
    fun `evaluates Karna 3 4 to 5`() {
        val expr = evaluator.evaluateStems(listOf("कर्ण", "त्रि", "चतुर्"))
        assertEquals(5L, expr.value)
    }

    @Test
    fun `evaluates Paridhi 10 to 62`() {
        val expr = evaluator.evaluateStems(listOf("परिधि", "दशन्"))
        assertEquals(62L, expr.value)
    }
}
