package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.MonusvarahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.NashcapadantasyaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.AnusvarasyaYayiParasavarnahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.VaPadantasyaSutra
import dev.panini.shiksha.Samjna
import kotlin.test.*

class AnusvaraPadaDomainTest {
    private fun state(left: String, designated: Boolean, gap: Boolean = false): DerivationState {
        val term = DerivationTerm("left", left, TermKind.PRATIPADIKA)
        return DerivationState(listOf(term) +
            (if (gap) listOf(DerivationTerm("zero", "", TermKind.PRATYAYA, upadesha = "सुँ",
                itProcessingPhase = ItProcessingPhase.PROCESSED)) else emptyList()) +
            DerivationTerm("right", "क", TermKind.PRATIPADIKA),
            samjnas = if (designated) setOf(SamjnaAssignment(term.id, Samjna.PADA)) else emptySet())
    }

    @Test fun `term-final anusvara is mandatory unless grammar designates a pada boundary`() {
        val internal = state("अं", designated = false)
        assertTrue(AnusvarasyaYayiParasavarnahSutra.matches(internal))
        assertFalse(VaPadantasyaSutra.matches(internal))
        val external = state("अं", designated = true)
        assertFalse(AnusvarasyaYayiParasavarnahSutra.matches(external))
        assertTrue(VaPadantasyaSutra.matches(external))
    }

    @Test fun `pada on another term does not license optional parasavarna`() {
        val context = state("अं", designated = false).copy(samjnas = setOf(SamjnaAssignment("right", Samjna.PADA)))
        assertFalse(VaPadantasyaSutra.matches(context))
        assertTrue(AnusvarasyaYayiParasavarnahSutra.matches(context))
    }

    @Test fun `empty affix does not conceal a following sound or lose provenance`() {
        val original = state("अम्", designated = true, gap = true)
        assertTrue(MonusvarahSutra.matches(original))
        val anusvara = MonusvarahSutra.apply(original).state
        assertTrue(VaPadantasyaSutra.matches(anusvara))
        val nasal = VaPadantasyaSutra.apply(anusvara).state
        assertEquals("अङ्", nasal.terms.first().surface)
        assertEquals(original.terms.drop(1), nasal.terms.drop(1))
        assertEquals(original.terms.map { it.id }, nasal.terms.map { it.id })
    }

    @Test fun `upasarga retains its existing word boundary treatment`() {
        val context = state("अं", designated = false).copy(samjnas = setOf(SamjnaAssignment("left", Samjna.UPASARGA)))
        assertTrue(VaPadantasyaSutra.matches(context))
        assertFalse(AnusvarasyaYayiParasavarnahSutra.matches(context))
    }

    @Test fun `nonfinal nasal is not misclassified by pada designation on its term`() {
        val context = state("अन्त", designated = true)
        assertTrue(NashcapadantasyaSutra.matches(context))
        assertFalse(MonusvarahSutra.matches(context))
    }
}
