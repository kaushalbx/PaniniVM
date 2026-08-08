package dev.panini.execution

import dev.panini.core.SupAffix
import kotlin.test.Test
import kotlin.test.assertEquals

class SamjnaKriyaRegistryTest {

    @Test
    fun `strips every suffix defined by authoritative sup metadata`() {
        SupAffix.entries.map { it.upadesha }.distinct().forEach { upadesha ->
            assertEquals(
                "गणित",
                SamjnaKriyaRegistry.stripSupSuffix("गणित + $upadesha"),
                upadesha,
            )
        }
        assertEquals("गणित", SamjnaKriyaRegistry.stripSupSuffix("गणित + ङसिँ"))
    }

    @Test
    fun `retains unknown and unsegmented endings`() {
        assertEquals("गणित + अमुक", SamjnaKriyaRegistry.stripSupSuffix("गणित + अमुक"))
        assertEquals("गणित", SamjnaKriyaRegistry.stripSupSuffix("गणित"))
    }
}
