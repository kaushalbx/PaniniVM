package dev.sanskrit.samjna

import dev.sanskrit.shiksha.Svara
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SvaraSamjnaTest {
    @Test
    fun `identifies vrddhi svaras from 1 1 1`() {
        assertTrue(SvaraSamjna.isVrddhi(Svara.AA))
        assertTrue(SvaraSamjna.isVrddhi(Svara.AI))
        assertTrue(SvaraSamjna.isVrddhi(Svara.AU))
        assertFalse(SvaraSamjna.isVrddhi(Svara.A))
    }

    @Test
    fun `identifies guna svaras from 1 1 2`() {
        assertTrue(SvaraSamjna.isGuna(Svara.A))
        assertTrue(SvaraSamjna.isGuna(Svara.E))
        assertTrue(SvaraSamjna.isGuna(Svara.O))
        assertFalse(SvaraSamjna.isGuna(Svara.AA))
    }

    @Test
    fun `resolves guna and vrddhi for ik svaras from 1 1 3`() {
        assertTrue(SvaraSamjna.isIk(Svara.I))
        assertTrue(SvaraSamjna.isIk(Svara.UU))
        assertFalse(SvaraSamjna.isIk(Svara.A))

        assertEquals(Svara.E, SvaraSamjna.gunaForIk(Svara.I))
        assertEquals(Svara.O, SvaraSamjna.gunaForIk(Svara.U))
        assertEquals(Svara.A, SvaraSamjna.gunaForIk(Svara.R))
        assertEquals(Svara.AI, SvaraSamjna.vrddhiForIk(Svara.II))
        assertEquals(Svara.AU, SvaraSamjna.vrddhiForIk(Svara.UU))
        assertEquals(Svara.AA, SvaraSamjna.vrddhiForIk(Svara.RR))
    }
}
