package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PhalaReferenceTest {

    private val parser = PaniniParser()

    @Test fun `derived phala is not the preceding result anaphor`() {
        for (affix in listOf("त्व", "मतुप्", "तरप्")) {
            val pada = parseSubanta("फल + $affix + अम्")
            assertFalse(PhalaReference.isReference(pada), affix)
            assertFalse(dev.panini.execution.NaturalSemanticNormalizer.isPriorResult(pada), affix)
        }
    }

    @Test
    fun `classifies phala from parsed nominal identity`() {
        assertTrue(PhalaReference.isReference(parseSubanta("फल + अम्")))
        assertFalse(PhalaReference.isReference(parseSubanta("फलित + अम्")))
    }

    private fun parseSubanta(source: String): SubantaPada =
        parser.parse(source).grammaticalVakyas().flatMap { it.padas }.filterIsInstance<SubantaPada>().single()
}
