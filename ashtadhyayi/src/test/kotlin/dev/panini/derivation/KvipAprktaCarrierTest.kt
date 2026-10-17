package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada3.*
import dev.panini.ashtadhyayi.adhyaya6.pada1.VerAprktasyaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.SatsudvisatrusadvisavahaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.AnyaebhyopiDrshyateSutra
import dev.panini.core.KrtAffix
import dev.panini.shiksha.*
import kotlin.test.*

class KvipAprktaCarrierTest {
    private val itEngine = DerivationEngine(listOf(LasakvataddhiteSutra, HalantyamSutra, TasyaLopahSutra))

    @Test fun `producers preserve upadesha but project pronunciation only i at the boundary`() {
        val initial = DerivationState(listOf(DerivationTerm("root", "सद्", TermKind.DHATU)))
        for (rule in listOf(SatsudvisatrusadvisavahaSutra, AnyaebhyopiDrshyateSutra)) {
            val raw = rule.apply(initial).state.terms.last()
            assertEquals("क्विप्", raw.upadesha)
            assertEquals(listOf(Vyanjana.KA, Vyanjana.VA, Vyanjana.PA), raw.varnas)
            assertEquals("ि", raw.nonOperativeUpadeshaSegments.single().text)
            assertEquals(ItProcessingPhase.RAW_UPADESHA, raw.itProcessingPhase)
        }
    }

    @Test fun `single v lopa preserves exact it proof identity and orthographic signs`() {
        val processed = itEngine.derive(DerivationState(listOf(KrtAffix.KVIP.rawTerm("kvip", "3.2.61")))).final
        val affix = processed.terms.single()
        assertEquals(listOf(Vyanjana.VA), affix.varnas)
        assertEquals(setOf("1.3.8", "1.3.3"), affix.itMarkerProvenance.map { it.designationSutra }.toSet())
        val annotated = processed.copy(terms = listOf(affix.copy(surface = "व्ऽ")))
        assertTrue(VerAprktasyaSutra.matches(annotated))
        val result = VerAprktasyaSutra.apply(annotated).state
        val empty = result.terms.single()
        assertTrue(empty.varnas.isEmpty())
        assertEquals("ऽ", empty.surface)
        assertEquals(affix.id, empty.id)
        assertEquals(affix.upadesha, empty.upadesha)
        assertEquals(affix.itMarkerProvenance, empty.itMarkerProvenance)
        assertEquals(0, result.substitutions.last().sourceVarnaIndex)
        assertFalse(VerAprktasyaSutra.matches(result))
        result.requireCompleteItProcessing()
    }

    @Test fun `operative vi nonaffix v and raw kvip are not aprkta targets`() {
        fun state(surface: String, kind: TermKind = TermKind.PRATYAYA) = DerivationState(listOf(
            DerivationTerm("term", surface, kind, upadesha = "क्विप्", itProcessingPhase = ItProcessingPhase.PROCESSED)))
        assertFalse(VerAprktasyaSutra.matches(state("वि")))
        assertFalse(VerAprktasyaSutra.matches(state("व्", TermKind.DHATU)))
        assertFalse(VerAprktasyaSutra.matches(DerivationState(listOf(KrtAffix.KVIP.rawTerm("kvip", "3.2.61")))))
    }
}
