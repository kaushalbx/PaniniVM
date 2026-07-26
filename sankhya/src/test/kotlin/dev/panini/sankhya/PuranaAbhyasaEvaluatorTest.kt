package dev.panini.sankhya

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PuranaAbhyasaEvaluatorTest {

    private val evaluator = SankhyaEvaluator()

    @Test
    fun `evaluates standalone ordinals`() {
        val expr = evaluator.evaluateStems(listOf("द्वि", "तीय"))
        val purana = assertIs<SankhyaExpression.Purana>(expr)
        assertEquals(2L, purana.value)
    }

    @Test
    fun `evaluates segmented ordinals with suffix`() {
        val expr = evaluator.evaluateStems(listOf("द्वि", "विंशति", "तम"))
        val purana = assertIs<SankhyaExpression.Purana>(expr)
        assertEquals(22L, purana.value)
    }

    @Test
    fun `evaluates standalone frequency numerals`() {
        val expr = evaluator.evaluateStems(listOf("द्विः"))
        val freq = assertIs<SankhyaExpression.Frequency>(expr)
        assertEquals(2L, freq.value)
    }

    @Test
    fun `evaluates segmented frequency with kritvas`() {
        val expr = evaluator.evaluateStems(listOf("पञ्च", "कृत्वः"))
        val freq = assertIs<SankhyaExpression.Frequency>(expr)
        assertEquals(5L, freq.value)
    }

    @Test
    fun `evaluates segmented distribution with dha`() {
        val expr = evaluator.evaluateStems(listOf("त्रि", "धा"))
        val dist = assertIs<SankhyaExpression.Distribution>(expr)
        assertEquals(3L, dist.value)
    }
}
