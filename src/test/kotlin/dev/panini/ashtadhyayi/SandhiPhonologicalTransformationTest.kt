package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya8.pada4.JhayoHonyatarasyamSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.ShashChoAtiSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.TorliSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SandhiPhonologicalTransformationTest {

    @Test
    fun `test TorliSutra transforms ta-varga to l before l`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("t1", "तत्", TermKind.PRATIPADIKA, upadesha = "तत्"),
                DerivationTerm("t2", "लयः", TermKind.PRATIPADIKA, upadesha = "लयः")
            )
        )
        assertTrue(TorliSutra.matches(state))
        val change = TorliSutra.apply(state)
        assertEquals("तल्", change.state.terms[0].surface)
    }

    @Test
    fun `test JhayoHonyatarasyamSutra transforms h to 4th varna after jhay stop`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("t1", "तद्", TermKind.PRATIPADIKA, upadesha = "तद्"),
                DerivationTerm("t2", "हितम्", TermKind.PRATIPADIKA, upadesha = "हितम्")
            )
        )
        assertTrue(JhayoHonyatarasyamSutra.matches(state))
        val change = JhayoHonyatarasyamSutra.apply(state)
        assertEquals("धितम्", change.state.terms[1].surface)
    }

    @Test
    fun `test ShashChoAtiSutra transforms sha to cha after jhay stop before at`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("t1", "तत्", TermKind.PRATIPADIKA, upadesha = "तत्"),
                DerivationTerm("t2", "शिवः", TermKind.PRATIPADIKA, upadesha = "शिवः")
            )
        )
        assertTrue(ShashChoAtiSutra.matches(state))
        val change = ShashChoAtiSutra.apply(state)
        assertEquals("छिवः", change.state.terms[1].surface)
    }
}
