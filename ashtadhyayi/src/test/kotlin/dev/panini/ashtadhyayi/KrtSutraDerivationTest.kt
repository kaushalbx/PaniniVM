package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya3.pada1.IgupadhaJnyaPriKirahKahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.SupiSthahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.AbhikshnyeNamulCaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.LasakvataddhiteSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.ItProcessingPhase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KrtSutraDerivationTest {

    @Test
    fun `test AbhikshnyeNamulCaSutra prescribes namul affix in bhava sense`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("dhatu", "पा", TermKind.DHATU, upadesha = "पा")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(AbhikshnyeNamulCaSutra.matches(state))
        val change = AbhikshnyeNamulCaSutra.apply(state)
        val addedTerm = change.state.allEffectiveTerms.last()
        assertEquals("णमुल्", addedTerm.upadesha)
        assertEquals("णमुँल्", addedTerm.surface)
        assertTrue(addedTerm.itProcessingPending)
    }

    @Test
    fun `test IgupadhaJnyaPriKirahKahSutra prescribes ka affix for igupadha roots`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("dhatu", "बुध्", TermKind.DHATU, upadesha = "बुध्"))
        )
        assertTrue(IgupadhaJnyaPriKirahKahSutra.matches(state))
        val change = IgupadhaJnyaPriKirahKahSutra.apply(state)
        val addedTerm = change.state.allEffectiveTerms.last()
        assertEquals("क", addedTerm.upadesha)
        assertEquals("क", addedTerm.surface)
        assertEquals("3.1.135", addedTerm.createdBySutra)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, addedTerm.itProcessingPhase)
        val designated = LasakvataddhiteSutra.apply(change.state).state
        assertEquals("1.3.8", designated.terms.last().itDesignations.single().sutra)
        assertEquals("अ", TasyaLopahSutra.apply(designated).state.terms.last().surface)
    }

    @Test
    fun `test SupiSthahSutra prescribes ka affix for stha root with subanta upapada`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("upapada", "गृह", TermKind.PRATIPADIKA, upadesha = "गृह"),
                DerivationTerm("dhatu", "स्था", TermKind.DHATU, upadesha = "स्था")
            )
        )
        assertTrue(SupiSthahSutra.matches(state))
        val change = SupiSthahSutra.apply(state)
        val addedTerm = change.state.allEffectiveTerms.last()
        assertEquals("क", addedTerm.upadesha)
        assertEquals("क", addedTerm.surface)
        assertEquals("3.2.78", addedTerm.createdBySutra)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, addedTerm.itProcessingPhase)
        val designated = LasakvataddhiteSutra.apply(change.state).state
        assertEquals("1.3.8", designated.terms.last().itDesignations.single().sutra)
        assertEquals("अ", TasyaLopahSutra.apply(designated).state.terms.last().surface)
    }
}
