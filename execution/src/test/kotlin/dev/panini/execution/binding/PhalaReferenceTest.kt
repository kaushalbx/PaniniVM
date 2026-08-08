package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PhalaReferenceTest {

    private val parser = PaniniParser()

    @Test
    fun `classifies phala from parsed nominal identity`() {
        assertTrue(PhalaReference.isReference(parseSubanta("फल + अम्")))
        assertFalse(PhalaReference.isReference(parseSubanta("फलित + अम्")))
    }

    private fun parseSubanta(source: String): SubantaPada =
        parser.parse(source).vakyas.flatMap { it.padas }.filterIsInstance<SubantaPada>().single()
}
