package dev.panini.shiksha

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OrthographyTest {
    @Test
    fun `shaping controls round trip without becoming phonological tokens`() {
        for (control in listOf("\u200D", "\u200C")) {
            val source = "द्${control}युभ्यः"
            val parsed = source.toSanskritText()
            assertEquals("द्युभ्यः".toVarnas(), source.toVarnas())
            assertEquals(1, parsed.sourceOrthographicSigns.size)
            assertEquals(1, parsed.sourceOrthographicSigns.single().afterVarnaCount)
            assertEquals(source, parsed.renderWithOrthographicSigns(parsed.sourceOrthographicSigns))
            assertEquals(source, source.toVarnas().toDevanagari(parsed.sourceOrthographicSigns))
        }
    }

    @Test
    fun `unattached shaping controls are rejected rather than ignored`() {
        for (source in listOf("\u200Dक", "क\u200C", "अ\u200D", "क्\u200D\u200C")) {
            assertFailsWith<IllegalArgumentException> { source.toSanskritText() }
        }
    }

    @Test
    fun `varna substitution preserves shaping sign and records only the sound change`() {
        val term = dev.panini.derivation.DerivationTerm(
            "word", "द्\u200Dयुभ्यः", dev.panini.derivation.TermKind.PRATIPADIKA,
        )
        val state = dev.panini.derivation.DerivationState(listOf(term))
        val changed = state.replaceTermVarna(term.id, 0, listOf(Vyanjana.TA), "test")
        assertEquals("त्\u200Dयुभ्यः", changed.surface)
        assertEquals(term.id, changed.terms.single().id)
        assertEquals(term.varnas.drop(1), changed.terms.single().varnas.drop(1))
        assertEquals(listOf(Vyanjana.TA), changed.substitutions.single().replacement.toVarnas())
    }

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
