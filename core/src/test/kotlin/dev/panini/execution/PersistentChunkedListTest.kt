package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PersistentChunkedListTest {
    @Test
    fun `append preserves older history snapshots across chunk boundaries`() {
        val original = emptyList<Int>().appendedPersistently((0 until 130).toList())
        val extended = original.appendedPersistently((130 until 300).toList())

        assertEquals((0 until 130).toList(), original)
        assertEquals((0 until 300).toList(), extended)
        assertEquals(299, extended.last())
    }

    @Test
    fun `indexed access validates list bounds`() {
        val values = emptyList<Int>().appendedPersistently(listOf(1, 2, 3))

        assertFailsWith<IndexOutOfBoundsException> { values[-1] }
        assertFailsWith<IndexOutOfBoundsException> { values[3] }
    }
}
