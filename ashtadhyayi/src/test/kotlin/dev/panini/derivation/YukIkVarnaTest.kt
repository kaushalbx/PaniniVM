package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada3.ThasyaIkahSutra
import kotlin.test.*

class YukIkVarnaTest {
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
