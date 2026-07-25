package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.ashtadhyayi.adhyaya1.pada4.RadhiksyorYasyaViprasnahSutra
import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.analysis.DhatuIdentity
import dev.panini.vyakaranam.analysis.KarakaRuleContext
import dev.panini.vyakaranam.analysis.KarakaRuleResult
import dev.panini.vyakaranam.analysis.ParticipantFacts
import dev.panini.vyakaranam.analysis.SemanticRelation
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult
import dev.panini.vyakaranam.ast.AvyayaPada
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AdditionalVibhaktiSutrasTest {

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
    fun testRadhiksyorYasyaViprasnahSutra() {
        val p = dummyParticipant(setOf(SemanticRelation.INQUIRY_DESTINY_TARGET))
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("राध्"),
            participant = p,
            allParticipants = listOf(p),
            prayoga = Prayoga.KARTARI,
        )
        assertTrue(RadhiksyorYasyaViprasnahSutra.matches(context))
        val res = RadhiksyorYasyaViprasnahSutra.apply(context) as KarakaRuleResult.Assigned
        assertEquals(Karaka.SAMPRADANA, res.karaka)
        assertEquals("1.4.39", res.evidence.sutra)
    }

    @Test
    fun testSaptamiPancamyauKarakamadhyeSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SAPTAMI, Vibhakti.PANCHAMI),
            participant = dummyParticipant(setOf(SemanticRelation.INTERVENING_DURATION_DISTANCE)),
        )
        assertTrue(SaptamiPancamyauKarakamadhyeSutra.matches(context))
        val res = SaptamiPancamyauKarakamadhyeSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SAPTAMI, res.vibhakti)
        assertEquals("2.3.7", res.evidence.sutra)
    }

    @Test
    fun testSamjnyoAnyatarasyamKarmaniSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.KARMAN,
            morphologicalCandidates = setOf(Vibhakti.TRTIYA, Vibhakti.DVITIYA),
        )
        assertTrue(SamjnyoAnyatarasyamKarmaniSutra.matches(context))
        val res = SamjnyoAnyatarasyamKarmaniSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.TRTIYA, res.vibhakti)
        assertEquals("2.3.22", res.evidence.sutra)
    }

    @Test
    fun testKaranadAkhyatayamSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.PANCHAMI, Vibhakti.TRTIYA),
            participant = dummyParticipant(setOf(SemanticRelation.GAMBLING_INSTRUMENT)),
        )
        assertTrue(KaranadAkhyatayamSutra.matches(context))
        val res = KaranadAkhyatayamSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PANCHAMI, res.vibhakti)
        assertEquals("2.3.33", res.evidence.sutra)
    }

    @Test
    fun testStokanAlpaKrcchraSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.PANCHAMI, Vibhakti.TRTIYA),
            participant = dummyParticipant(setOf(SemanticRelation.INDETERMINATE_QUANTITY)),
        )
        assertTrue(StokanAlpaKrcchraSutra.matches(context))
        val res = StokanAlpaKrcchraSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PANCHAMI, res.vibhakti)
        assertEquals("2.3.34", res.evidence.sutra)
    }

    @Test
    fun testNaksatreCaLupiSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.TRTIYA, Vibhakti.SAPTAMI),
            participant = dummyParticipant(setOf(SemanticRelation.ASTROLOGICAL_TIME)),
        )
        assertTrue(NaksatreCaLupiSutra.matches(context))
        val res = NaksatreCaLupiSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.TRTIYA, res.vibhakti)
        assertEquals("2.3.45", res.evidence.sutra)
    }

    @Test
    fun testRujarthanamBhavavacananamAjvarehSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.DISEASE_PAIN_OBJECT)),
        )
        assertTrue(RujarthanamBhavavacananamAjvarehSutra.matches(context))
        val res = RujarthanamBhavavacananamAjvarehSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.54", res.evidence.sutra)
    }

    @Test
    fun testAsisiNathahSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.BLESSING_HOPE_OBJECT)),
        )
        assertTrue(AsisiNathahSutra.matches(context))
        val res = AsisiNathahSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.55", res.evidence.sutra)
    }

    @Test
    fun testJasiniPrahanaNatakaKrathaPisarnHimvisayamSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.INJURY_VIOLENCE_OBJECT)),
        )
        assertTrue(JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra.matches(context))
        val res = JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.56", res.evidence.sutra)
    }

    @Test
    fun testAdhikaranavacinacCaSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.LOCATION_PARTICIPLE_RELATION)),
        )
        assertTrue(AdhikaranavacinacCaSutra.matches(context))
        val res = AdhikaranavacinacCaSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.68", res.evidence.sutra)
    }
}
