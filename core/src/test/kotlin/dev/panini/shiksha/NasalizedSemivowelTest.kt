package dev.panini.shiksha

import kotlin.test.*

class NasalizedSemivowelTest {
    @Test
    fun `nasalized semivowels round trip independently of vowel nasalization`() {
        for (varna in listOf(Vyanjana.YA, Vyanjana.VA, Vyanjana.LA)) {
            val text = SanskritText(listOf(
                VarnaToken(VarnaTokenId("vowel"), Svara.A),
                VarnaToken(VarnaTokenId("semivowel"), varna, nasalized = true),
                VarnaToken(VarnaTokenId("following"), Svara.I),
            ))
            val parsed = SanskritText.parse(text.render())
            assertEquals(text.varnas.map { it.varna to it.nasalized }, parsed.varnas.map { it.varna to it.nasalized })
            assertFalse(parsed.varnas.first().nasalized)
            assertTrue(parsed.varnas[1].nasalized)
            assertTrue(parsed.sourceOrthographicSigns.isEmpty())
        }
    }

    @Test
    fun `ra cannot be given a nonexistent nasal counterpart`() {
        assertFailsWith<IllegalArgumentException> {
            VarnaToken(VarnaTokenId("ra"), Vyanjana.RA, nasalized = true)
        }
    }
}
