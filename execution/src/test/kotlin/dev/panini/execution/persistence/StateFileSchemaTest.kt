package dev.panini.execution.persistence

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StateFileSchemaTest {

    @Test
    fun `preserves established state record wire names`() {
        StateRecordType.entries.forEach { type ->
            assertEquals(type, StateRecordType.fromWireName(type.name))
        }
        assertEquals("PANINI_STATE_V2", StateFileSchema.HEADER_V2)
    }

    @Test
    fun `ignores unknown state record kinds`() {
        assertNull(StateRecordType.fromWireName("UNKNOWN"))
        assertNull(StateRecordType.fromWireName(null))
    }
}
