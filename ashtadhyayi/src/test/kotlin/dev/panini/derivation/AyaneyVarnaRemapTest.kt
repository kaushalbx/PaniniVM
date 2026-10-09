package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.AyaneyInIyiyahSutra
import dev.panini.core.ItMarker
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AyaneyVarnaRemapTest {
    @Test
    fun `all five substitutions remap the final it by token position`() {
        for ((source, expected) in listOf("फक्" to "आयनक्", "ढक्" to "एय्क्",
            "खक्" to "ईन्क्", "छक्" to "ईय्क्", "घक्" to "इय्क्")) {
            val raw = DerivationTerm("affix", source, TermKind.PRATYAYA,
                itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
                createdBySutra = "test")
            var state = HalantyamSutra.apply(DerivationState(listOf(raw))).state
            assertTrue(AyaneyInIyiyahSutra.matches(state), source)
            state = AyaneyInIyiyahSutra.apply(state).state
            val term = state.terms.single()
            assertEquals(expected, term.surface, source)
            assertEquals(setOf(term.varnas.lastIndex), term.itDesignations.single().varnaIndices)
            assertEquals("क्", term.itDesignations.single().designatedText)
            assertEquals(raw.id, term.id)
            assertEquals(raw.upadesha, term.upadesha)
            assertEquals(raw.createdBySutra, term.createdBySutra)
            assertFalse(AyaneyInIyiyahSutra.matches(state))
            val processed = TasyaLopahSutra.apply(state).state
            assertEquals(term.varnas.dropLast(1), processed.terms.single().varnas)
        }
    }

    @Test
    fun `initial designation is explicitly consumed and final designation survives`() {
        val raw = DerivationTerm("affix", "खक्", TermKind.PRATYAYA,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        val final = HalantyamSutra.apply(DerivationState(listOf(raw))).state.terms.single()
        val designated = final.copy(itDesignations = final.itDesignations +
            final.designateVarnaIt(0, ItMarker.KHIT, "1.3.8"))
        val result = AyaneyInIyiyahSutra.apply(DerivationState(listOf(designated))).state.terms.single()
        assertEquals(listOf("1.3.3"), result.itDesignations.map { it.sutra })
        assertTrue(ItMarker.KHIT in result.sthaniProps!!.itMarkers)
        assertEquals("खक्", result.sthaniProps!!.upadesha)
    }

    @Test
    fun `root context and missing terminal designation retain the original narrow domain`() {
        val raw = DerivationTerm("affix", "घञ्", TermKind.PRATYAYA,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        assertFalse(AyaneyInIyiyahSutra.matches(DerivationState(listOf(raw))))
        val designated = HalantyamSutra.apply(DerivationState(listOf(raw))).state.terms.single()
        assertFalse(AyaneyInIyiyahSutra.matches(DerivationState(listOf(
            DerivationTerm("root", "भू", TermKind.DHATU), designated))))
    }
}
