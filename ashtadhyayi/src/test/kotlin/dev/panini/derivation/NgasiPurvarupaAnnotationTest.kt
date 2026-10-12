package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.NasiNgasoshCaSutra
import dev.panini.core.SupAffix
import dev.panini.shiksha.*
import kotlin.test.*

class NgasiPurvarupaAnnotationTest {
    @Test fun `internal purvarupa preserves surviving annotations without inventing avagraha`() {
        for (identity in listOf(SupAffix.NGASI, SupAffix.NGAS)) {
            val stem = DerivationTerm("stem", "अग्नेँ", TermKind.PRATIPADIKA)
            val affix = DerivationTerm("affix", "असँऽ", TermKind.PRATYAYA,
                upadesha = identity.upadesha, createdBySutra = "fixture")
            val before = DerivationState(listOf(stem, affix))
            assertTrue(NasiNgasoshCaSutra.matches(before))
            val after = NasiNgasoshCaSutra.apply(before).state
            assertEquals(stem, after.terms.first())
            val changed = after.terms.last()
            assertEquals(affix.varnas.drop(1), changed.varnas)
            assertTrue(changed.phonologicalText.effectiveVarnas.last().nasalized)
            assertEquals(1, changed.orthographicSigns.size)
            assertEquals(changed.varnas.size, changed.orthographicSigns.single().afterVarnaCount)
            assertEquals(affix.upadesha, changed.upadesha)
            assertEquals(affix.createdBySutra, changed.createdBySutra)
            assertEquals(0, after.substitutions.single().sourceVarnaIndex)
            assertFalse(NasiNgasoshCaSutra.matches(after))
        }
    }
}
