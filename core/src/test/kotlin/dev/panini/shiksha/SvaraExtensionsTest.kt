package dev.panini.shiksha

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SvaraExtensionsTest {
    @Test
    fun `classifies short and long vowels`() {
        assertTrue(Svara.A.isHrasva)
        assertTrue(Svara.R.isHrasva)
        assertTrue(Svara.AA.isDirgha)
        assertTrue(Svara.E.isDirgha)
        assertFalse(Svara.A.isDirgha)
    }

    @Test
    fun `maps vowels to hrasva and dirgha counterparts`() {
        assertEquals(Svara.A, Svara.AA.toHrasva())
        assertEquals(Svara.I, Svara.AI.toHrasva())
        assertEquals(Svara.U, Svara.AU.toHrasva())
        assertEquals(Svara.II, Svara.I.toDirgha())
        assertEquals(Svara.E, Svara.E.toDirgha())
    }

    @Test
    fun `changes the phonological final without inspecting matras`() {
        assertEquals("गङ्ग", "गङ्गा".withFinalHrasva())
        assertEquals("नदि", "नदी".withFinalHrasva())
        assertEquals("नदी", "नदि".withFinalDirgha())
        assertEquals("आ", "अ".withFinalDirgha())
    }
}
