package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.shiksha.Samjna
import kotlin.test.*

class ValueEnvironmentTest {
    @Test
    fun `ValueEnvironment merge preserves types and gives precedence to the newer environment`() {
        val conversation = ValueEnvironment(
            mapOf("फल" to SanskritValue.Shabda("पुरातनम्")),
        )
        val host = ValueEnvironment(
            mapOf("फल" to SanskritValue.Sankhya(5, "पञ्च")),
        )

        val merged = conversation.mergedWith(host)

        assertEquals(5, assertIs<SanskritValue.Sankhya>(merged.values.getValue("फल")).value)
        assertEquals("पञ्च", merged.displayValues().getValue("फल"))
        assertTrue(Samjna.SANKHYA in merged.samjnas().getValue("फल"))
    }
}
