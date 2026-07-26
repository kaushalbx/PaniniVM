package dev.panini.execution

import dev.panini.core.Karaka
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
    fun `factory does not guess Sankhya from surface text`() {
        val created = SanskritValue.of("पञ्च")
        assertIs<SanskritValue.Shabda>(created)
    }

    @Test
    fun `ExecutionContext resolves typed SanskritValue list`() {
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(10, "दश"),
                    ExecutionExpression.sankhya(2, "द्वि"),
                )
            )
        )

        val resolved = context.resolveValues(requireNotNull(context.bindings[Karaka.KARMAN]))
        assertEquals(2, resolved.size)
        assertEquals(10L, assertIs<SanskritValue.Sankhya>(resolved[0]).value)
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(resolved[1]).value)
    }

    @Test
    fun `ValueEnvironment merge preserves types and gives precedence to the newer environment`() {
        val conversation = ValueEnvironment(
            mapOf("फल" to SanskritValue.Shabda("पुरातनम्")),
        )
        val host = ValueEnvironment(
            mapOf("फल" to SanskritValue.Sankhya(5, "पञ्च")),
        )

        val merged = conversation.mergedWith(host)

        assertEquals(5, assertIs<SanskritValue.Sankhya>(merged.values.getValue("फल")).value)
        assertEquals("पञ्च", merged.displayValues().getValue("फल"))
        assertTrue(ExecutionSamjna.SANKHYA in merged.samjnas().getValue("फल"))
    }
}
