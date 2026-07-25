package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.EtiStuShaasVriDrJuShyahKyapSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.SatsudvisatrusadvisavahaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.AkartariChaKarakeSamyayamSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.KrmyoKahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.ArdhadhatukamSheshahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.LaKarmaniChaBhaveChakartariChaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.LinAshisiSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.LitCaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.TayorevaKtyaktakhalarthahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.TinsitSarvadhatukamSutra
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Batch10SarvadhatukamAndKrtSutrasTest {

    @Test
    fun `test 3 1 109 EtiStuShaasVriDrJuShyahKyapSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "स्तु", TermKind.DHATU, upadesha = "स्तु")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(EtiStuShaasVriDrJuShyahKyapSutra.matches(state))
        assertEquals("क्यप्", EtiStuShaasVriDrJuShyahKyapSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 2 61 SatsudvisatrusadvisavahaSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "सद्", TermKind.DHATU, upadesha = "सद्")))
        assertTrue(SatsudvisatrusadvisavahaSutra.matches(state))
        assertEquals("क्विप्", SatsudvisatrusadvisavahaSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 3 14 KrmyoKahSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")))
        assertTrue(KrmyoKahSutra.matches(state))
        assertEquals("क", KrmyoKahSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 3 102 AkartariChaKarakeSamyayamSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "हृ", TermKind.DHATU, upadesha = "हृ")))
        assertTrue(AkartariChaKarakeSamyayamSutra.matches(state))
        assertEquals("घञ्", AkartariChaKarakeSamyayamSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 4 69 LaKarmaniChaBhaveChakartariChaSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LAT))
        )
        assertTrue(LaKarmaniChaBhaveChakartariChaSutra.matches(state))
    }

    @Test
    fun `test 3 4 70 TayorevaKtyaktakhalarthahSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")))
        assertTrue(TayorevaKtyaktakhalarthahSutra.matches(state))
    }

    @Test
    fun `test 3 4 113 TinsitSarvadhatukamSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("tip", "ति", TermKind.PRATYAYA, upadesha = "तिप्")
            )
        )
        assertTrue(TinsitSarvadhatukamSutra.matches(state))
        val updatedState = TinsitSarvadhatukamSutra.apply(state).state
        assertTrue(updatedState.samjnas.any { it.targetId == "tip" && it.samjna == Samjna.SARVADHATUKA })
    }

    @Test
    fun `test 3 4 114 ArdhadhatukamSheshahSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ"),
                DerivationTerm("ghanj", "अ", TermKind.PRATYAYA, upadesha = "घञ्")
            )
        )
        assertTrue(ArdhadhatukamSheshahSutra.matches(state))
        val updatedState = ArdhadhatukamSheshahSutra.apply(state).state
        assertTrue(updatedState.samjnas.any { it.targetId == "ghanj" && it.samjna == Samjna.ARDHADHATUKA })
    }

    @Test
    fun `test 3 4 115 LitCaSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("nal", "अ", TermKind.PRATYAYA, upadesha = "णल्")
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT))
        )
        assertTrue(LitCaSutra.matches(state))
        val updatedState = LitCaSutra.apply(state).state
        assertTrue(updatedState.samjnas.any { it.targetId == "nal" && it.samjna == Samjna.ARDHADHATUKA })
    }

    @Test
    fun `test 3 4 116 LinAshisiSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("yasu", "यास्", TermKind.PRATYAYA, upadesha = "यासुट्")
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LING))
        )
        assertTrue(LinAshisiSutra.matches(state))
        val updatedState = LinAshisiSutra.apply(state).state
        assertTrue(updatedState.samjnas.any { it.targetId == "yasu" && it.samjna == Samjna.ARDHADHATUKA })
    }
}
