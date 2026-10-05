package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya3.pada1.HalahShnahShanajJhauSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.AaneMukSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ShnaSubstitutionProvenanceTest {
    @Test
    fun `imperative shanac is a fresh raw substitution not participial ana`() {
        val state = DerivationState(terms = listOf(
            DerivationTerm("root", "ग्रह्", TermKind.DHATU, upadesha = "ग्रहँ"),
            DerivationTerm("shna", "ना", TermKind.PRATYAYA, upadesha = "श्ना", createdBySutra = "3.1.81"),
            DerivationTerm("ending", "हि", TermKind.PRATYAYA, upadesha = "सिप्"),
        ))
        assertTrue(HalahShnahShanajJhauSutra.matches(state))
        val replaced = HalahShnahShanajJhauSutra.apply(state).state
        val affix = replaced.terms.single { it.id == "shna" }
        assertEquals("शानच्", affix.upadesha)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, affix.itProcessingPhase)
        val processed = replaced.replaceTerm("shna", affix.copy(surface = "आन", itProcessingPhase = ItProcessingPhase.PROCESSED))
        assertFalse(AaneMukSutra.matches(processed))
        assertTrue(processed.terms.single { it.id == "shna" }.matchesUpadesha("श्ना"))
        assertEquals(null, dev.panini.ashtadhyayi.adhyaya6.pada4.shna(processed))
        val participle = processed.replaceTerm("shna", affix.copy(
            surface = "आन", itProcessingPhase = ItProcessingPhase.PROCESSED,
            createdBySutra = "3.2.124", establishedBySutras = emptySet(),
        ))
        assertTrue(AaneMukSutra.matches(participle))
    }
}
