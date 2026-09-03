package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya5.pada1.TenaTulyamKriyaCedVatihSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.PancamyasTasilSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.SaptamyasTralSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TaddhitaSutraDerivationTest {

    @Test
    fun `test TenaTulyamKriyaCedVatihSutra prescribes vatih affix`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("pratipadika", "ब्राह्मण", TermKind.PRATIPADIKA, upadesha = "ब्राह्मण"))
        )
        assertTrue(TenaTulyamKriyaCedVatihSutra.matches(state))
        val change = TenaTulyamKriyaCedVatihSutra.apply(state)
        val addedTerm = change.state.allEffectiveTerms.last()
        assertEquals("वत्", addedTerm.upadesha)
        assertEquals("वत्", addedTerm.surface)
    }

    @Test
    fun `test PancamyasTasilSutra prescribes tasil affix`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("pratipadika", "तद्", TermKind.PRATIPADIKA, upadesha = "तद्"))
        )
        assertTrue(PancamyasTasilSutra.matches(state))
        val change = PancamyasTasilSutra.apply(state)
        val addedTerm = change.state.allEffectiveTerms.last()
        assertEquals("तसिल्", addedTerm.upadesha)
        assertEquals("तस्", addedTerm.surface)
    }

    @Test
    fun `test SaptamyasTralSutra prescribes tral affix`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("pratipadika", "सर्व", TermKind.PRATIPADIKA, upadesha = "सर्व"))
        )
        assertTrue(SaptamyasTralSutra.matches(state))
        val change = SaptamyasTralSutra.apply(state)
        val addedTerm = change.state.allEffectiveTerms.last()
        assertEquals("त्रल्", addedTerm.upadesha)
        assertEquals("त्रल्", addedTerm.surface)
        assertEquals(dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA, addedTerm.itProcessingPhase)
    }
}
