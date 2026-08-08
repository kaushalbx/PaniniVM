package dev.panini.vyakaranam.ast

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProgramNodeTest {
    private val vakya = NamaVakya("राम + सुँ", emptyList())

    @Test
    fun `repeat preserves structural leaves and expands execution order`() {
        val repeat = Repeat("त्रिः राम + सुँ", 3, Invocation(vakya))

        assertEquals(1, repeat.invocations().size)
        assertEquals(3, repeat.expandedInvocations().size)
    }

    @Test
    fun `repeat count must be positive`() {
        assertFailsWith<IllegalArgumentException> {
            Repeat("", 0, Invocation(vakya))
        }
    }

    @Test
    fun `procedure and scope expose their nested program leaves`() {
        val invocation = Invocation(vakya)
        val procedure = Procedure("definition", "योग", body = listOf(invocation))
        val scope = Scope("scope", "गणित", body = listOf(procedure))

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
