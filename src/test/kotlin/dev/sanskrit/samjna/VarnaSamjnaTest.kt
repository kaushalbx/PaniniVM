package dev.sanskrit.samjna

import dev.sanskrit.shiksha.Svara
import dev.sanskrit.shiksha.Vyanjana
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VarnaSamjnaTest {
    @Test
    fun `identifies samyoga from 1 1 7 using halanta vyanjana`() {
        assertTrue(VarnaSamjna.isSamyoga("क् त्"))
        assertTrue(VarnaSamjna.isSamyoga("क्त"))
        assertFalse(VarnaSamjna.isSamyoga("क्"))
    }

    @Test
    fun `identifies anunasika from 1 1 8`() {
        assertTrue(VarnaSamjna.isAnunasika(Vyanjana.MA))
        assertTrue(VarnaSamjna.isAnunasika(Vyanjana.NA))
        assertFalse(VarnaSamjna.isAnunasika(Vyanjana.KA))
    }

    @Test
    fun `identifies savarna from 1 1 9 and rejects ac hal mixture from 1 1 10`() {
        assertTrue(VarnaSamjna.isSavarna(Svara.A, Svara.AA))
        assertTrue(VarnaSamjna.isSavarna(Svara.I, Svara.II))
        assertFalse(VarnaSamjna.isSavarna(Svara.A, Svara.I))
        assertFalse(VarnaSamjna.isSavarna(Svara.A, Vyanjana.KA))
    }
}
