package dev.panini.shiksha

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VarnamalaVarnaApiTest {
    @Test
    fun `varna-native savarna ignores devanagari layout`() {
        assertTrue(Varnamala.areSavarna(Svara.I, Svara.II))
        assertTrue(Varnamala.areSavarna(Vyanjana.KA, Vyanjana.GA))
        assertFalse(Varnamala.areSavarna(Svara.A, Vyanjana.KA))
    }

    @Test
    fun `varna-native grade operations return phonological sequences`() {
        assertEquals(Svara.I, Varnamala.getHrasva(Svara.AI))
        assertEquals(listOf(Svara.A, Vyanjana.RA), Varnamala.getGuna(Svara.R))
        assertEquals(listOf(Svara.AA, Vyanjana.RA), Varnamala.getVrddhi(Svara.R))
    }

}
