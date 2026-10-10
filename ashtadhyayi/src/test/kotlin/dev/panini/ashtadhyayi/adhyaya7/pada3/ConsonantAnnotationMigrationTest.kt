package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.KrtAffix
import dev.panini.core.SanadiAffix
import dev.panini.derivation.*
import kotlin.test.*

class ConsonantAnnotationMigrationTest {
    private fun check(before: DerivationState, after: DerivationState, expected: String, index: Int) {
        val old = before.terms.first { it.id == "root" }
        val changed = after.terms.first { it.id == "root" }
        assertEquals(expected, changed.surface)
        assertEquals(old.id, changed.id)
        assertEquals(old.upadesha, changed.upadesha)
        assertEquals(old.createdBySutra, changed.createdBySutra)
        assertEquals(before.terms.filter { it.id != "root" }, after.terms.filter { it.id != "root" })
        assertEquals(index, after.substitutions.single().sourceVarnaIndex)
    }

    @Test fun `guttural substitution changes only final c or j preserving earlier nasal vowel`() {
        for ((surface, expected) in listOf("पँच्ऽ" to "पँक्ऽ", "यँज्ऽ" to "यँग्ऽ")) {
            for (affix in listOf(KrtAffix.GHAN, KrtAffix.NYAT)) {
                val root = DerivationTerm("root", surface, TermKind.DHATU, upadesha = "lexeme", createdBySutra = "fixture")
                val before = DerivationState(listOf(root, affix.term("suffix")))
                assertTrue(CajoKuGhinnyatohSutra.matches(before))
                val after = CajoKuGhinnyatohSutra.apply(before).state
                check(before, after, expected, root.varnas.lastIndex)
                assertFalse(CajoKuGhinnyatohSutra.matches(after))
            }
        }
    }

    @Test fun `ji guttural substitution preserves nasal vowel and abhyasa`() {
        val root = DerivationTerm("root", "जिँऽ", TermKind.DHATU, upadesha = "जि")
        val before = DerivationState(listOf(DerivationTerm("abhyasa", "जि", TermKind.DHATU), root, SanadiAffix.SAN.term("san")))
        assertTrue(SanlitorJehSutra.matches(before))
        val after = SanlitorJehSutra.apply(before).state
        check(before, after, "गिँऽ", 0)
        assertFalse(SanlitorJehSutra.matches(after))
    }

    @Test fun `cha substitution retains lexical identity and nasalization without reapplying`() {
        for ((upadesha, surface, expected) in listOf(
            Triple("इषुँ", "इँष्ऽ", "इँछ्ऽ"),
            Triple("गमॢँ", "गँम्ऽ", "गँछ्ऽ"),
            Triple("यमँ", "यँम्ऽ", "यँछ्ऽ"),
        )) {
            val root = DerivationTerm("root", surface, TermKind.DHATU, upadesha = upadesha)
            val before = DerivationState(listOf(root, DerivationTerm("shap", "अ", TermKind.PRATYAYA, upadesha = "शप्")))
            assertTrue(IshugamiyamamChahSutra.matches(before))
            val after = IshugamiyamamChahSutra.apply(before).state
            check(before, after, expected, root.varnas.lastIndex)
            assertFalse(IshugamiyamamChahSutra.matches(after))
        }
    }
}
