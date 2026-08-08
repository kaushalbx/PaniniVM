package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PvmImperativeLexiconTest {

    @Test
    fun `resolves causative pvm command surfaces`() {
        assertEquals("योजय", PvmImperativeLexicon.surface("युज्", causative = true))
        assertEquals("गणय", PvmImperativeLexicon.surface("गण", causative = true))
        assertEquals("मुद्रय", PvmImperativeLexicon.surface("मुद्र्", causative = true))
    }

    @Test
    fun `keeps causative and simple paradigms distinct`() {
        assertEquals("देहि", PvmImperativeLexicon.surface("दा", causative = false))
        assertNull(PvmImperativeLexicon.surface("दा", causative = true))
        assertNull(PvmImperativeLexicon.surface("युज्", causative = false))
    }
}
