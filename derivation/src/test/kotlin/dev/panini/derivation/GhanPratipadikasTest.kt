package dev.panini.derivation

import dev.panini.core.Linga
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GhanPratipadikasTest {
    @Test fun `ghan action nouns derive forward and are masculine by ghanabantah`() {
        val entries = GhanPratipadikas.entries().associateBy { it.text }
        println("GHAN stems=${entries.size}")
        for (stem in listOf("योग", "हार")) {
            val entry = entries.getValue(stem)
            assertEquals(setOf(Linga.PUMS), entry.linga)
            assertTrue("36" in entry.lingaRuleIds)
        }
    }
}
