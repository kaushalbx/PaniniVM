package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.EtiStuShaasVriDrJuShyahKyapSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.SatsudvisatrusadvisavahaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.AkartariChaKarakeSamyayamSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.LrtahSadVaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.LatahSatrsanacauSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.ArdhadhatukamSheshahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.LaKarmaniChaBhaveChakartariChaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.LinAshisiSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.LitCaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.TayorevaKtyaktakhalarthahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.TinsitSarvadhatukamSutra
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Batch10SarvadhatukamAndKrtSutrasTest {

    @Test
    fun `derives kyap affix via EtiStuShaasVriDrJuShyahKyapSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "स्तु", TermKind.DHATU, upadesha = "स्तु")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val result = DerivationEngine(listOf(EtiStuShaasVriDrJuShyahKyapSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.109" })
        assertEquals("क्यप्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives kvip affix via SatsudvisatrusadvisavahaSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "सद्", TermKind.DHATU, upadesha = "सद्")))
        val result = DerivationEngine(listOf(SatsudvisatrusadvisavahaSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.2.61" })
        assertEquals("क्विप्", result.final.allEffectiveTerms.last().upadesha)
    }

    private fun participleState(lakara: dev.panini.core.Lakara, samjna: dev.panini.shiksha.Samjna): DerivationState {
        val root = DerivationTerm("root", "डुकृञ्", TermKind.DHATU, upadesha = "डुकृञ्")
        return DerivationState(listOf(root), samjnas = setOf(dev.panini.derivation.SamjnaAssignment(root.id, samjna)),
            context = dev.panini.derivation.DerivationalContext(rupa = dev.panini.derivation.Rupa(lakara = lakara)))
    }

    @Test
    fun `3_3_14 puts a sat affix in place of lrt`() {
        for ((samjna, affix) in listOf(dev.panini.shiksha.Samjna.SATR to "शतृँ", dev.panini.shiksha.Samjna.SANAC to "शानच्")) {
            val result = DerivationEngine(listOf(LrtahSadVaSutra)).derive(participleState(dev.panini.core.Lakara.LRT, samjna))
            assertTrue(result.applications.any { it.sutra == "3.3.14" })
            assertEquals(affix, result.final.allEffectiveTerms.last().upadesha)
        }
    }

    @Test
    fun `3_3_14 and 3_2_124 keep to their own lakaras`() {
        val lat = participleState(dev.panini.core.Lakara.LAT, dev.panini.shiksha.Samjna.SATR)
        val lrt = participleState(dev.panini.core.Lakara.LRT, dev.panini.shiksha.Samjna.SATR)
        assertFalse(LrtahSadVaSutra.matches(lat))
        assertFalse(LatahSatrsanacauSutra.matches(lrt))
        assertTrue(LatahSatrsanacauSutra.matches(lat))
        val bare = DerivationState(listOf(DerivationTerm("root", "डुकृञ्", TermKind.DHATU, upadesha = "डुकृञ्")),
            context = dev.panini.derivation.DerivationalContext(rupa = dev.panini.derivation.Rupa(lakara = dev.panini.core.Lakara.LRT)))
        assertFalse(LrtahSadVaSutra.matches(bare), "No participle asked for: the finite लृट् stays.")
    }

    @Test
    fun `derives ghañ affix via AkartariChaKarakeSamyayamSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "हृ", TermKind.DHATU, upadesha = "हृ")))
        val result = DerivationEngine(listOf(AkartariChaKarakeSamyayamSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.3.102" })
        assertEquals("घञ्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `activates Lakāra semantics via LaKarmaniChaBhaveChakartariChaSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LAT))
        )
        val result = DerivationEngine(listOf(LaKarmaniChaBhaveChakartariChaSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.4.69" })
    }

    @Test
    fun `restricts kṛtya and kta via TayorevaKtyaktakhalarthahSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")))
        val result = DerivationEngine(listOf(TayorevaKtyaktakhalarthahSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.4.70" })
    }

    @Test
    fun `assigns sārvadhātuka saṃjñā via TinsitSarvadhatukamSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("tip", "ति", TermKind.PRATYAYA, upadesha = "तिप्")
            )
        )
        val result = DerivationEngine(listOf(TinsitSarvadhatukamSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.4.113" })
        assertTrue(result.final.samjnas.any { it.targetId == "tip" && it.samjna == Samjna.SARVADHATUKA })
    }

    @Test
    fun `assigns ārdhadhātuka saṃjñā via ArdhadhatukamSheshahSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ"),
                DerivationTerm("ghanj", "अ", TermKind.PRATYAYA, upadesha = "घञ्")
            )
        )
        val result = DerivationEngine(listOf(ArdhadhatukamSheshahSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.4.114" })
        assertTrue(result.final.samjnas.any { it.targetId == "ghanj" && it.samjna == Samjna.ARDHADHATUKA })
    }

    @Test
    fun `assigns ārdhadhātuka saṃjñā in Liṭ via LitCaSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("nal", "अ", TermKind.PRATYAYA, upadesha = "णल्")
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT))
        )
        val result = DerivationEngine(listOf(LitCaSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.4.115" })
        assertTrue(result.final.samjnas.any { it.targetId == "nal" && it.samjna == Samjna.ARDHADHATUKA })
    }

    @Test
    fun `assigns ārdhadhātuka saṃjñā in Āśīrliṅ via LinAshisiSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("yasu", "यास्", TermKind.PRATYAYA, upadesha = "यासुट्")
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LING))
        )
        val result = DerivationEngine(listOf(LinAshisiSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.4.116" })
        assertTrue(result.final.samjnas.any { it.targetId == "yasu" && it.samjna == Samjna.ARDHADHATUKA })
    }
}
