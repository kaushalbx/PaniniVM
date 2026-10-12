package dev.panini.shiksha

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class VarnaSourceSpanTest {
    @Test
    fun `nasalization retains the exact independent dependent and inherent vowel spans`() {
        for ((written, expected) in listOf(
            "उँ" to listOf(VarnaSourceSpan(0, 2)),
            "तुँ" to listOf(VarnaSourceSpan(0, 1), VarnaSourceSpan(1, 3)),
            "कँ" to listOf(VarnaSourceSpan(0, 1), VarnaSourceSpan(1, 2)),
        )) {
            val parsed = written.toSanskritText()
            assertEquals(expected, parsed.varnas.map { it.sourceSpan })
            assertEquals(true, parsed.last()?.nasalized)
            assertEquals(written, parsed.render())
        }
    }

    @Test
    fun `written spans do not change token positions or turn avagraha into a varna`() {
        val parsed = "क्तवतुँऽ".toSanskritText()
        assertEquals(listOf(Vyanjana.KA, Vyanjana.TA, Svara.A, Vyanjana.VA,
            Svara.A, Vyanjana.TA, Svara.U), parsed.varnas.map { it.varna })
        assertEquals(VarnaSourceSpan(0, 2), parsed.first()?.sourceSpan)
        assertEquals(VarnaSourceSpan(5, 7), parsed.last()?.sourceSpan)
        assertEquals(VarnaSourceSpan(3, 3), parsed.varnas[2].sourceSpan)
    }

    @Test
    fun `synthetic tokens have no invented written provenance`() {
        assertNull(VarnaToken(VarnaTokenId("synthetic"), Svara.A).sourceSpan)
    }
}
