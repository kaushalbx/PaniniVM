package dev.panini.plugin

import dev.panini.vyakaranam.ast.ProgramDocument
import dev.panini.vyakaranam.parser.PaniniParser
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PvmCanonicalAstIntegrationTest {
    @Test
    fun `canonical document retains typed nodes and source spans for IDE consumers`() {
        val source = File("projects/number-guessing-game/number_guessing_game.pvm").readText()
        val document = PaniniParser().parseDocument(source)

        assertIs<ProgramDocument>(document)
        assertTrue(document.items.isNotEmpty())
        assertEquals(document.items.size, document.itemSpans.size)
        document.itemSpans.forEach { span ->
            assertTrue(span.start in source.indices)
            assertTrue(span.endExclusive in 1..source.length)
            assertTrue(span.endExclusive > span.start)
            assertTrue(source.substring(span.start, span.endExclusive).isNotBlank())
        }
    }
}
