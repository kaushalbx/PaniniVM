package dev.panini.plugin

import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PvmInlayHintsTest {
    @Test
    fun `hints use canonical ukti spans and skip declarations`() {
        val source = """
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।
            पञ्चन् + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val document = PaniniParser().parseDocument(source)

        val hints = PvmInlayHints.compute(source, document)

        assertEquals(1, hints.size)
        assertEquals(source.length, hints.single().first)
        assertTrue(hints.single().second.isNotBlank())
    }

    @Test
    fun `canceled computation yields no partial hints`() {
        val source = "पञ्चन् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val document = PaniniParser().parseDocument(source)

        assertTrue(PvmInlayHints.compute(source, document) { true }.isEmpty())
    }
}
