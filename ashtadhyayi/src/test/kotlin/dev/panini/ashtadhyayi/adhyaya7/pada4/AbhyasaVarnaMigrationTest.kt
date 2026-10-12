package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class AbhyasaVarnaMigrationTest {
    @Test
    fun `san substitution retains nasal vowel and orthographic sign with exact trace`() {
        val abhyasa = DerivationTerm("abhyasa", "पँऽ", TermKind.DHATU, upadesha = "पठ्", createdBySutra = "6.1.9")
        val san = DerivationTerm("san", "स", TermKind.PRATYAYA, upadesha = "सन्")
        val state = DerivationState(listOf(abhyasa, san))
        assertTrue(SanyAtaSutra.matches(state))
        val result = SanyAtaSutra.apply(state).state
        val changed = result.terms.first()
        assertEquals("पिँऽ", changed.surface)
        assertEquals(abhyasa.id, changed.id)
        assertEquals(abhyasa.upadesha, changed.upadesha)
        assertEquals(abhyasa.createdBySutra, changed.createdBySutra)
        assertEquals(san, result.terms.last())
        assertEquals(1, result.substitutions.single().sourceVarnaIndex)
        assertFalse(SanyAtaSutra.matches(result))
        assertFalse(SanyAtaSutra.matches(state.copy(terms = listOf(abhyasa, san.copy(kind = TermKind.DHATU)))))
        assertFalse(SanyAtaSutra.matches(state.copy(terms = listOf(abhyasa.copy(surface = "पा"), san))))
    }

    @Test
    fun `7 4 79 treats an inherent final a as a phonological vowel`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("abhyasa", "प", TermKind.DHATU),
                DerivationTerm("san", "सन्", TermKind.PRATYAYA, upadesha = "सन्"),
            ),
        )

        assertTrue(SanyAtaSutra.matches(state))
        assertEquals("पि", SanyAtaSutra.apply(state).state.terms.first().surface)
    }

    @Test
    fun `7 4 82 applies guna to the final ik varna independent of matra spelling`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("abhyasa", "कु", TermKind.DHATU),
                DerivationTerm("yang", "यङ्", TermKind.PRATYAYA, upadesha = "यङ्"),
            ),
        )

        assertTrue(GunoYangiSutra.matches(state))
        assertEquals("को", GunoYangiSutra.apply(state).state.terms.first().surface)
    }
}
