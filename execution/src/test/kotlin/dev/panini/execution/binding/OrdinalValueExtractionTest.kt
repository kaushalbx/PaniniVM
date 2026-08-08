package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OrdinalValueExtractionTest {

    private val parser = PaniniParser()

    @Test
    fun `extracts lexical and segmented ordinals through sankhya morphology`() {
        assertEquals(1L, NumeralPadaBinder.extractOrdinalValue(parsePada("प्रथम + अम्")))
        assertEquals(2L, NumeralPadaBinder.extractOrdinalValue(parsePada("द्वि + तीय + अम्")))
    }

    @Test
    fun `does not classify a cardinal as an ordinal`() {
        assertNull(NumeralPadaBinder.extractOrdinalValue(parsePada("द्वि + अम्")))
    }

    private fun parsePada(source: String): Pada = parser.parse(source).vakyas.flatMap { it.padas }.single()
}
