package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Sequence
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ProgramAstTest {
    private val parser = PaniniParser()

    @Test
    fun `sequence owns its statements and connectors`() {
        val ukti = parser.parse(
            "राम + सुँ भू + लट् + तिप् च फल + अम् खाद् + लट् + तिप् ।",
        )

        val sequence = assertIs<Sequence>(ukti.body)
        assertEquals(2, sequence.statements.size)
        assertEquals(listOf("च"), sequence.connectors)
        sequence.statements.forEach { assertIs<Invocation>(it) }
        assertEquals(2, ukti.vakyas.size)
    }

    @Test
    fun `conditional owns explicit condition and branches`() {
        val ukti = parser.parse(
            "यदि राम + सुँ भू + लट् + तिप् तर्हि फल + अम् खाद् + लट् + तिप् " +
                "अन्यथा जल + अम् पा + लट् + तिप् ।",
        )

        val conditional = assertIs<Conditional>(ukti.body)
        assertIs<Invocation>(conditional.condition)
        assertIs<Invocation>(conditional.consequent)
        assertIs<Invocation>(conditional.alternate)
        assertEquals(3, ukti.vakyas.size)
    }

    @Test
    fun `conditional without otherwise has no alternate node`() {
        val ukti = parser.parse(
            "यदि राम + सुँ भू + लट् + तिप् तर्हि फल + अम् खाद् + लट् + तिप् ।",
        )

        assertNull(assertIs<Conditional>(ukti.body).alternate)
    }
}
