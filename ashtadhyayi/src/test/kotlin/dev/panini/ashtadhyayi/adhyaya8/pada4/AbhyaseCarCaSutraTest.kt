package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AbhyaseCarCaSutraTest {
    @Test
    fun `exact initial substitution retains vowel nasalization signs and term identity`() {
        val term = DerivationTerm("abhyasa", "भँऽ", TermKind.DHATU, upadesha = "भू", createdBySutra = "6.1.8")
        val state = DerivationState(
            listOf(term, DerivationTerm("dhatu", "भू", TermKind.DHATU)),
            samjnas = setOf(SamjnaAssignment(term.id, Samjna.ABHYASA)),
        )
        assertTrue(AbhyaseCarCaSutra.matches(state))
        val result = AbhyaseCarCaSutra.apply(state).state
        val changed = result.terms.first()
        assertEquals("बँऽ", changed.surface)
        assertEquals(term.id, changed.id)
        assertEquals(term.upadesha, changed.upadesha)
        assertEquals(term.createdBySutra, changed.createdBySutra)
        assertEquals(state.samjnas, result.samjnas)
        assertEquals(state.terms.last(), result.terms.last())
        assertEquals(0, result.substitutions.single().sourceVarnaIndex)
    }

    @Test
    fun `8 4 54 changes bha of an abhyasa to ba`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("abhyasa", "भ", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("dhatu", "भू", TermKind.DHATU, upadesha = "भू"),
            ),
            samjnas = setOf(SamjnaAssignment("abhyasa", Samjna.ABHYASA)),
        )

        assertTrue(AbhyaseCarCaSutra.matches(state))
        val result = AbhyaseCarCaSutra.apply(state).state
        assertEquals(listOf("ब", "भू"), result.terms.map { it.surface })
    }
}
