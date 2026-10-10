package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import kotlin.test.*

class FinalVowelAnnotationMigrationTest {
    private fun state(surface: String, affix: DerivationTerm) = DerivationState(
        listOf(DerivationTerm("stem", surface, TermKind.DHATU, upadesha = "source", createdBySutra = "fixture"), affix),
        stage = DerivationStage.ANGAKARYA, activeAdhikaras = setOf("6.4.1"),
        samjnas = setOf(SamjnaAssignment(affix.id, Samjna.SAMBUDDHI)),
    )

    private fun check(before: DerivationState, after: DerivationState, expected: String) {
        val old = before.terms.first()
        val changed = after.terms.first()
        assertEquals(expected, changed.surface)
        assertEquals(old.id, changed.id)
        assertEquals(old.upadesha, changed.upadesha)
        assertEquals(old.createdBySutra, changed.createdBySutra)
        assertEquals(before.terms.last(), after.terms.last())
        assertEquals(before.samjnas, after.samjnas)
        assertEquals(old.varnas.lastIndex, after.substitutions.single().sourceVarnaIndex)
    }

    @Test fun `lengthening preserves nasalized final a and orthographic sign`() {
        val original = state("भवँऽ", TingAffix.MIP.term().copy(surface = "मि"))
        assertTrue(AtoDirghoYaniSutra.matches(original))
        val result = AtoDirghoYaniSutra.apply(original).state
        check(original, result, "भवाँऽ")
        assertFalse(AtoDirghoYaniSutra.matches(result))
    }

    @Test fun `os uses phonological form without discarding annotation`() {
        for (affix in listOf(SupAffix.OS_6, SupAffix.OS_7)) {
            val original = state("रामँऽ", affix.term().copy(surface = "ओँस्"))
            assertTrue(OsiCaSutra.matches(original))
            val result = OsiCaSutra.apply(original).state
            check(original, result, "रामेँऽ")
            assertFalse(OsiCaSutra.matches(result))
            assertFalse(OsiCaSutra.matches(original.copy(terms = listOf(original.terms.first(), original.terms.last().copy(kind = TermKind.DHATU)))))
        }
    }

    @Test fun `sambuddhi e preserves nasalization of aa`() {
        val original = state("मालाँऽ", SupAffix.SU.term())
        assertTrue(SambuddhauCaSutra.matches(original))
        val result = SambuddhauCaSutra.apply(original).state
        check(original, result, "मालेँऽ")
        assertFalse(SambuddhauCaSutra.matches(result))
    }

    @Test fun `general guna preserves nasalization and trace of exact final occurrence`() {
        val original = state("भूँऽ", DerivationTerm("suffix", "अ", TermKind.PRATYAYA))
        check(original, SarvadhatukardhadhatukayohSutra.apply(original).state, "भोँऽ")
    }
}
