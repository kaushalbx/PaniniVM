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
}
