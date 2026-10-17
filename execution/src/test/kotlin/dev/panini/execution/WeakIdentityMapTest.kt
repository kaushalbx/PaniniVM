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

    @Test fun `bounded maps evict old entries even when values retain their keys`() {
        data class RetainingValue(val owner: Token)
        val first = Token(1)
        val second = Token(2)
        val owners = WeakIdentityMap<Token, RetainingValue>(maxEntries = 1)

        owners[first] = RetainingValue(first)
        owners[second] = RetainingValue(second)

        assertEquals(1, owners.size())
        assertNull(owners[first])
        assertSame(second, owners[second]?.owner)
    }

    @Test fun `get or put computes once per object identity`() {
        val first = Token(1)
        val equalButDistinct = Token(1)
        val owners = WeakIdentityMap<Token, String>()
        var computations = 0

        assertEquals("value-1", owners.getOrPut(first) { "value-${++computations}" })
        assertEquals("value-1", owners.getOrPut(first) { "value-${++computations}" })
        assertEquals("value-2", owners.getOrPut(equalButDistinct) { "value-${++computations}" })
        assertEquals(2, computations)
    }
}
