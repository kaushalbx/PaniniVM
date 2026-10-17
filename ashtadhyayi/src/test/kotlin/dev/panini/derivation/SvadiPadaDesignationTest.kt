package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada4.SvadisvAsarvanamasthaneSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.YaciBhamSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.SasajusoRuhSutra
import dev.panini.core.*
import dev.panini.shiksha.Samjna
import kotlin.test.*

class SvadiPadaDesignationTest {
    private fun state(affix: DerivationTerm, linga: Linga = Linga.PUMS) = DerivationState(
        listOf(DerivationTerm("stem", "मनस्", TermKind.PRATIPADIKA), affix),
        stage = DerivationStage.IT_PROCESSED, context = DerivationalContext(rupa = Rupa(linga = linga)))

    @Test fun `non sarvanamasthana consonantal sup and taddhita establish pada`() {
        for (affix in listOf(SupAffix.BHIS.term("affix").copy(surface = "भिस्"),
            TaddhitaAffix.TVA.term("affix"), TaddhitaAffix.TAL.term("affix").copy(surface = "ता"))) {
            val before = state(affix)
            assertTrue(SvadisvAsarvanamasthaneSutra.matches(before))
            assertFalse(YaciBhamSutra.matches(before))
            val after = SvadisvAsarvanamasthaneSutra.apply(before).state
            assertTrue(SamjnaAssignment("stem", Samjna.PADA) in after.samjnas)
            assertEquals(before.terms, after.terms)
            assertFalse(SvadisvAsarvanamasthaneSutra.matches(after))
            assertTrue(SasajusoRuhSutra.matches(after))
            val rutva = SasajusoRuhSutra.apply(after).state
            assertEquals(after.terms.last(), rutva.terms.last())
            assertEquals(ItProcessingPhase.RAW_UPADESHA, rutva.terms.first().itProcessingPhase)
        }
    }

    @Test fun `bha replaces preceding pada and does not remove suffix pada`() {
        val suffix = SupAffix.TA.term("affix").copy(surface = "आ")
        val before = state(suffix).withSamjnas(setOf(SamjnaAssignment("stem", Samjna.PADA), SamjnaAssignment("affix", Samjna.PADA)))
        assertTrue(YaciBhamSutra.matches(before))
        val after = YaciBhamSutra.apply(before).state
        assertTrue(SamjnaAssignment("stem", Samjna.BHA) in after.samjnas)
        assertFalse(SamjnaAssignment("stem", Samjna.PADA) in after.samjnas)
        assertTrue(SamjnaAssignment("affix", Samjna.PADA) in after.samjnas)
        assertFalse(SvadisvAsarvanamasthaneSutra.matches(after))
    }

    @Test fun `sarvanamasthana and verbal affixes are outside both designations`() {
        for (affix in listOf(SupAffix.AU.term("affix"), SupAffix.JAS.term("affix").copy(surface = "अस्"),
            TingAffix.TIP.term().copy(surface = "ति"),
            DerivationTerm("affix", "अ", TermKind.PRATYAYA, upadesha = "अण्", createdBySutra = "3.2.1"))) {
            assertFalse(SvadisvAsarvanamasthaneSutra.matches(state(affix)))
            assertFalse(YaciBhamSutra.matches(state(affix)))
        }
        val si = SupAffix.JAS.term("affix").copy(surface = "इ", upadesha = "शि")
        assertFalse(SvadisvAsarvanamasthaneSutra.matches(state(si, Linga.NAPUMSAKA)))
        assertFalse(YaciBhamSutra.matches(state(si, Linga.NAPUMSAKA)))
    }

    @Test fun `typed taddhita source survives substitution and processing must finish`() {
        val affix = TaddhitaAffix.TVA.term("affix").copy(surface = "त्व", upadesha = "changed")
        assertTrue(affix.isSvadiAffix())
        assertEquals(TaddhitaAffix.TVA, affix.sourceTaddhitaAffix)
        assertFalse(SvadisvAsarvanamasthaneSutra.matches(state(affix.copy(itProcessingPhase = ItProcessingPhase.RAW_UPADESHA))))
    }

    @Test fun `consonantal s stem endings use svadi designation and authentic rutva`() {
        for ((vibhakti, vacana, expected) in listOf(
            Triple(Vibhakti.TRTIYA, Vacana.DVIVACANA, "मनोभ्याम्"),
            Triple(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "मनोभिः"),
            Triple(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "मनःसु"),
        )) {
            val result = SubantaEngine().derive(SubantaDerivationRequest("मनस्", vibhakti, vacana, Linga.NAPUMSAKA))
            assertEquals(expected, result.final.surface)
            val trace = result.applications.map { it.sutra }
            assertTrue(trace.indexOf("1.4.17") >= 0)
            assertTrue(trace.indexOf("8.2.66") > trace.indexOf("1.4.17"))
            assertFalse("1.4.18" in trace)
            if (vibhakti == Vibhakti.TRTIYA) assertTrue("6.1.114" in trace)
            else assertTrue("8.3.15" in trace)
        }
    }
}
