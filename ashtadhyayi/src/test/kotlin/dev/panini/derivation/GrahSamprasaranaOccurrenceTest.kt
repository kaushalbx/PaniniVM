package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.GrahijyaSamprasaranaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.SamprasaranacCaSutra
import dev.panini.shiksha.*
import kotlin.test.*

class GrahSamprasaranaOccurrenceTest {
    private fun state(surface: String = "ग्रह्", suffix: String = "श्ना") = DerivationState(listOf(
        DerivationTerm("root", surface, TermKind.DHATU, upadesha = "ग्रह्"),
        DerivationTerm("suffix", suffix, TermKind.PRATYAYA)))

    @Test fun `exact samprasarana and purvarupa retain nasalization signs and lexical identity`() {
        val initial = state("ग्रँह्ऽ")
        assertTrue(GrahijyaSamprasaranaSutra.matches(initial))
        val first = GrahijyaSamprasaranaSutra.apply(initial).state
        assertEquals(listOf(Vyanjana.GA, Svara.R, Svara.A, Vyanjana.HA), first.terms.first().varnas)
        assertEquals(1, first.substitutions.single().sourceVarnaIndex)
        assertFalse(GrahijyaSamprasaranaSutra.matches(first))
        val result = SamprasaranacCaSutra.apply(first).state
        assertEquals("गृह्ऽ", result.terms.first().surface)
        assertEquals("ग्रह्", result.terms.first().upadesha)
        assertEquals(initial.terms.last(), result.terms.last())
        assertEquals(1, result.substitutions.last().sourceVarnaIndex)
        assertEquals(first.terms.first().surface, result.substitutions.last().originalSurface)
        assertFalse(SamprasaranacCaSutra.matches(result))
    }

    @Test fun `purvarupa preserves the actual samprasarana vowel annotation`() {
        val first = GrahijyaSamprasaranaSutra.apply(state()).state
        val annotated = first.copy(terms = listOf(first.terms.first().copy(surface = "गृँअह्ऽ"), first.terms.last()))
        assertTrue(SamprasaranacCaSutra.matches(annotated))
        val result = SamprasaranacCaSutra.apply(annotated).state
        assertEquals("गृँह्ऽ", result.terms.first().surface)
    }

    @Test fun `bare vocalic r or unrelated suffix does not establish samprasarana`() {
        assertFalse(SamprasaranacCaSutra.matches(state("गृअह्")))
        assertFalse(GrahijyaSamprasaranaSutra.matches(state(suffix = "शप्")))
        assertFalse(GrahijyaSamprasaranaSutra.matches(state("ग्रहण्")))
    }
}
