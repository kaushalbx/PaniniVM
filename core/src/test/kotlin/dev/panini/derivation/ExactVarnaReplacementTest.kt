package dev.panini.derivation

import dev.panini.shiksha.*
import kotlin.test.*

class ExactVarnaReplacementTest {
    @Test
    fun `expansion preserves unaffected nasalization and parsed avagraha`() {
        val original = DerivationTerm("term", "अँक्ऽइ", TermKind.PRATIPADIKA, formedPadaRupa = Rupa())
        val result = DerivationState(listOf(original)).replaceTermVarna("term", 2, listOf(Svara.AA, Vyanjana.RA), "test")
        val term = result.terms.single()
        assertEquals("अँक्ऽआर्", term.surface)
        assertEquals(listOf(Svara.A, Vyanjana.KA, Svara.AA, Vyanjana.RA), term.varnas)
        assertTrue(term.phonologicalText.effectiveVarnas.first().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), term.orthographicSigns)
        assertEquals(original.id, term.id)
        assertEquals(original.upadesha, term.upadesha)
        assertEquals(original.formedPadaRupa, term.formedPadaRupa)
    }

    @Test
    fun `deletion remaps a parsed sign and retains surviving nasal token`() {
        val original = DerivationTerm("term", "अँक्ऽइ", TermKind.PRATIPADIKA)
        val term = DerivationState(listOf(original)).replaceTermVarna("term", 1, emptyList(), "test").terms.single()
        assertEquals("अँऽइ", term.surface)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 1)), term.orthographicSigns)
    }

    @Test
    fun `replacement carries vowel nasalization without duplicating chandrabindu`() {
        val original = DerivationTerm("term", "एँ", TermKind.PRATIPADIKA)
        val term = DerivationState(listOf(original)).replaceTermVarna("term", 0, listOf(Svara.A, Vyanjana.YA), "test").terms.single()
        assertEquals("अँय्", term.surface)
        assertTrue(term.phonologicalText.effectiveVarnas.first().nasalized)
        assertFailsWith<IllegalArgumentException> {
            DerivationState(listOf(original)).replaceTermVarna("term", 1, emptyList(), "test")
        }
    }
}
