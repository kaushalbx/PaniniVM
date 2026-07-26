package dev.panini.sankhya

import kotlin.test.Test
import kotlin.test.assertEquals

class SankhyaMathEvaluatorTest {

    private val evaluator = SankhyaEvaluator()

    @Test
    fun `evaluates segmented multiplication Dvi Gunita Sata to 200`() {
        val expr = evaluator.evaluateStems(listOf("द्वि", "गुणित", "शत"))
        assertEquals(200L, expr.value)
    }

    @Test
    fun `evaluates segmented multiplication Ashta Gunita Sata to 800`() {
        val expr = evaluator.evaluateStems(listOf("अष्ट", "गुणित", "शत"))
        assertEquals(800L, expr.value)
    }

    @Test
    fun `evaluates segmented square Varga Krita Pancha to 25`() {
        val expr = evaluator.evaluateStems(listOf("वर्ग", "कृत", "पञ्च"))
        assertEquals(25L, expr.value)
    }

    @Test
    fun `evaluates segmented square root Moola Shodasha to 4`() {
        val expr = evaluator.evaluateStems(listOf("मूल", "षोडश"))
        assertEquals(4L, expr.value)
    }

    @Test
    fun `evaluates segmented Abhi Adhika expression Dvi Abhi Adhika Sata to 102`() {
        val expr = evaluator.evaluateStems(listOf("द्वि", "अभि", "अधिक", "शत"))
        assertEquals(102L, expr.value)
    }

    @Test
    fun `evaluates segmented Sa Hita expression Dvi Sa Hita Sata to 102`() {
        val expr = evaluator.evaluateStems(listOf("द्वि", "स", "हित", "शत"))
        assertEquals(102L, expr.value)
    }

    @Test
    fun `evaluates segmented Sam Yuta expression Dvi Sam Yuta Sata to 102`() {
        val expr = evaluator.evaluateStems(listOf("द्वि", "सम्", "युत", "शत"))
        assertEquals(102L, expr.value)
    }
}
