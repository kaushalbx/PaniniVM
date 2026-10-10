package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.AtoRorAplutadSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.HashiCaSutra
import dev.panini.core.TaddhitaAffix
import dev.panini.shiksha.*
import kotlin.test.*

class GunaRutvaAnnotationTest {
    @Test fun `rutva transforms only repha before internal guna coalescence`() {
        for (following in listOf("अ", "ग")) {
            val left = DerivationTerm("left", "अँर्ऽ", TermKind.PRATIPADIKA,
                createdBySutra = "fixture")
            val right = DerivationTerm("right", following, TermKind.PRATIPADIKA)
            val plain = DerivationState(listOf(left, right), stage = DerivationStage.PADA_FORMED)
            assertFalse(AtoRorAplutadSutra.matches(plain))
            assertFalse(HashiCaSutra.matches(plain))
            val before = plain.addVarnaSubstitution(left.id, Vyanjana.SA, listOf(Vyanjana.RA), "8.2.66")
            val ruRule = if (following == "अ") AtoRorAplutadSutra else HashiCaSutra
            assertTrue(ruRule.matches(before))
            val intermediate = ruRule.apply(before).state
            assertEquals(listOf(Svara.A, Svara.U), intermediate.terms.first().varnas)
            assertTrue(intermediate.terms.first().phonologicalText.effectiveVarnas.first().nasalized)
            assertEquals(1, intermediate.substitutions.last().sourceVarnaIndex)
            assertEquals(right, intermediate.terms.last())
            assertTrue(AdGunaSutra.matches(intermediate))
            val after = AdGunaSutra.apply(intermediate).state
            assertEquals("ओँऽ", after.terms.first().surface)
            assertEquals(left.createdBySutra, after.terms.first().createdBySutra)
            assertEquals(right, after.terms.last())
        }
    }

    @Test fun `ru followup replaces exact final vowel retaining nasalization and signs`() {
        val term = DerivationTerm("term", "अँउऽ", TermKind.PRATIPADIKA, createdBySutra = "fixture")
        val state = DerivationState(listOf(term), stage = DerivationStage.PADA_FORMED)
            .addVarnaSubstitution(term.id, Vyanjana.RA, listOf(Svara.U), "6.1.114")
        assertTrue(AdGunaSutra.matches(state))
        val after = AdGunaSutra.apply(state).state
        val result = after.terms.single()
        assertEquals("ओँऽ", result.surface)
        assertTrue(result.phonologicalText.effectiveVarnas.all { it.nasalized })
        assertEquals(term.upadesha, result.upadesha)
        assertEquals(term.createdBySutra, result.createdBySutra)
        assertEquals(0, after.substitutions.last().sourceVarnaIndex)
        assertFalse(AdGunaSutra.matches(after))
    }

    @Test fun `taddhita exclusion requires an actual current affix`() {
        for (identity in TaddhitaAffix.entries) {
            for (kind in listOf(TermKind.PRATYAYA, TermKind.PRATIPADIKA)) {
                val state = DerivationState(listOf(
                    DerivationTerm("left", "अ", TermKind.PRATIPADIKA),
                    DerivationTerm("right", "इ", kind, upadesha = identity.upadesha),
                ), stage = DerivationStage.PADA_FORMED)
                assertEquals(kind != TermKind.PRATYAYA, AdGunaSutra.matches(state), identity.name)
            }
        }
    }
}
