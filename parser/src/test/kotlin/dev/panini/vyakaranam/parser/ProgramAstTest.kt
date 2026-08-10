package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.NamaVakya
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.WhileLoop
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
    fun `iti connects a quoted command to its reporting command`() {
        val ukti = parser.parse(
            "सङ्ख्या + अम् अनुमिनु + लोट् + सिप् इति मुद्र् + णिच् + लोट् + सिप् ।",
        )

        val quotation = assertIs<Quotation>(ukti.body)
        assertIs<Invocation>(quotation.quoted)
        assertIs<Invocation>(quotation.reporting)
    }

    @Test
    fun `quotation remains structured inside a longer sequence`() {
        val ukti = parser.parse(
            "राम + सुँ भू + लट् + तिप् च सङ्ख्या + अम् अनुमिनु + लोट् + सिप् " +
                "इति मुद्र् + णिच् + लोट् + सिप् ततः फल + अम् खाद् + लट् + तिप् ।",
        )

        val sequence = assertIs<Sequence>(ukti.body)
        assertIs<Invocation>(sequence.statements.first())
        assertIs<Quotation>(sequence.statements[1])
        assertIs<Invocation>(sequence.statements.last())
        assertEquals(listOf("च", "ततः"), sequence.connectors)
    }

    @Test
    fun `yavat tavat builds a bounded condition loop`() {
        val loop = assertIs<WhileLoop>(
            parser.parse(
                "पञ्च + कृत्वः यावत् विजय + सुँ न तावत् प्रयत्न + अम् कृ + लोट् + सिप् ।",
            ).body,
        )

        assertEquals(listOf("पञ्च"), loop.maximumIterationStems)
        assertEquals("विजय+सुँन", loop.condition.sourceText)
        assertIs<Invocation>(loop.body)
    }

    @Test
    fun `bounded loop may own an exhaustion clause`() {
        val loop = assertIs<WhileLoop>(
            parser.parse(
                "द्वि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + अम् कृ + लोट् + सिप् " +
                    "अन्यथा समाप्त + अम् मुद्र् + लोट् + सिप् ।",
            ).body,
        )

        assertIs<Invocation>(loop.exhausted)
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
    fun `otherwise may contain a nested conditional`() {
        val ukti = parser.parse(
            "यदि एक + अम् एक + अम् च अस् + लोट् + सिप् तर्हि जय + अम् मुद्र् + लोट् + सिप् " +
                "अन्यथा यदि एक + अम् द्वि + अम् च नि + विद् + लोट् + सिप् तर्हि लघु + अम् मुद्र् + लोट् + सिप् " +
                "अन्यथा गुरु + अम् मुद्र् + लोट् + सिप् ।",
        )

        val outer = assertIs<Conditional>(ukti.body)
        val nested = assertIs<Conditional>(outer.alternate)
        assertIs<Invocation>(nested.consequent)
        assertIs<Invocation>(nested.alternate)
    }

    @Test
    fun `conditional result may flow to one tatah target`() {
        val conditional = assertIs<Conditional>(
            parser.parse(
                "यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् " +
                    "तर्हि लघु + अम् दा + लोट् + सिप् अन्यथा गुरु + अम् दा + लोट् + सिप् " +
                    "ततः मुद्र् + लोट् + सिप् ।",
            ).body,
        )

        val consequent = assertIs<Sequence>(conditional.consequent)
        assertIs<Invocation>(consequent.statements.last())
        assertEquals(listOf("ततः"), consequent.connectors)
        assertIs<Sequence>(conditional.alternate)
    }

    @Test
    fun `nominal attribute pipeline has two explicit AST stages`() {
        val sequence = assertIs<Sequence>(
            parser.parse(
                "परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + भिस् ततः मुद्र् + लोट् + सिप् ।",
            ).body,
        )

        assertEquals(listOf("ततः"), sequence.connectors)
        assertIs<NamaVakya>(assertIs<Invocation>(sequence.statements.first()).vakya)
        assertIs<AkhyataVakya>(assertIs<Invocation>(sequence.statements.last()).vakya)
    }

    @Test
    fun `conditional arms may be bare nominal values`() {
        val conditional = assertIs<Conditional>(
            parser.parse(
                "यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् " +
                    "तर्हि लघु अन्यथा गुरु ततः मुद्र् + लोट् + सिप् ।",
            ).body,
        )

        assertIs<Sequence>(conditional.consequent)
        assertIs<Sequence>(conditional.alternate)
    }

    @Test
    fun `loop may pipe its named outcome to a target`() {
        val loop = assertIs<WhileLoop>(
            parser.parse(
                "द्वि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + अम् कृ + लोट् + सिप् " +
                    "ततः परिणाम + ङे दा + लोट् + सिप् ।",
            ).body,
        )

        assertIs<Invocation>(loop.resultTarget)
    }

    @Test
    fun `purvapara syntax builds a pipeline directly`() {
        val source = "पञ्च + अम् द्वि + अम् च गणित + ङस् गुण् + ल्युट् + ङस् " +
            "गणित + ङस् रन्ध्र + ल्युट् + ङस् पूर्व + ङस् पर + ङस् एका + सुँ कृ + लोट् + सिप् ।"

        val pipeline = assertIs<Pipeline>(parser.parse(source).body)

        assertEquals(listOf("पञ्च", "द्वि"), pipeline.arguments)
        assertEquals(listOf("गुण् + ल्युट्", "रन्ध्र + ल्युट्"), pipeline.stages.map { it.operationStem })
    }
}
