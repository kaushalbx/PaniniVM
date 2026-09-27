package dev.panini.shiksha

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OrthographyTest {
    @Test
    fun `renders avagraha at a phonological boundary without creating a varna`() {
        val varnas = listOf(Vyanjana.KA, Svara.A, Vyanjana.VA, Svara.E, Ayogavaha.VISARGA)
        val placement = OrthographicSignPlacement(OrthographicSign.AVAGRAHA, afterVarnaCount = 4)

        assertEquals("कवेऽः", varnas.toDevanagari(listOf(placement)))
        assertEquals(varnas, "कवेऽः".toVarnas())
    }

    @Test
    fun `does not place an orthographic sign inside a syllable`() {
        val varnas = listOf(Vyanjana.KA, Svara.E)

        assertFailsWith<IllegalArgumentException> {
            varnas.toDevanagari(
                listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, afterVarnaCount = 1)),
            )
        }
    }
}
