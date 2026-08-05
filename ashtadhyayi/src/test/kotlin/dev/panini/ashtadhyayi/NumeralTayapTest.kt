package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya5.pada2.DvitribhyamTayasyAyajSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.SankhyayaAvayaveTayapSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.UbhadUdattoNityamSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

import dev.panini.derivation.SamjnaAssignment
import dev.panini.shiksha.Samjna

class NumeralTayapTest {

    @Test
    fun `test SankhyayaAvayaveTayapSutra derives pancatayam and dvitayam`() {
        val statePanca = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "पञ्च", kind = TermKind.PRATIPADIKA, upadesha = "पञ्चन्")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.AVAYAVA))
        )
        assertTrue(SankhyayaAvayaveTayapSutra.matches(statePanca))
        val changePanca = SankhyayaAvayaveTayapSutra.apply(statePanca)
        assertEquals("तय", changePanca.state.terms.last().surface)

        val stateDvi = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "द्वि", kind = TermKind.PRATIPADIKA, upadesha = "द्वि")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.AVAYAVA))
        )
        assertTrue(SankhyayaAvayaveTayapSutra.matches(stateDvi))
        val changeDvi = SankhyayaAvayaveTayapSutra.apply(stateDvi)
        assertEquals("तय", changeDvi.state.terms.last().surface)
    }

    @Test
    fun `test DvitribhyamTayasyAyajSutra matches dvi and tri and derives ayac`() {
        val stateTri = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "त्रि", kind = TermKind.PRATIPADIKA, upadesha = "त्रि")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.AVAYAVA))
        )
        assertTrue(DvitribhyamTayasyAyajSutra.matches(stateTri))
        val changeTri = DvitribhyamTayasyAyajSutra.apply(stateTri)
        assertEquals("अय", changeTri.state.terms.last().surface)
    }

    @Test
    fun `test UbhadUdattoNityamSutra derives ayac for ubha`() {
        val stateUbha = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "उभ", kind = TermKind.PRATIPADIKA, upadesha = "उभ")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.AVAYAVA))
        )
        assertTrue(UbhadUdattoNityamSutra.matches(stateUbha))
        val changeUbha = UbhadUdattoNityamSutra.apply(stateUbha)
        assertEquals("अय", changeUbha.state.terms.last().surface)
    }
}
