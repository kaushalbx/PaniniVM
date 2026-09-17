package dev.panini.execution

import dev.panini.core.SupAffix
import kotlin.test.Test
import kotlin.test.assertEquals

class PrakriyaRegistryTest {

    @Test
    fun `strips every suffix defined by authoritative sup metadata`() {
        SupAffix.entries.map { it.upadesha }.distinct().forEach { upadesha ->
            assertEquals(
                "गणित",
                PrakriyaRegistry.stripSupSuffix("गणित + $upadesha"),
                upadesha,
            )
        }
        assertEquals("गणित", PrakriyaRegistry.stripSupSuffix("गणित + ङसिँ"))
    }

    @Test
    fun `retains unknown and unsegmented endings`() {
        assertEquals("गणित + अमुक", PrakriyaRegistry.stripSupSuffix("गणित + अमुक"))
        assertEquals("गणित", PrakriyaRegistry.stripSupSuffix("गणित"))
    }
}
