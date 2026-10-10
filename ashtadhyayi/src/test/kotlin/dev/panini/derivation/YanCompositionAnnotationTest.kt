package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.IkoYanAciSutra
import dev.panini.shiksha.*
import kotlin.test.*

class YanCompositionAnnotationTest {
    @Test fun `siyut redistribution retains its nasal vowel and remainder without consuming augment`() {
        val left = DerivationTerm("left", "इँऽ", TermKind.PRATIPADIKA)
        val right = DerivationTerm("siyut", "ईँऽय्ँऽ", TermKind.AGAMA, upadesha = "सीयुट्")
        val after = IkoYanAciSutra.apply(DerivationState(listOf(left, right))).state
        assertEquals(listOf(Vyanjana.YA, Svara.II), after.terms.first().varnas)
        assertTrue(after.terms.first().phonologicalText.effectiveVarnas.all { it.nasalized })
        assertEquals(listOf(1, 2), after.terms.first().orthographicSigns.map { it.afterVarnaCount })
        assertEquals(listOf(Vyanjana.YA), after.terms.last().varnas)
        assertTrue(after.terms.last().phonologicalText.effectiveVarnas.single().nasalized)
        assertEquals("सीयुट्", after.terms.last().upadesha)
        assertTrue(after.droppedTerms.isEmpty())
        assertEquals(0, after.substitutions.single().sourceVarnaIndex)
    }

    @Test fun `yan composition preserves nasal semivowel and both terms signs`() {
        val left = DerivationTerm("left", "इँऽ", TermKind.PRATIPADIKA, createdBySutra = "fixture")
        val right = DerivationTerm("right", "अँऽ", TermKind.PRATIPADIKA)
        val before = DerivationState(listOf(left, right), stage = DerivationStage.PADA_FORMED)
        assertTrue(IkoYanAciSutra.matches(before))
        val after = IkoYanAciSutra.apply(before).state
        val result = after.terms.single()
        assertEquals(listOf(Vyanjana.YA, Svara.A), result.varnas)
        assertEquals(listOf(true, true), result.phonologicalText.effectiveVarnas.map { it.nasalized })
        assertEquals(listOf(1, 2), result.orthographicSigns.map { it.afterVarnaCount })
        assertEquals(left.id, result.id)
        assertEquals(left.upadesha, result.upadesha)
        assertEquals(left.createdBySutra, result.createdBySutra)
        assertEquals(right.id, after.droppedTerms.single().id)
        assertEquals(0, after.substitutions.single().sourceVarnaIndex)
    }
}
