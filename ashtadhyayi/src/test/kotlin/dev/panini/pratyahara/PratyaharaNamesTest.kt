package dev.panini.pratyahara

import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PratyaharaNamesTest {
    private val names = PratyaharaEngine().names()

    @Test fun `a name is the sound, a pronouncing a for a consonant, and a later it-letter`() {
        assertTrue(listOf(Svara.A, Vyanjana.CA) in names)                 // अच्
        assertTrue(listOf(Vyanjana.HA, Svara.A, Vyanjana.LA) in names)    // हल्
        assertTrue(listOf(Svara.I, Vyanjana.KA) in names)                 // इक्
        assertTrue(listOf(Vyanjana.YA, Svara.A, Vyanjana.NNA) in names)   // यण्
        assertTrue(listOf(Svara.E, Vyanjana.NGA) in names)                // एङ्
        // Only the pratyāhāras the grammar uses: वम्, हम् are not among them.
        assertFalse(listOf(Vyanjana.VA, Svara.A, Vyanjana.MA) in names)
        assertFalse(listOf(Vyanjana.HA, Svara.A, Vyanjana.MA) in names)
        assertTrue(names.size == names.distinct().size)
    }

    @Test fun `hal closes on its it-letter and does not sound la`() {
        val engine = PratyaharaEngine()
        // ल is sounded only in लण्, so the ranges after it (झल्, शल्) do not contain it.
        assertFalse(engine.contains(Pratyahara.JHAL, Vyanjana.LA))
        assertTrue(engine.contains(Pratyahara.HAL, Vyanjana.LA))
        assertTrue(engine.contains(Pratyahara.JHAL, Vyanjana.HA))
    }
}
