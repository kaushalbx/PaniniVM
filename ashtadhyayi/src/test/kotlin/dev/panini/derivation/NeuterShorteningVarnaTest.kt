package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada2.HrasvoNapumsakePratipadikasyaSutra
import dev.panini.core.Linga
import dev.panini.shiksha.*
import kotlin.test.*

class NeuterShorteningVarnaTest {
    private val rule = HrasvoNapumsakePratipadikasyaSutra
    private fun state(term: DerivationTerm, linga: Linga = Linga.NAPUMSAKA) =
        DerivationState(listOf(term), context = DerivationalContext(rupa = Rupa(linga = linga)))

    @Test fun `all long final vowels shorten at their exact occurrence`() {
        for (vowel in Svara.entries.filter { it.isDirgha }) {
            val word = listOf(Vyanjana.KA, Svara.A, Vyanjana.RA, vowel).toDevanagari()
            val term = DerivationTerm("stem", word, TermKind.PRATIPADIKA)
            val original = state(term)
            assertTrue(rule.matches(original))
            val result = rule.apply(original).state
            assertEquals(term.varnas.dropLast(1) + vowel.toHrasva(), result.terms.single().varnas)
            assertEquals(term.upadesha, result.terms.single().upadesha)
            assertEquals(3, result.substitutions.single().sourceVarnaIndex)
            assertFalse(rule.matches(result))
        }
    }

    @Test fun `shortening preserves nasalization accent signs and grammatical provenance`() {
        val term = DerivationTerm("stem", "काऽरीँऽ", TermKind.PRATIPADIKA, upadesha = "कारी",
            grammaticalAccents = listOf(GrammaticalAccent(3, AccentType.UDATTA, "test-accent")))
        val original = state(term)
        assertTrue(rule.matches(original))
        val result = rule.apply(original).state
        val target = result.terms.single()
        assertEquals(listOf(Vyanjana.KA, Svara.AA, Vyanjana.RA, Svara.I), target.varnas)
        assertEquals(term.phonologicalText.effectiveVarnas.take(3), target.phonologicalText.effectiveVarnas.take(3))
        assertTrue(target.phonologicalText.effectiveVarnas.last().nasalized)
        assertEquals(term.grammaticalAccents, target.grammaticalAccents)
        assertEquals(term.id, target.id)
        assertEquals(term.upadesha, target.upadesha)
        assertEquals(term.phonologicalText.sourceOrthographicSigns, target.orthographicSigns)
        assertEquals(rule.sutra, result.substitutions.single().sutra)
    }

    @Test fun `other genders short vowels nonstems and changed stems are excluded`() {
        val stem = DerivationTerm("stem", "कारी", TermKind.PRATIPADIKA)
        assertFalse(rule.matches(state(stem, Linga.PUMS)))
        assertFalse(rule.matches(state(stem, Linga.STRI)))
        assertFalse(rule.matches(state(stem.copy(surface = "कारि"))))
        assertFalse(rule.matches(state(stem.copy(kind = TermKind.PRATYAYA))))
        assertFalse(rule.matches(state(stem.copy(surface = "कारे"))))
    }
}
