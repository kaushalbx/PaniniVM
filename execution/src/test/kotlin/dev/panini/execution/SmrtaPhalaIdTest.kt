package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SmrtaPhalaIdTest {

    @Test
    fun `builds canonical remembered result identity`() {
        assertEquals("उक्ति-१२", SmrtaPhalaId.turnPrefix(12))
        assertEquals("उक्ति-१२/योग-3", SmrtaPhalaId.of(12, KriyaInvocationId.of(3)))
    }

    @Test
    fun `rejects invalid identity components`() {
        assertFailsWith<IllegalArgumentException> { SmrtaPhalaId.turnPrefix(0) }
        assertFailsWith<IllegalArgumentException> { SmrtaPhalaId.of(1, " ") }
    }
}
