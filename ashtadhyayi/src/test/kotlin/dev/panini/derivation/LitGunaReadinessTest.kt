package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada3.SarvadhatukardhadhatukayohSutra
import dev.panini.core.Lakara
import dev.panini.core.TingAffix
import kotlin.test.*

class LitGunaReadinessTest {
    private fun state(phase: ItProcessingPhase): DerivationState = DerivationState(
        listOf(DerivationTerm("root", "नी", TermKind.DHATU),
            DerivationTerm("ending", "अ", TermKind.PRATYAYA, sourceTingAffix = TingAffix.MIP,
                itProcessingPhase = phase)),
        activeAdhikaras = setOf("6.4.1"),
        context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)))

    @Test fun `identical affix spelling is not a proxy for unfinished it processing`() {
        val raw = state(ItProcessingPhase.RAW_UPADESHA)
        val processed = state(ItProcessingPhase.PROCESSED)
        assertEquals(raw.terms.last().surface, processed.terms.last().surface)
        assertEquals(raw.terms.last().upadesha, processed.terms.last().upadesha)
        assertFalse(SarvadhatukardhadhatukayohSutra.matches(raw))
        assertTrue(SarvadhatukardhadhatukayohSutra.matches(processed))
        val changed = SarvadhatukardhadhatukayohSutra.apply(processed).state
        assertEquals("ने", changed.terms.first().surface)
        assertEquals(processed.terms.last(), changed.terms.last())
        assertEquals(1, changed.substitutions.single().sourceVarnaIndex)
    }
}
