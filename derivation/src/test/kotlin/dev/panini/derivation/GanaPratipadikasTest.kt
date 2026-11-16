package dev.panini.derivation

import dev.panini.ganapatha.SvaradiGana
import kotlin.test.Test
import kotlin.test.assertTrue

class GanaPratipadikasTest {
    @Test fun `gana stems carry a sutra-sourced gender and exclude indeclinables`() {
        val entries = GanaPratipadikas.entries()
        assertTrue(entries.isNotEmpty())
        assertTrue(entries.all { it.linga.isNotEmpty() && it.lingaRuleIds.isNotEmpty() })
        assertTrue(entries.none { SvaradiGana.contains(it.text) }, "svarādi members are avyayas")
        assertTrue(entries.none { it.text in setOf("च", "वा", "प्र", "अथ") })
    }
}
