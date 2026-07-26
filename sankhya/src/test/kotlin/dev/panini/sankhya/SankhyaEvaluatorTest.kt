package dev.panini.sankhya

import kotlin.test.Test
import kotlin.test.assertEquals

class SankhyaEvaluatorTest {

    private val evaluator = SankhyaEvaluator()

    @Test
    fun `evaluates primitives`() {
        assertEquals(1L, evaluator.evaluateStems(listOf("एक")).value)
        assertEquals(2L, evaluator.evaluateStems(listOf("द्वि")).value)
        assertEquals(6L, evaluator.evaluateStems(listOf("षष्")).value)
        assertEquals(10L, evaluator.evaluateStems(listOf("दशन्")).value)
        assertEquals(20L, evaluator.evaluateStems(listOf("विंशति")).value)
        assertEquals(100L, evaluator.evaluateStems(listOf("शत")).value)
    }

    @Test
    fun `evaluates additive unit plus ten`() {
        assertEquals(13L, evaluator.evaluateStems(listOf("त्रि", "दश")).value)
        assertEquals(22L, evaluator.evaluateStems(listOf("द्वि", "विंशति")).value)
        assertEquals(25L, evaluator.evaluateStems(listOf("पञ्च", "विंशति")).value)
    }

    @Test
    fun `evaluates multiplicative coefficient times magnitude`() {
        assertEquals(200L, evaluator.evaluateStems(listOf("द्वि", "शत")).value)
        assertEquals(5000L, evaluator.evaluateStems(listOf("पञ्च", "सहस्र")).value)
    }

    @Test
    fun `evaluates adhika expressions`() {
        assertEquals(101L, evaluator.evaluateStems(listOf("एक", "अधिक", "शत")).value)
        assertEquals(122L, evaluator.evaluateStems(listOf("द्वि", "विंशति", "अधिक", "शत")).value)
    }

    @Test
    fun `evaluates una expressions`() {
        assertEquals(19L, evaluator.evaluateStems(listOf("एक", "ऊन", "विंशति")).value)
        assertEquals(98L, evaluator.evaluateStems(listOf("द्वि", "न्यून", "शत")).value)
    }
}
