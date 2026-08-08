package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class KriyaInvocationIdTest {

    @Test
    fun `creates and parses canonical invocation identities`() {
        assertEquals("योग-3", KriyaInvocationId.of(3))
        assertEquals(3, KriyaInvocationId.indexOf("योग-3"))
    }

    @Test
    fun `rejects unrelated malformed and nonpositive identities`() {
        assertNull(KriyaInvocationId.indexOf("फल-3"))
        assertNull(KriyaInvocationId.indexOf("योग-अ"))
        assertNull(KriyaInvocationId.indexOf("योग-0"))
    }
}
