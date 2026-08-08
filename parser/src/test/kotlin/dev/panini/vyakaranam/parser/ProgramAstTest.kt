package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
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
        assertEquals(2, ukti.grammaticalVakyas().size)
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
        assertEquals(3, ukti.grammaticalVakyas().size)
    }

    @Test
    fun `conditional without otherwise has no alternate node`() {
        val ukti = parser.parse(
            "यदि राम + सुँ भू + लट् + तिप् तर्हि फल + अम् खाद् + लट् + तिप् ।",
        )

        assertNull(assertIs<Conditional>(ukti.body).alternate)
    }

    @Test
    fun `purvapara syntax builds a pipeline directly`() {
        val source = "पञ्च + अम् द्वि + अम् च गणित + ङस् गुण् + ल्युट् + ङस् " +
            "गणित + ङस् रन्ध्र + ल्युट् + ङस् पूर्व + पर + ङस् एका + सुँ कृ + लोट् + सिप् ।"

        val pipeline = assertIs<Pipeline>(parser.parse(source).body)

        assertEquals(listOf("पञ्च", "द्वि"), pipeline.arguments)
        assertEquals(listOf("गुण् + ल्युट्", "रन्ध्र + ल्युट्"), pipeline.stages.map { it.operationStem })
    }
}
