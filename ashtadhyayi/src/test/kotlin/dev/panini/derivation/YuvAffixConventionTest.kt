package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada1.YuvoranakauSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.*
import dev.panini.core.YuvAffixConvention
import kotlin.test.*

class YuvAffixConventionTest {
    private val rule = YuvoranakauSutra
    private fun state(upadesha: String, surface: String, kind: TermKind = TermKind.PRATYAYA,
        phase: ItProcessingPhase = ItProcessingPhase.PROCESSED) = DerivationState(listOf(
        DerivationTerm("affix", surface, kind, upadesha = upadesha, itProcessingPhase = phase)))

    @Test fun `all declared nasal yan affixes receive the ordered substitutions`() {
        for (convention in YuvAffixConvention.entries) for (upadesha in convention.upadeshas) {
            val yu = convention == YuvAffixConvention.YU
            val initial = state(upadesha, if (yu) "यु" else "वु")
            assertTrue(rule.matches(initial), upadesha)
            val result = rule.apply(initial).state
            assertEquals(if (yu) "अन" else "अक", result.surface)
            assertEquals(upadesha, result.terms.single().upadesha)
            assertEquals(0, result.substitutions.single().sourceVarnaIndex)
            assertEquals(initial.surface, result.substitutions.single().originalSurface)
            assertFalse(rule.matches(result))
            result.requireCompleteItProcessing()
        }
    }

    @Test fun `roots unproven yu and nonnasal yus cannot masquerade as these affixes`() {
        assertFalse(rule.matches(state("यु", "यु", TermKind.DHATU)))
        assertFalse(rule.matches(state("यु", "यु")))
        assertFalse(rule.matches(state("युस्", "यु")))
        assertFalse(rule.matches(state("ल्युट्", "यु", phase = ItProcessingPhase.RAW_UPADESHA)))
        assertFalse(rule.matches(state("ल्युट्", "वु")))
        assertFalse(rule.matches(state("वुन्", "यु")))
    }

    @Test fun `raw affix it proofs survive substitution without reentering it processing`() {
        val lifecycle = DerivationEngine(listOf(HalantyamSutra, ChutuSutra, LasakvataddhiteSutra, TasyaLopahSutra))
        for ((raw, expected) in listOf("ल्युट्" to "अन", "ण्वुल्" to "अक")) {
            val initial = state(raw, raw, phase = ItProcessingPhase.RAW_UPADESHA)
            val processed = lifecycle.derive(initial).final
            assertTrue(rule.matches(processed))
            val original = processed.terms.single()
            assertTrue(original.itMarkerProvenance.isNotEmpty())
            val result = rule.apply(processed).state
            assertEquals(expected, result.surface)
            assertEquals(original.itMarkerProvenance, result.terms.single().itMarkerProvenance)
            assertEquals(original.upadesha, result.terms.single().upadesha)
            assertEquals(ItProcessingPhase.PROCESSED, result.terms.single().itProcessingPhase)
            assertFalse(HalantyamSutra.matches(result))
            result.requireCompleteItProcessing()
        }
    }

    @Test fun `explicit nasal yan proof is distinct from nasal u and written signs survive`() {
        assertFalse(rule.matches(state("युँ", "यु")))
        val initial = state("य्ँउ", "युऽ")
        assertEquals(YuvAffixConvention.YU, initial.terms.single().yuvAffixConvention)
        assertTrue(rule.matches(initial))
        val result = rule.apply(initial).state
        assertEquals("अनऽ", result.surface)
        assertEquals(initial.terms.single().id, result.terms.single().id)
    }
}
