package dev.panini.execution

import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class AttributeOperandProjectionTest {
    @Test fun `method phrase does not prevent projection of later field arguments`() {
        val original = invocation("गुण + मतुप् + ङस् परीक्षण + टा शब्द + मतुप् + ङस् मूल्य + अम् डुकृञ् + उ + लोट् + सिप् ।")
        val projection = AttributeOperandProjection.project(original, knownOperationStems = setOf("परीक्षण"))
        assertEquals(listOf(listOf("शब्द", "मूल्य")), projection.operands.map { it.access.chain })
        assertTrue(projection.invocation.vakya.padas.any { it === original.vakya.padas[0] })
        assertTrue(projection.invocation.vakya.padas.any { it === original.vakya.padas[1] })
    }

    private fun invocation(source: String) = assertIs<Invocation>(PaniniParser().parse(source).body)

    @Test fun `projection preserves case and negation outside the field phrase`() {
        val original = invocation("गुण + मतुप् + ङस् मूल्य + अम् न एक + अम् च विद् + लोट् + सिप् ।")
        val projection = AttributeOperandProjection.project(original)
        assertEquals(1, projection.operands.size)
        assertEquals(listOf("गुण", "मूल्य"), projection.operands.single().access.chain)
        val replacement = projection.invocation.vakya.padas.first() as SubantaPada
        assertEquals("अम्", replacement.sup.text)
        val negation = original.vakya.padas.filterIsInstance<AvyayaPada>().single { it.function == AvyayaFunction.NISHEDHA }
        assertTrue(projection.invocation.vakya.padas.any { it === negation })
    }

    @Test fun `disconnected field phrases fail instead of falling back to literal operands`() {
        val original = invocation("गुण + मतुप् + ङस् न मूल्य + अम् एक + अम् च युज् + लोट् + सिप् ।")
        assertTrue(assertFailsWith<IllegalArgumentException> {
            AttributeOperandProjection.project(original)
        }.message.orEmpty().contains("intervening words"))
    }
}
