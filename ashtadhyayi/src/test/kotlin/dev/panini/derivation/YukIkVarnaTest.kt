package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada3.ThasyaIkahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.VatoYukSutra
import kotlin.test.*

class YukIkVarnaTest {
    @Test
    fun `yuk recognizes annotated varnas without widening its existing domain`() {
        for (surface in listOf("वाँऽ", "पाँऽ", "मँऽ")) {
            val root = DerivationTerm("root", surface, TermKind.DHATU)
            val suffix = DerivationTerm("suffix", "उँऽ", TermKind.PRATYAYA)
            val original = DerivationState(listOf(root, suffix))
            assertTrue(VatoYukSutra.matches(original))
            val result = VatoYukSutra.apply(original).state
            assertEquals(root, result.terms.first())
            assertEquals(suffix, result.terms.last())
            val augment = result.terms[1]
            assertEquals("युक्", augment.upadesha)
            assertEquals(ItProcessingPhase.RAW_UPADESHA, augment.itProcessingPhase)
            assertEquals(root.id, augment.augmentTargetId)
            assertEquals(NonOperativeUpadeshaFunction.UCCARANARTHA, augment.nonOperativeUpadeshaSegments.single().function)
            assertFalse(VatoYukSutra.matches(result))
            assertFalse(VatoYukSutra.matches(original.replaceTerm(suffix.id, suffix.copy(surface = "ऊ"))))
        }
        assertFalse(VatoYukSutra.matches(DerivationState(listOf(
            DerivationTerm("root", "मा", TermKind.DHATU), DerivationTerm("suffix", "उ", TermKind.PRATYAYA)))))
    }

    @Test
    fun `ik substitution selects only the processed affix it matched`() {
        val raw = DerivationTerm("raw", "ठ", TermKind.PRATYAYA, upadesha = "ठक्", itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        for (upadesha in listOf("ठक्", "ठच्", "ष्ठन्")) {
            val processed = DerivationTerm("processed", "ठँऽ", TermKind.PRATYAYA, upadesha = upadesha)
            val original = DerivationState(listOf(raw, processed))
            assertTrue(ThasyaIkahSutra.matches(original))
            val result = ThasyaIkahSutra.apply(original).state
            assertEquals(raw, result.terms.first())
            assertEquals("इक", result.terms.last().surface)
            assertEquals(processed.id, result.terms.last().id)
            assertEquals(upadesha, result.terms.last().upadesha)
            assertFalse(ThasyaIkahSutra.matches(result))
        }
        assertFalse(ThasyaIkahSutra.matches(DerivationState(listOf(raw))))
    }
}
