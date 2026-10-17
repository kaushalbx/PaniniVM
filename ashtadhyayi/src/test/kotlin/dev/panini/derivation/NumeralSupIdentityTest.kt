package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada1.AstabhyahAushSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.SadbhyoLukSutra
import dev.panini.core.SupAffix
import kotlin.test.*

class NumeralSupIdentityTest {
    private fun state(word: String, suffix: DerivationTerm = SupAffix.JAS.term("renamed")) =
        DerivationState(listOf(DerivationTerm("stem", word, TermKind.PRATIPADIKA, upadesha = "अन्य"), suffix),
            activeAdhikaras = setOf("6.4.1"))

    @Test fun `annotated eight shapes select aush with renamed typed sup`() {
        for (word in listOf("अँष्टन्", "अँष्ट", "अँष्टा")) for (suffix in listOf(SupAffix.JAS, SupAffix.SAS)) {
            val original = state(word, suffix.term("renamed"))
            assertTrue(AstabhyahAushSutra.matches(original), word)
            val changed = AstabhyahAushSutra.apply(original).state
            assertEquals(original.terms.first(), changed.terms.first())
            assertEquals("औश्", changed.terms.last().upadesha)
            assertFalse(AstabhyahAushSutra.matches(changed))
            assertFalse(SadbhyoLukSutra.matches(changed))
        }
    }
    @Test fun `shat shape luk preserves the stem and archives its typed sup`() {
        for (word in listOf("पँञ्चन्", "षष्", "षट्", "सँप्तन्", "अँष्टन्", "नँवन्", "दँशन्")) {
            val original = state(word)
            assertTrue(SadbhyoLukSutra.matches(original), word)
            val changed = SadbhyoLukSutra.apply(original).state
            assertEquals(listOf(original.terms.first()), changed.terms)
            assertEquals(SupAffix.JAS, changed.droppedTerms.single().sourceSupAffix)
            assertEquals("7.1.22", changed.droppedTerms.single().droppedBySutra)
        }
    }
    @Test fun `fake or already substituted affixes cannot reselect the prescriptions`() {
        for (suffix in listOf(DerivationTerm("sup-jas", "जस्", TermKind.PRATYAYA),
            SupAffix.AM.term("sup-jas"), SupAffix.JAS.term("old").copy(upadesha = "शि"))) {
            assertFalse(AstabhyahAushSutra.matches(state("अष्टन्", suffix)))
            assertFalse(SadbhyoLukSutra.matches(state("पञ्चन्", suffix)))
        }
        for (word in listOf("एक", "त्रि", "शत", "षोडश")) assertFalse(SadbhyoLukSutra.matches(state(word)))
    }
}
