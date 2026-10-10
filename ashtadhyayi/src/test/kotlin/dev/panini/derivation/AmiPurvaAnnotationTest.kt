package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.AmiPurvahSutra
import dev.panini.core.SupAffix
import dev.panini.shiksha.*
import kotlin.test.*

class AmiPurvaAnnotationTest {
    @Test fun `am purvarupa preserves stem annotations and typed suffix provenance`() {
        val stem = DerivationTerm("stem", "कँविँऽ", TermKind.PRATIPADIKA, createdBySutra = "fixture")
        val affix = DerivationTerm("arbitrary-am-id", "अम्ऽ", TermKind.PRATYAYA,
            upadesha = SupAffix.AM.upadesha, sourceSupAffix = SupAffix.AM)
        val before = DerivationState(listOf(stem, affix), stage = DerivationStage.PRATYAYA_SELECTED)
        assertTrue(AmiPurvahSutra.matches(before))
        val after = AmiPurvahSutra.apply(before).state
        val result = after.terms.single()
        assertEquals(stem.varnas + Vyanjana.MA, result.varnas)
        assertEquals(listOf(false, true, false, true, false), result.phonologicalText.effectiveVarnas.map { it.nasalized })
        assertEquals(listOf(4, 5), result.orthographicSigns.map { it.afterVarnaCount })
        assertEquals(stem.upadesha, result.upadesha)
        assertEquals(stem.createdBySutra, result.createdBySutra)
        val dropped = after.droppedTerms.single()
        assertEquals(affix.surface, dropped.originalSurfaceBeforeDrop)
        assertEquals(stem.id, dropped.mergedIntoTermId)
        assertEquals(0, dropped.mergedAffixVowelFromEnd)
        assertEquals(SupAffix.AM, dropped.sourceSupAffix)
        assertEquals(0, after.substitutions.single().sourceVarnaIndex)
        assertFalse(AmiPurvahSutra.matches(before.replaceTerm(affix.id,
            affix.copy(kind = TermKind.PRATIPADIKA))))
    }
}
