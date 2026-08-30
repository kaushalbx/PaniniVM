package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya3.pada1.IgupadhaJnyaPriKirahKahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.SupiSthahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.AbhikshnyeNamulCaSutra
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
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
        assertEquals("अ", addedTerm.surface)
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
        assertEquals("अ", addedTerm.surface)
    }
}
