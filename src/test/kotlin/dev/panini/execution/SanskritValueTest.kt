package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SanskritValueTest {
    @Test
    fun `Sankhya holds integer value and canonical word`() {
        val sankhya = SanskritValue.Sankhya(10L, "दश")
        assertEquals(10L, sankhya.value)
        assertEquals("दश", sankhya.word)
        assertEquals("दश", sankhya.toDisplayText())
        assertTrue(ExecutionSamjna.SANKHYA in sankhya.samjnas)
    }

    @Test
    fun `Shabda holds text and custom samjnas`() {
        val shabda = SanskritValue.Shabda("रामः", setOf(ExecutionSamjna.SHABDA, ExecutionSamjna.NAMAPADA))
        assertEquals("रामः", shabda.text)
        assertEquals("रामः", shabda.toDisplayText())
        assertTrue(ExecutionSamjna.NAMAPADA in shabda.samjnas)
    }

    @Test
    fun `Gana groups multiple values`() {
        val element1 = SanskritValue.Sankhya(1L, "एक")
        val element2 = SanskritValue.Sankhya(2L, "द्वि")
        val gana = SanskritValue.Gana(listOf(element1, element2))

        assertEquals("एक द्वि", gana.toDisplayText())
        assertTrue(ExecutionSamjna.GANA in gana.samjnas)
        assertTrue(ExecutionSamjna.SANKHYA in gana.samjnas)
    }

    @Test
    fun `Satya renders Sanskrit boolean text`() {
        val trueVal = SanskritValue.Satya(true)
        val falseVal = SanskritValue.Satya(false)

        assertEquals("सत्यम्", trueVal.toDisplayText())
        assertEquals("असत्यम्", falseVal.toDisplayText())
        assertTrue(ExecutionSamjna.SATYA in trueVal.samjnas)
    }

    @Test
    fun `factory creates Sankhya automatically for number words`() {
        val created = SanskritValue.of("पञ्च")
        val sankhya = assertIs<SanskritValue.Sankhya>(created)
        assertEquals(5L, sankhya.value)
    }

    @Test
    fun `ExecutionContext resolves typed SanskritValue list`() {
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("दश", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            )
        )

        val resolved = context.resolveValues(requireNotNull(context.bindings[Karaka.KARMAN]))
        assertEquals(2, resolved.size)
        assertEquals(10L, assertIs<SanskritValue.Sankhya>(resolved[0]).value)
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(resolved[1]).value)
    }
}
