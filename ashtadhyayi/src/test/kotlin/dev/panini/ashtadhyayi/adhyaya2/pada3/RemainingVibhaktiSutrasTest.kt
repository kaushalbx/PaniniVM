package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.analysis.NishedhaRuleEngine
import dev.panini.analysis.NishedhaRuleResult
import dev.panini.analysis.ParticipantFacts
import dev.panini.analysis.ProhibitionContext
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.vyakaranam.ast.AvyayaPada
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RemainingVibhaktiSutrasTest {

    private fun dummyParticipant(
        relations: Set<SemanticRelation> = emptySet(),
        possibleVibhaktis: Set<Vibhakti> = Vibhakti.values().toSet(),
    ) = ParticipantFacts(
        id = "test-participant",
        expression = AvyayaPada(sourceText = "test", form = "test"),
        possibleVibhaktis = possibleVibhaktis,
        semanticRelations = relations,
    )

    @Test
    fun testAntarantarenaYukteSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.DVITIYA),
            participant = dummyParticipant(setOf(SemanticRelation.BETWEEN_OR_WITHOUT)),
        )
        assertTrue(AntarantarenaYukteSutra.matches(context))
        val res = AntarantarenaYukteSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.DVITIYA, res.vibhakti)
        assertEquals("2.3.4", res.evidence.sutra)
    }

    @Test
    fun testKarmapravacaniyayukteDvitiyaSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.DVITIYA),
            participant = dummyParticipant(setOf(SemanticRelation.KARMAPRAVACANIYA_GOVERNANCE)),
        )
        assertTrue(KarmapravacaniyayukteDvitiyaSutra.matches(context))
        val res = KarmapravacaniyayukteDvitiyaSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.DVITIYA, res.vibhakti)
        assertEquals("2.3.8", res.evidence.sutra)
    }

    @Test
    fun testEnapaDvitiyaSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.DVITIYA),
            participant = dummyParticipant(setOf(SemanticRelation.ENAPA_SUFFIX)),
        )
        assertTrue(EnapaDvitiyaSutra.matches(context))
        val res = EnapaDvitiyaSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.DVITIYA, res.vibhakti)
        assertEquals("2.3.9", res.evidence.sutra)
    }

    @Test
    fun testPancamyApangParibhihSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.PANCHAMI),
            participant = dummyParticipant(setOf(SemanticRelation.EXCLUSION_LIMIT)),
        )
        assertTrue(PancamyApangParibhihSutra.matches(context))
        val res = PancamyApangParibhihSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PANCHAMI, res.vibhakti)
        assertEquals("2.3.10", res.evidence.sutra)
    }

    @Test
    fun testPratinidhiPratidaneCaYasmatSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.PANCHAMI),
            participant = dummyParticipant(setOf(SemanticRelation.REPRESENTATIVE_EXCHANGE)),
        )
        assertTrue(PratinidhiPratidaneCaYasmatSutra.matches(context))
        val res = PratinidhiPratidaneCaYasmatSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PANCHAMI, res.vibhakti)
        assertEquals("2.3.11", res.evidence.sutra)
    }

    @Test
    fun testKriyarthopapadasyaCaKarmaniSthaninahSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.CHATURTHI),
            participant = dummyParticipant(setOf(SemanticRelation.IMPLIED_PURPOSE_OBJECT)),
        )
        assertTrue(KriyarthopapadasyaCaKarmaniSthaninahSutra.matches(context))
        val res = KriyarthopapadasyaCaKarmaniSthaninahSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.CHATURTHI, res.vibhakti)
        assertEquals("2.3.14", res.evidence.sutra)
    }

    @Test
    fun testTumarthacCaBhavavacanatSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.CHATURTHI),
            participant = dummyParticipant(setOf(SemanticRelation.PURPOSE_ACTION)),
        )
        assertTrue(TumarthacCaBhavavacanatSutra.matches(context))
        val res = TumarthacCaBhavavacanatSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.CHATURTHI, res.vibhakti)
        assertEquals("2.3.15", res.evidence.sutra)
    }

    @Test
    fun testVibhasaGuneAstriyamSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.PANCHAMI, Vibhakti.TRTIYA),
            participant = dummyParticipant(setOf(SemanticRelation.NON_FEMININE_QUALITY_CAUSE)),
        )
        assertTrue(VibhasaGuneAstriyamSutra.matches(context))
        val res = VibhasaGuneAstriyamSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PANCHAMI, res.vibhakti)
        assertEquals("2.3.25", res.evidence.sutra)
    }

    @Test
    fun testSasthiHetuprayogeSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.EXPLICIT_HETU_USE)),
        )
        assertTrue(SasthiHetuprayogeSutra.matches(context))
        val res = SasthiHetuprayogeSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.26", res.evidence.sutra)
    }

    @Test
    fun testSarvanamnasTrtiyaCaSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.PRONOMINAL_HETU)),
        )
        assertTrue(SarvanamnasTrtiyaCaSutra.matches(context))
        val res = SarvanamnasTrtiyaCaSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.TRTIYA, res.vibhakti)
        assertEquals("2.3.27", res.evidence.sutra)
    }

    @Test
    fun testSasthyAtasarthaPratyayenaSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.SPATIAL_DIRECTION)),
        )
        assertTrue(SasthyAtasarthaPratyayenaSutra.matches(context))
        val res = SasthyAtasarthaPratyayenaSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.30", res.evidence.sutra)
    }

    @Test
    fun testDurantikarthebhyoDvitiyaCaSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.PANCHAMI, Vibhakti.TRTIYA),
            participant = dummyParticipant(setOf(SemanticRelation.DISTANCE_OR_PROXIMITY)),
        )
        assertTrue(DurantikarthebhyoDvitiyaCaSutra.matches(context))
        val res = DurantikarthebhyoDvitiyaCaSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PANCHAMI, res.vibhakti)
        assertEquals("2.3.35", res.evidence.sutra)
    }

    @Test
    fun testAyuktaKusalabhyamCaSevayamSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI, Vibhakti.SAPTAMI),
            participant = dummyParticipant(setOf(SemanticRelation.ENGROSSED_ATTACHMENT)),
        )
        assertTrue(AyuktaKusalabhyamCaSevayamSutra.matches(context))
        val res = AyuktaKusalabhyamCaSevayamSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.40", res.evidence.sutra)
    }

    @Test
    fun testPancamiVibhakteSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.PANCHAMI),
            participant = dummyParticipant(setOf(SemanticRelation.COMPARATIVE_DISTINCTION)),
        )
        assertTrue(PancamiVibhakteSutra.matches(context))
        val res = PancamiVibhakteSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PANCHAMI, res.vibhakti)
        assertEquals("2.3.42", res.evidence.sutra)
    }

    @Test
    fun testPratipadikarthalingaparimanavacanamatrePrathamaSutra() {
        val contextAbhihita = VibhaktiRuleContext(
            karaka = Karaka.KARTR,
            morphologicalCandidates = setOf(Vibhakti.PRATHAMA),
            abhihita = true,
        )
        assertTrue(PratipadikarthalingaparimanavacanamatrePrathamaSutra.matches(contextAbhihita))
        val res = PratipadikarthalingaparimanavacanamatrePrathamaSutra.apply(contextAbhihita) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PRATHAMA, res.vibhakti)
        assertEquals("2.3.46", res.evidence.sutra)
    }

    @Test
    fun testSambodhaneCaSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.SAMBODHANA,
            morphologicalCandidates = setOf(Vibhakti.PRATHAMA),
        )
        assertTrue(SambodhaneCaSutra.matches(context))
        val res = SambodhaneCaSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PRATHAMA, res.vibhakti)
        assertEquals("2.3.47", res.evidence.sutra)
    }

    @Test
    fun testJnyoAvidarthasyaKaraneSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.KARANA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.MEMORY_OR_RULING_OBJECT)),
        )
        assertTrue(JnyoAvidarthasyaKaraneSutra.matches(context))
        val res = JnyoAvidarthasyaKaraneSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.51", res.evidence.sutra)
    }

    @Test
    fun testNaLokavyayanisthanisthakhalarthatrnnamSutra() {
        val prohibition = NishedhaRuleEngine.evaluateProhibition(
            ProhibitionContext(targetSutraNumber = "2.3.65", isKrtProhibitedForSasthi = true)
        )
        assertTrue(prohibition is NishedhaRuleResult.Blocked)
        val blocked = prohibition as NishedhaRuleResult.Blocked
        assertEquals("2.3.66", blocked.blockerSutraNumber)
        assertEquals("2.3.65", blocked.blockedTargetSutraNumber)
    }

    @Test
    fun testKrtyanamKartariVaSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.KARTR,
            morphologicalCandidates = setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI),
        )
        assertTrue(KrtyanamKartariVaSutra.matches(context))
        val res = KrtyanamKartariVaSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.TRTIYA, res.vibhakti)
        assertEquals("2.3.71", res.evidence.sutra)
    }

    @Test
    fun testTulyarthairAtulopamabhyamTrtiyanatarasyamSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.EQUAL_COMPARISON)),
        )
        assertTrue(TulyarthairAtulopamabhyamTrtiyanatarasyamSutra.matches(context))
        val res = TulyarthairAtulopamabhyamTrtiyanatarasyamSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.TRTIYA, res.vibhakti)
        assertEquals("2.3.72", res.evidence.sutra)
    }

    @Test
    fun testCaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.CHATURTHI, Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.BENEDICTION_WELLBEING)),
        )
        assertTrue(CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra.matches(context))
        val res = CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.CHATURTHI, res.vibhakti)
        assertEquals("2.3.73", res.evidence.sutra)
    }
}
