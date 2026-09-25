package dev.panini.shiksha

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DevanagariCodecTest {
    @Test
    fun `parses orthography as phonological varnas`() {
        assertEquals(listOf(Vyanjana.KA, Svara.A), "क".toVarnas())
        assertEquals(listOf(Vyanjana.KA, Svara.AA), "का".toVarnas())
        assertEquals(listOf(Vyanjana.KA, Svara.I), "कि".toVarnas())
        assertEquals(listOf(Vyanjana.KA), "क्".toVarnas())
        assertEquals(
            listOf(Vyanjana.RA, Svara.AA, Vyanjana.MA, Svara.A),
            "राम".toVarnas(),
        )
    }

    @Test
    fun `renders consonant vowel sequences canonically`() {
        listOf("क", "का", "कि", "क्", "क्त", "रामः", "सुँ", "णिच्").forEach { form ->
            assertEquals(form, SanskritText.parse(form).render(), form)
        }
    }

    @Test
    fun `replaces a phonological final independent of its unicode spelling`() {
        assertEquals("गङ्ग", "गङ्गा".replaceLastVarna(Svara.AA, listOf(Svara.A)))
        assertEquals("नदि", "नदी".replaceLastVarna(Svara.II, listOf(Svara.I)))
        assertEquals("अय्", "ए".replaceLastVarna(Svara.E, listOf(Svara.A, Vyanjana.YA)))
    }

    @Test
    fun `retains stable occurrence identities`() {
        val text = SanskritText.parse("तत", tokenIdPrefix = "term")

        assertEquals(listOf("term:0", "term:1", "term:2", "term:3"), text.varnas.map { it.id.value })
        assertEquals(2, text.varnas.count { it.varna == Vyanjana.TA })
    }

    @Test
    fun `does not silently discard unsupported input`() {
        assertFailsWith<IllegalStateException> { SanskritText.parse("राम A") }
        assertEquals(listOf(Svara.AA), "ा".toVarnas())
        assertEquals("आ", SanskritText.parse("ा").render())
    }

    @Test
    fun `treats avagraha as a known non-phonological sign`() {
        assertEquals(
            listOf(Vyanjana.NA, Vyanjana.TA, Svara.I),
            "ऽन्ति".toVarnas(),
        )
    }

    @Test
    fun `non-present token retains provenance and is not rendered`() {
        val parsed = SanskritText.parse("णिच्", "nic")
        val hidden = parsed.copy(
            varnas = parsed.varnas.map {
                if (it.varna == Vyanjana.NNA || it.varna == Vyanjana.CA) {
                    it.copy(state = VarnaState.LUPTA, stateAssignedBySutra = "1.3.9")
                } else it
            },
        )

        assertEquals("इ", hidden.render())
        assertEquals(3, hidden.varnas.size)
        assertEquals(1, hidden.effectiveVarnas.size)
    }
}
