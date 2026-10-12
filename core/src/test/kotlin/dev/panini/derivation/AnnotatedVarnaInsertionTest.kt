package dev.panini.derivation

import dev.panini.shiksha.*
import kotlin.test.*

class AnnotatedVarnaInsertionTest {
    @Test
    fun `insertion cannot silently move a pending it designation`() {
        val original = DerivationTerm("term", "अप्", TermKind.PRATYAYA)
        val designated = original.copy(itDesignations = listOf(original.designateVarnaIt(1, dev.panini.core.ItMarker.P, "1.3.3")))
        assertFailsWith<IllegalArgumentException> {
            DerivationState(listOf(designated)).insertTermVarnas("term", 1, listOf(Vyanjana.TA), "test")
        }
    }

    @Test
    fun `insertion retains nasalization and shifts parsed avagraha only after its boundary`() {
        val original = DerivationTerm("term", "अँक्ऽइ", TermKind.PRATIPADIKA, formedPadaRupa = Rupa())
        val state = DerivationState(listOf(original))
        val before = state.insertTermVarnas("term", 1, listOf(Vyanjana.TA), "test").terms.single()
        assertEquals("अँत्क्ऽइ", before.surface)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 3)), before.orthographicSigns)
        assertTrue(before.phonologicalText.effectiveVarnas.first().nasalized)
        assertEquals(original.id, before.id)
        assertEquals(original.upadesha, before.upadesha)
        assertEquals(original.formedPadaRupa, before.formedPadaRupa)

        val at = state.insertTermVarnas("term", 2, listOf(Vyanjana.TA), "test").terms.single()
        assertEquals("अँक्ऽति", at.surface)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), at.orthographicSigns)
    }

    @Test
    fun `initial and final insertions preserve existing annotations and validate positions`() {
        val state = DerivationState(listOf(DerivationTerm("term", "कँ", TermKind.PRATIPADIKA)))
        assertEquals("अकँ", state.insertTermVarnas("term", 0, listOf(Svara.A), "test").terms.single().surface)
        assertEquals("कँक", state.insertTermVarnas("term", 2, listOf(Vyanjana.KA, Svara.A), "test").terms.single().surface)
        assertFailsWith<IllegalArgumentException> { state.insertTermVarnas("term", 3, listOf(Svara.A), "test") }
    }
}
