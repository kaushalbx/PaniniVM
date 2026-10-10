package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.SavarnaDirghaSutra
import dev.panini.shiksha.*
import kotlin.test.*

class SavarnaCompositionAnnotationTest {
    @Test fun `coalesced affix vowel retains its surviving accent locus`() {
        val stem = DerivationTerm("stem", "अज", TermKind.PRATIPADIKA)
        val affix = DerivationTerm("affix", "आ", TermKind.PRATYAYA, upadesha = "टाप्")
        val after = SavarnaDirghaSutra.apply(DerivationState(listOf(stem, affix))).state
        val dropped = after.droppedTerms.single()
        assertEquals("अजा", after.surface)
        assertEquals(stem.id, dropped.mergedIntoTermId)
        assertEquals(0, dropped.mergedAffixVowelFromEnd)
        assertEquals(affix.surface, dropped.originalSurfaceBeforeDrop)
    }

    @Test fun `ordinary coalescence preserves untouched nasal tokens signs and metadata`() {
        for (leftSurface in listOf("अँइऽ", "अँईऽ")) {
            val left = DerivationTerm("left", leftSurface, TermKind.PRATIPADIKA, createdBySutra = "fixture")
            val right = DerivationTerm("right", "इकँऽ", TermKind.PRATIPADIKA)
            val before = DerivationState(listOf(left, right), stage = DerivationStage.PADA_FORMED)
            assertTrue(SavarnaDirghaSutra.matches(before))
            val after = SavarnaDirghaSutra.apply(before).state
            val result = after.terms.single()
            assertEquals(listOf(Svara.A, Svara.II, Vyanjana.KA, Svara.A), result.varnas)
            assertEquals(listOf(true, false, false, true), result.phonologicalText.effectiveVarnas.map { it.nasalized })
            assertEquals(listOf(2, 4), result.orthographicSigns.map { it.afterVarnaCount })
            assertEquals(left.upadesha, result.upadesha)
            assertEquals(left.createdBySutra, result.createdBySutra)
            assertEquals(right.surface, after.droppedTerms.single().originalSurfaceBeforeDrop)
        }
    }
}
