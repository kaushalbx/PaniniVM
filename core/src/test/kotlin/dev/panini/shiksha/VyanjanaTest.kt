package dev.panini.shiksha

import kotlin.test.Test
import kotlin.test.assertEquals

class VyanjanaTest {
    @Test
    fun `renders halanta form`() {
        assertEquals("स्", Vyanjana.SA.halanta)
        assertEquals("क्", Vyanjana.KA.halanta)
    }

    @Test
    fun `exposes virama explicitly`() {
        assertEquals('्', Vyanjana.VIRAMA)
    }
}
