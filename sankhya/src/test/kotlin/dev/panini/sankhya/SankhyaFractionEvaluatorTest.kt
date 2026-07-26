package dev.panini.sankhya

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SankhyaFractionEvaluatorTest {

    private val evaluator = SankhyaEvaluator()

    @Test
    fun `evaluates primitive fraction Ardha to 1 by 2`() {
        val expr = evaluator.evaluateStems(listOf("अर्ध"))
        val fraction = assertIs<SankhyaExpression.RationalFraction>(expr)
        assertEquals(1L, fraction.numerator)
        assertEquals(2L, fraction.denominator)
    }

    @Test
    fun `evaluates numerator-denominator fraction Tri Pada to 3 by 4`() {
        val expr = evaluator.evaluateStems(listOf("त्रि", "पाद"))
        val fraction = assertIs<SankhyaExpression.RationalFraction>(expr)
        assertEquals(3L, fraction.numerator)
        assertEquals(4L, fraction.denominator)
    }

    @Test
    fun `evaluates mixed rational prefix Saardha Dvi to 5 by 2`() {
        val expr = evaluator.evaluateStems(listOf("सार्ध", "द्वि"))
        val fraction = assertIs<SankhyaExpression.RationalFraction>(expr)
        assertEquals(5L, fraction.numerator)
        assertEquals(2L, fraction.denominator)
    }

    @Test
    fun `evaluates mixed rational prefix Sapaada Tri to 13 by 4`() {
        val expr = evaluator.evaluateStems(listOf("सपाद", "त्रि"))
        val fraction = assertIs<SankhyaExpression.RationalFraction>(expr)
        assertEquals(13L, fraction.numerator)
        assertEquals(4L, fraction.denominator)
    }

    @Test
    fun `evaluates mixed rational prefix Paadona Tri to 11 by 4`() {
        val expr = evaluator.evaluateStems(listOf("पादोन", "त्रि"))
        val fraction = assertIs<SankhyaExpression.RationalFraction>(expr)
        assertEquals(11L, fraction.numerator)
        assertEquals(4L, fraction.denominator)
    }
}
