package dev.panini.vyakaranam.ast

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProgramNodeTest {
    private val vakya = NamaVakya("राम + सुँ", emptyList())

    @Test
    fun `grammatical loop traversal includes test without adding it to binding projection`() {
        val condition = Invocation(NamaVakya("condition", emptyList()))
        val body = Invocation(NamaVakya("body", emptyList()))
        val exhausted = Invocation(NamaVakya("exhausted", emptyList()))
        val target = Invocation(NamaVakya("target", emptyList()))
        val loop = WhileLoop("", condition, body, exhausted = exhausted, resultTarget = target)
        assertEquals(listOf(condition, body, exhausted, target), loop.grammaticalInvocations())
        assertEquals(listOf(body, exhausted, target), loop.expandedInvocations())
        assertEquals(listOf(condition.vakya, body.vakya, exhausted.vakya, target.vakya),
            Ukti("", body = loop).grammaticalVakyas())
    }

    @Test
    fun `repeat preserves structural leaves and expands execution order`() {
        val repeat = Repeat("त्रिः राम + सुँ", 3, Invocation(vakya))

        assertEquals(1, repeat.invocations().size)
        assertEquals(3, repeat.expandedInvocations().size)
    }

    @Test
    fun `repeat count must be nonnegative`() {
        assertFailsWith<IllegalArgumentException> {
            Repeat("", -1, Invocation(vakya))
        }
        val zero = Repeat("", 0, Invocation(vakya))
        assertEquals(1, zero.invocations().size)
        assertEquals(0, zero.expandedInvocations().size)
    }

    @Test
    fun `sequence boundaries retain typed result flow`() {
        val statement = Invocation(vakya)
        val sequence = Sequence("", listOf(statement, statement, statement), listOf("ततः", "च"))

        assertEquals(SequenceResultFlow.PREVIOUS_RESULT, sequence.boundaries[0].resultFlow)
        assertEquals(SequenceResultFlow.NONE, sequence.boundaries[1].resultFlow)
        assertEquals(0 to 1, sequence.boundaries[0].let {
            it.sourceStatementIndex to it.targetStatementIndex
        })
    }

    @Test
    fun `prakriya and scope expose their nested program leaves`() {
        val invocation = Invocation(vakya)
        val prakriya = Prakriya("definition", "योग", body = listOf(invocation))
        val scope = Scope("scope", "गणित", body = listOf(prakriya))

        assertEquals(listOf(invocation), scope.invocations())
    }

    @Test
    fun `depth first traversal exposes canonical children in source order`() {
        val first = Invocation(vakya)
        val second = Invocation(NamaVakya("सीता + सुँ", emptyList()))
        val alternate = Invocation(NamaVakya("फल + अम्", emptyList()))
        val conditional = Conditional(
            "conditional",
            first,
            Repeat("repeat", 2, second),
            alternate,
        )

        assertEquals(
            listOf(conditional, first, conditional.consequent, second, alternate),
            conditional.depthFirst().toList(),
        )
        assertEquals(
            listOf(first, second, second, alternate),
            conditional.expandedInvocations(),
        )
    }

    @Test
    fun `transformer recursively rewrites nested invocations`() {
        val original = Conditional(
            "conditional",
            Invocation(vakya),
            Repeat("repeat", 2, Invocation(vakya)),
        )
        val replacement = NamaVakya("सीता + सुँ", emptyList())
        val transformed = object : ProgramNodeTransformer() {
            override fun visitInvocation(node: Invocation): ProgramNode = Invocation(replacement)
        }.transform(original)

        assertEquals(listOf(replacement, replacement), transformed.invocations().map { it.vakya })
    }
}
