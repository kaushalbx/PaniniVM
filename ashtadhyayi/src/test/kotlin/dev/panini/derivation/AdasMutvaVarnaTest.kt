package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada2.AdoAserAsiDoMahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.EtaIdBahuvacaneSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class AdasMutvaVarnaTest {
    private fun state(surface: String, vacana: Vacana = Vacana.DVIVACANA,
        affix: SupAffix = if (vacana == Vacana.BAHUVACANA) SupAffix.JAS else SupAffix.AU): DerivationState = DerivationState(
        listOf(DerivationTerm("stem", surface, TermKind.PRATIPADIKA, upadesha = "अदस्")),
        droppedTerms = listOf(DerivationTerm("affix", "", TermKind.PRATYAYA, upadesha = affix.upadesha,
            sourceSupAffix = affix, mergedIntoTermId = "stem", mergedAffixVowelFromEnd = 0)),
        stage = DerivationStage.PADA_FORMED, context = DerivationalContext(rupa = Rupa(vacana = vacana)),
    ).addVarnaSubstitution("stem", Vyanjana.SA, listOf(Svara.A), "7.2.102")

    @Test fun `mutva preserves duration after preceding vowel sandhi`() {
        for ((source, expected) in listOf("अदम्" to "अमुम्", "अदौ" to "अमू", "अदान्" to "अमून्", "अदया" to "अमुया")) {
            val result = AdoAserAsiDoMahSutra.apply(state(source)).state
            assertEquals(expected, result.surface)
            assertEquals(DerivationStage.PADA_FORMED, result.stage)
            assertEquals(state(source).droppedTerms, result.droppedTerms)
            assertEquals(1, result.substitutions.single { it.sutra == "8.2.80" }.sourceVarnaIndex)
        }
    }

    @Test fun `plural e specialization remains phonological and preserves following material`() {
        assertEquals("अमी", EtaIdBahuvacaneSutra.apply(state("अदे", Vacana.BAHUVACANA)).state.surface)
        assertEquals("अमीभ्यस्", EtaIdBahuvacaneSutra.apply(state("अदेभ्यस्", Vacana.BAHUVACANA, SupAffix.BHYAS_4)).state.surface)
    }

    @Test fun `unrelated annotations and term metadata survive exact replacements`() {
        val original = state("अँदौँऽ")
        val result = AdoAserAsiDoMahSutra.apply(original).state
        val term = result.terms.single()
        assertEquals(listOf(Svara.A, Vyanjana.MA, Svara.UU), term.varnas)
        assertTrue(term.phonologicalText.effectiveVarnas.first().nasalized)
        assertTrue(term.phonologicalText.effectiveVarnas.last().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 3)), term.orthographicSigns)
        assertEquals(original.terms.single().upadesha, term.upadesha)
    }

    @Test fun `s r o and unlicensed lexical forms are excluded`() {
        for (source in listOf("अदस्", "अदर्", "अदो", "अदः", "असौ", "अमू")) {
            assertFalse(AdoAserAsiDoMahSutra.matches(state(source).copy(substitutions = emptyList())), source)
        }
        assertFalse(AdoAserAsiDoMahSutra.matches(state("अदौ").copy(droppedTerms = emptyList())))
    }

    @Test fun `81 overrides the general grade with its own trace after sup incorporation`() {
        val original = state("अदे", Vacana.BAHUVACANA)
        assertTrue(AdoAserAsiDoMahSutra.matches(original))
        assertTrue(EtaIdBahuvacaneSutra.matches(original))
        assertTrue(original.terms.none { it.kind == TermKind.PRATYAYA })
        val result = DerivationEngine(listOf(AdoAserAsiDoMahSutra, EtaIdBahuvacaneSutra)).derive(
            original, DerivationConfig(computeSvara = false))
        assertEquals("अमी", result.final.surface)
        assertEquals(listOf("8.2.81"), result.applications.map { it.sutra })
        assertTrue(result.applications.single().conflictTrace.any { it.contains("8.2.80") })
        assertEquals(DerivationStage.FINAL, result.final.stage)
        assertEquals(original.droppedTerms, result.final.droppedTerms)
    }

    @Test fun `81 requires semantic plurality exact e and the actual source domain`() {
        assertFalse(EtaIdBahuvacaneSutra.matches(state("अदे", Vacana.DVIVACANA)))
        assertFalse(EtaIdBahuvacaneSutra.matches(state("अदि", Vacana.BAHUVACANA)))
        assertFalse(EtaIdBahuvacaneSutra.matches(state("अदौ", Vacana.BAHUVACANA)))
        assertFalse(EtaIdBahuvacaneSutra.matches(state("अदे", Vacana.BAHUVACANA).copy(substitutions = emptyList())))
        assertFalse(EtaIdBahuvacaneSutra.matches(state("अदे", Vacana.BAHUVACANA).copy(droppedTerms = emptyList())))
    }

    @Test fun `81 preserves nasalization signs and provenance at its exact loci`() {
        val original = state("अँदेँऽ", Vacana.BAHUVACANA)
        val result = EtaIdBahuvacaneSutra.apply(original).state
        val term = result.terms.single()
        assertEquals(listOf(Svara.A, Vyanjana.MA, Svara.II), term.varnas)
        assertEquals(2, term.phonologicalText.effectiveVarnas.count { it.nasalized })
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 3)), term.orthographicSigns)
        assertEquals(original.terms.single().upadesha, term.upadesha)
        assertEquals(original.droppedTerms, result.droppedTerms)
        assertEquals(original.stage, result.stage)
        assertEquals(1, result.substitutions.single { it.sutra == "8.2.81" }.sourceVarnaIndex)
        assertFalse(EtaIdBahuvacaneSutra.matches(result))
        assertFalse(AdoAserAsiDoMahSutra.matches(result))
    }
}
