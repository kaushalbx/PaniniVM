package dev.panini.derivation

import dev.panini.core.FrequencyAffix
import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.LasakvataddhiteSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.DhralopePurvasyaDirghonahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.DhoDheLopaSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.SankhyayahKriyaAbhyavrttiKrtvasucSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import kotlin.test.*

class FrequencyAffixLifecycleTest {
    @Test fun `taddhita exclusion is target specific and lopa lengthening is causal`() {
        val raw = FrequencyAffix.KRTVASUC.rawTerm("affix", "fixture")
        val taddhita = DerivationState(listOf(raw), samjnas = setOf(SamjnaAssignment(raw.id, Samjna.TADDHITA)))
        assertFalse(LasakvataddhiteSutra.matches(taddhita))
        assertTrue(LasakvataddhiteSutra.matches(taddhita.copy(samjnas = setOf(SamjnaAssignment("other", Samjna.TADDHITA)))))
        val original = DerivationState(listOf(DerivationTerm("unrelated", "अ", TermKind.PRATIPADIKA),
            DerivationTerm("left", "इँढ्", TermKind.PRATIPADIKA), DerivationTerm("right", "ढ", TermKind.PRATIPADIKA)))
        val deleted = DhoDheLopaSutra.apply(original).state
        assertTrue(DhralopePurvasyaDirghonahSutra.matches(deleted))
        val result = DhralopePurvasyaDirghonahSutra.apply(deleted).state
        assertEquals(original.terms.first(), result.terms.first())
        assertEquals("ईँ", result.terms[1].surface)
        assertFalse(DhralopePurvasyaDirghonahSutra.matches(result))
        val visarga = original.copy(substitutions = listOf(VarnaSubstitution("unrelated", 'र', "ः", "8.3.15")))
        assertFalse(DhralopePurvasyaDirghonahSutra.matches(visarga))
    }
    @Test fun `frequency raw terms retain pronunciation material without treating it as it`() {
        for ((affix, expected) in listOf(FrequencyAffix.KRTVASUC to "कृत्वस्", FrequencyAffix.SUC to "स्")) {
            val raw = affix.rawTerm("arbitrary", "fixture")
            assertEquals(affix.upadesha, raw.upadesha)
            assertEquals(ItProcessingPhase.RAW_UPADESHA, raw.itProcessingPhase)
            assertEquals("ु", raw.nonOperativeUpadeshaSegments.single().text)
            assertEquals(NonOperativeUpadeshaFunction.UCCARANARTHA, raw.nonOperativeUpadeshaSegments.single().function)
            assertFalse(Svara.U in raw.varnas)
            assertEquals(Vyanjana.CA, raw.varnas.last())
            val initial = DerivationState(listOf(raw))
            val designated = HalantyamSutra.apply(initial).state
            assertEquals(setOf(raw.varnas.lastIndex), designated.terms.single().itDesignations.single().varnaIndices)
            val processed = TasyaLopahSutra.apply(designated).state
            assertEquals(expected, processed.surface)
            assertEquals(raw.id, processed.terms.single().id)
            assertEquals(raw.upadesha, processed.terms.single().upadesha)
            assertEquals(raw.nonOperativeUpadeshaSegments, processed.terms.single().nonOperativeUpadeshaSegments)
            assertEquals("च्", processed.terms.single().itMarkerProvenance.single().designatedText)
            processed.requireCompleteItProcessing()
        }
    }

    @Test fun `krtvasuc selection preserves numeral and requests explicit it processing`() {
        val stem = DerivationTerm("base", "पञ्चँऽ", TermKind.PRATIPADIKA, upadesha = "पञ्चन्")
        val initial = DerivationState(listOf(stem), samjnas = setOf(SamjnaAssignment(stem.id, Samjna.KRTVASUC)))
        assertTrue(SankhyayahKriyaAbhyavrttiKrtvasucSutra.matches(initial))
        val selected = SankhyayahKriyaAbhyavrttiKrtvasucSutra.apply(initial).state
        assertEquals(stem, selected.terms.first())
        assertEquals(initial.samjnas + SamjnaAssignment(selected.terms.last().id, Samjna.TADDHITA), selected.samjnas)
        assertEquals("5.4.17", selected.terms.last().createdBySutra)
        assertFailsWith<IllegalArgumentException> { selected.requireCompleteItProcessing() }
        val processed = TasyaLopahSutra.apply(HalantyamSutra.apply(selected).state).state
        assertEquals("कृत्वस्", processed.terms.last().surface)
        assertFalse(SankhyayahKriyaAbhyavrttiKrtvasucSutra.matches(processed))
    }
}
