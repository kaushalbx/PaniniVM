package dev.panini.execution

import kotlin.test.*

class WeakIdentityMapTest {
    private data class Token(val state: Int)

    @Test fun `equal continuation tokens retain independent ownership and replay state`() {
        val first = Token(1)
        val second = Token(1)
        assertEquals(first, second)
        assertNotSame(first, second)
        val owners = WeakIdentityMap<Token, String>()
        owners[first] = "first frame"
        owners[second] = "second frame"
        assertEquals("first frame", owners.remove(first))
        assertNull(owners.remove(first))
        assertEquals("second frame", owners.remove(second))
        val consumed = WeakIdentityMap<Token, Unit>()
        assertNull(consumed.putIfAbsent(first, Unit))
        assertEquals(Unit, consumed.putIfAbsent(first, Unit))
        assertNull(consumed.putIfAbsent(second, Unit))
    }
}
