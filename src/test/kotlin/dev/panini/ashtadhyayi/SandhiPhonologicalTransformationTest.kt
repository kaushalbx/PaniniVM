package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya6.pada1.EtattadohSulopoKoAnanjparoHaliSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.HaliSarveshamSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.JharoJhariSavarneSutra
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

    @Test
    fun `test EtattadohSulopoKoAnanjparoHaliSutra elides visarga from sah and eshah before hal`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("t1", "सः", TermKind.PRATIPADIKA, upadesha = "सः"),
                DerivationTerm("t2", "गच्छति", TermKind.PRATIPADIKA, upadesha = "गच्छति")
            )
        )
        assertTrue(EtattadohSulopoKoAnanjparoHaliSutra.matches(state))
        val change = EtattadohSulopoKoAnanjparoHaliSutra.apply(state)
        assertEquals("स", change.state.terms[0].surface)
    }

    @Test
    fun `test HaliSarveshamSutra elides y from bhoh before hal`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("t1", "भोः", TermKind.PRATIPADIKA, upadesha = "भोः"),
                DerivationTerm("t2", "देवाः", TermKind.PRATIPADIKA, upadesha = "देवाः")
            )
        )
        assertTrue(HaliSarveshamSutra.matches(state))
        val change = HaliSarveshamSutra.apply(state)
        assertEquals("भो", change.state.terms[0].surface)
    }

    @Test
    fun `test JharoJhariSavarneSutra elides redundant jhar consonant before savarna jhar`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("t1", "तत्", TermKind.PRATIPADIKA, upadesha = "तत्"),
                DerivationTerm("t2", "तन्वा", TermKind.PRATIPADIKA, upadesha = "तन्वा")
            )
        )
        assertTrue(JharoJhariSavarneSutra.matches(state))
        val change = JharoJhariSavarneSutra.apply(state)
        assertEquals("त", change.state.terms[0].surface)
    }
}
