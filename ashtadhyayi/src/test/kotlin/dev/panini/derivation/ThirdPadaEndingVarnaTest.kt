package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya3.pada1.HalahShnahShanajJhauSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.VyatyayoBahulamSutra
import dev.panini.core.Lakara
import kotlin.test.*

class ThirdPadaEndingVarnaTest {
    @Test fun `shanac selection uses hi varnas without consuming its annotations`() {
        val rule = HalahShnahShanajJhauSutra
        val state = DerivationState(listOf(
            DerivationTerm("root", "क्रीड्", TermKind.DHATU),
            DerivationTerm("vikarana", "ना", TermKind.PRATYAYA, upadesha = "श्ना"),
            DerivationTerm("ending", "हिँऽ", TermKind.PRATYAYA)))
        assertTrue(rule.matches(state))
        val changed = rule.apply(state).state
        assertEquals("शानच्", changed.terms[1].surface)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, changed.terms[1].itProcessingPhase)
        assertEquals(state.terms.last(), changed.terms.last())
        assertFalse(rule.matches(state.copy(terms = state.terms.dropLast(1) +
            state.terms.last().copy(surface = "ही"))))
    }

    @Test fun `vedic mi substitute preserves untouched vowel and ending identity`() {
        val rule = VyatyayoBahulamSutra
        val ending = DerivationTerm("ending", "मिँऽ", TermKind.PRATYAYA, upadesha = "मिप्")
        val state = DerivationState(listOf(DerivationTerm("root", "भू", TermKind.DHATU), ending),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LET)))
        assertTrue(rule.matches(state))
        val changed = rule.apply(state).state
        assertEquals("निँऽ", changed.terms.last().surface)
        assertEquals(ending.id, changed.terms.last().id)
        assertEquals(ending.upadesha, changed.terms.last().upadesha)
        assertEquals(ending.itMarkerProvenance, changed.terms.last().itMarkerProvenance)
        assertEquals(ending.phonologicalText.effectiveVarnas[1],
            changed.terms.last().phonologicalText.effectiveVarnas[1])
        assertEquals(0, changed.substitutions.single().sourceVarnaIndex)
        assertFalse(rule.matches(changed))
        assertFalse(rule.matches(state.copy(context = DerivationalContext(rupa = Rupa(lakara = Lakara.LAT)))))
        assertFalse(rule.matches(state.copy(terms = listOf(ending.copy(surface = "मी")))))
        assertFalse(rule.matches(state.copy(terms = listOf(ending.copy(upadesha = "तिप्")))))
    }
}
