package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya5.pada2.DvitribhyamTayasyAyajSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.SankhyayaAvayaveTayapSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.UbhadUdattoNityamSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.ItProcessingPhase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

import dev.panini.derivation.SamjnaAssignment
import dev.panini.shiksha.Samjna

class NumeralTayapTest {

    @Test
    fun `portion guards recognize pronounced varnas independently of annotations`() {
        fun state(base: String, surface: String = base, prior: String? = null) = DerivationState(
            terms = listOfNotNull(prior?.let { DerivationTerm("prior", it, TermKind.PRATYAYA, upadesha = "fixture") },
                DerivationTerm("base", surface, TermKind.PRATIPADIKA, upadesha = base)),
            samjnas = setOf(SamjnaAssignment("base", Samjna.AVAYAVA)),
        )
        for (aya in listOf("अय", "अयँ", "अयऽ")) {
            assertFalse(DvitribhyamTayasyAyajSutra.matches(state("द्वि", prior = aya)))
            assertFalse(UbhadUdattoNityamSutra.matches(state("उभ", prior = aya)))
        }
        for (taya in listOf("तय", "तयँ", "तयऽ"))
            assertFalse(SankhyayaAvayaveTayapSutra.matches(state("पञ्चन्", "पञ्च", taya)))
        // Preserve the original exact-form guard; longer or long-vowel forms are not completed अय.
        assertTrue(DvitribhyamTayasyAyajSutra.matches(state("द्वि", prior = "अया")))
        assertTrue(SankhyayaAvayaveTayapSutra.matches(state("पञ्चन्", "पञ्च", "तया")))
        val annotatedUbha = state("fixture", "उभँऽ")
        assertTrue(UbhadUdattoNityamSutra.matches(annotatedUbha))
        val result = UbhadUdattoNityamSutra.apply(annotatedUbha).state
        assertEquals(annotatedUbha.terms.first(), result.terms.first())
        assertEquals(annotatedUbha.samjnas, result.samjnas)
        assertEquals("5.2.44", result.terms.last().createdBySutra)
        assertFalse(UbhadUdattoNityamSutra.matches(result))
        assertFalse(UbhadUdattoNityamSutra.matches(state("fixture", "उभा")))
        assertFalse(UbhadUdattoNityamSutra.matches(annotatedUbha.copy(samjnas = emptySet())))
    }

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
        assertEquals("तयप्", changePanca.state.terms.last().surface)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, changePanca.state.terms.last().itProcessingPhase)

        val stateDvi = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "द्वि", kind = TermKind.PRATIPADIKA, upadesha = "द्वि")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.AVAYAVA))
        )
        assertTrue(SankhyayaAvayaveTayapSutra.matches(stateDvi))
        val changeDvi = SankhyayaAvayaveTayapSutra.apply(stateDvi)
        assertEquals("तयप्", changeDvi.state.terms.last().surface)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, changeDvi.state.terms.last().itProcessingPhase)
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
