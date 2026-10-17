package dev.panini.execution

import dev.panini.shiksha.Samjna
import kotlin.test.*

class PuranaValueTest {
    @Test fun `ordinal values are positive one based and distinct from cardinal counts`() {
        val second = SanskritValue.Purana(2, "द्वितीय")
        assertEquals(2L, second.value)
        assertEquals("द्वितीय", second.toDisplayText())
        assertTrue(Samjna.PURANA in second.samjnas)
        assertTrue(second.semanticallyEquals(SanskritValue.Purana(2, "द्वितीया")))
        val cardinal = SanskritValue.Sankhya(2, "द्वि")
        assertFalse(second.semanticallyEquals(cardinal))
        assertFalse(cardinal.semanticallyEquals(second))
        assertTrue(OperationValueType.ORDINAL.accepts(second))
        assertFalse(OperationValueType.NUMBER.accepts(second))
        assertFalse(OperationValueType.ORDINAL.accepts(cardinal))
        for (position in listOf(0L, -1L, Long.MIN_VALUE)) {
            assertFailsWith<IllegalArgumentException> { SanskritValue.Purana(position, "invalid") }
        }
        assertEquals(Long.MAX_VALUE, SanskritValue.Purana(Long.MAX_VALUE, "large").value)
    }
}
