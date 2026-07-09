package dev.sanskrit.pratyahara

import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.shiksha.Varna
import dev.sanskrit.shiksha.Svara
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PratyaharaEngineTest {
    private val engine = PratyaharaEngine()

    @Test
    fun `derives ac from the Maheshvara Sutras`() {
        val result = engine.derive(Pratyahara.AC)

        assertEquals(
            setOf<Varna>(
                Svara.A,
                Svara.I,
                Svara.U,
                Svara.R,
                Svara.L,
                Svara.E,
                Svara.O,
                Svara.AI,
                Svara.AU,
            ),
            result,
        )
    }

    @Test
    fun `derives yan without hardcoded sound groups`() {
        val result = engine.derive(Pratyahara.YAN)

        assertEquals(
            setOf<Varna>(
                Vyanjana.YA,
                Vyanjana.VA,
                Vyanjana.RA,
                Vyanjana.LA,
            ),
            result,
        )
    }

    @Test
    fun `derives khar used by visarga sutras`() {
        val result = engine.derive(Pratyahara.KHAR)

        assertTrue(Vyanjana.KHA in result)
        assertTrue(Vyanjana.KA in result)
        assertTrue(Vyanjana.SA in result)
        assertTrue(Vyanjana.GA !in result)
    }
}
