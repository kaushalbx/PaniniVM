package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PvmImperativeLexiconTest {

    @Test
    fun `causative command surfaces have migrated to derivation`() {
        assertNull(PvmImperativeLexicon.surface("मुद्र्"))
        assertNull(PvmImperativeLexicon.surface("युज्"))
        assertNull(PvmImperativeLexicon.surface("गण"))
    }

    @Test
    fun `keeps causative and simple paradigms distinct`() {
        assertEquals("देहि", PvmImperativeLexicon.surface("दा"))
        assertNull(PvmImperativeLexicon.surface("युज्"))
    }
}
