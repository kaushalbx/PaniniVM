package dev.panini.derivation

import dev.panini.shiksha.*
import kotlin.test.*

class ExactVarnaReplacementTest {
    @Test fun `nasal vowel to semivowel replacement keeps nasality on that consonant`() {
        for ((vowel, semivowel) in listOf(Svara.I to Vyanjana.YA, Svara.U to Vyanjana.VA)) {
            val original = DerivationTerm("term", "${vowel.devanagari}ँऽअ", TermKind.PRATIPADIKA,
                formedPadaRupa = Rupa())
            val after = DerivationState(listOf(original)).replaceTermVarna("term", 0, listOf(semivowel), "test")
            val changed = after.terms.single()
            assertEquals(listOf(semivowel, Svara.A), changed.varnas)
            assertEquals(listOf(true, false), changed.phonologicalText.effectiveVarnas.map { it.nasalized })
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 1)), changed.orthographicSigns)
            assertEquals(original.formedPadaRupa, changed.formedPadaRupa)
            assertEquals(original.upadesha, changed.upadesha)
            assertEquals(0, after.substitutions.single().sourceVarnaIndex)
        }
    }

    @Test
    fun `range replacement maps annotations of each consumed token explicitly`() {
        val original = DerivationTerm("term", "असँऽइ", TermKind.PRATIPADIKA, formedPadaRupa = Rupa())
        val result = DerivationState(listOf(original)).replaceTermVarnaRange(
            "term", 1, 2, listOf(Vyanjana.YA, Svara.A), mapOf(0 to 0, 1 to 1), "test",
        )
        val term = result.terms.single()
        assertEquals(listOf(Svara.A, Vyanjana.YA, Svara.A, Svara.I), term.varnas)
        assertEquals(listOf(false, false, true, false), term.phonologicalText.effectiveVarnas.map { it.nasalized })
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 3)), term.orthographicSigns)
        assertEquals(1, result.substitutions.size)
        assertEquals(original.upadesha, term.upadesha)
        assertEquals(original.formedPadaRupa, term.formedPadaRupa)
    }

    @Test
    fun `range deletion remaps signs and retains untouched nasal tokens in one trace`() {
        val original = DerivationTerm("term", "अँय्ऽअइ", TermKind.PRATIPADIKA, formedPadaRupa = Rupa())
        val result = DerivationState(listOf(original)).deleteTermVarnas("term", 1, 2, "test")
        val term = result.terms.single()
        assertEquals(listOf(Svara.A, Svara.I), term.varnas)
        assertTrue(term.phonologicalText.effectiveVarnas.first().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 1)), term.orthographicSigns)
        assertEquals(1, result.substitutions.size)
        assertEquals(original.upadesha, term.upadesha)
        assertEquals(original.formedPadaRupa, term.formedPadaRupa)
        assertFailsWith<IllegalArgumentException> {
            DerivationState(listOf(original)).deleteTermVarnas("term", 3, 2, "test")
        }
    }

    @Test
    fun `semivowel substitution preserves consonant nasality without nasalizing a vowel`() {
        val original = DerivationTerm("term", "अय्ँऽइ", TermKind.PRATIPADIKA, formedPadaRupa = Rupa())
        val result = DerivationState(listOf(original)).replaceTermVarna("term", 1, listOf(Vyanjana.LA), "test")
        val term = result.terms.single()
        assertEquals(listOf(Svara.A, Vyanjana.LA, Svara.I), term.varnas)
        assertEquals(listOf(false, true, false), term.phonologicalText.effectiveVarnas.map { it.nasalized })
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), term.orthographicSigns)
        assertEquals(original.id, term.id)
        assertEquals(original.upadesha, term.upadesha)
        assertEquals(original.formedPadaRupa, term.formedPadaRupa)
        assertEquals("test", result.substitutions.last().sutra)
    }

    @Test
    fun `deleting nasalized semivowel does not transfer nasality to neighboring vowels`() {
        val original = DerivationTerm("term", "अय्ँऽइ", TermKind.PRATIPADIKA)
        val term = DerivationState(listOf(original)).replaceTermVarna("term", 1, emptyList(), "test").terms.single()
        assertEquals(listOf(Svara.A, Svara.I), term.varnas)
        assertTrue(term.phonologicalText.effectiveVarnas.none { it.nasalized })
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 1)), term.orthographicSigns)
    }

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
