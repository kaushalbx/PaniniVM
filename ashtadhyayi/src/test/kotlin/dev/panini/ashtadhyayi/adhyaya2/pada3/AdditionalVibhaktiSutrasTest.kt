package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.ashtadhyayi.adhyaya1.pada4.RadhiksyorYasyaViprasnahSutra
import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.analysis.DhatuIdentity
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.KarakaRuleResult
import dev.panini.analysis.ParticipantFacts
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.vyakaranam.ast.AvyayaPada
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class AdditionalVibhaktiSutrasTest {
    @Test
    fun `manya dative requires the specific disregard construction`() {
        val ordinary = VibhaktiRuleContext(Karaka.KARMAN, setOf(Vibhakti.CHATURTHI),
            participant = dummyParticipant())
        org.junit.jupiter.api.Assertions.assertFalse(ManyaKarmaniAnadareSutra.matches(ordinary))
        val marked = ordinary.copy(dhatu = dev.panini.analysis.DhatuIdentity("मन्"), participant = dummyParticipant(
            setOf(SemanticRelation.MAN_INANIMATE_DISREGARD_OBJECT)))
        assertFalse(ManyaKarmaniAnadareSutra.matches(marked.copy(dhatu = dev.panini.analysis.DhatuIdentity("चिन्त्"))))
        assertTrue(ManyaKarmaniAnadareSutra.matches(marked))
        assertEquals("2.3.17", (ManyaKarmaniAnadareSutra.apply(marked) as VibhaktiRuleResult.Assigned).evidence.sutra)
    }


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
            candidates = setOf(Karaka.SAMPRADANA),
        )
        assertTrue(RadhiksyorYasyaViprasnahSutra.matches(context))
        assertTrue(RadhiksyorYasyaViprasnahSutra.matches(context.copy(candidates = emptySet())))
        assertTrue(RadhiksyorYasyaViprasnahSutra.matches(context.copy(candidates = setOf(Karaka.KARMAN))))
        val unestablished = p.copy(semanticRelations = emptySet())
        assertFalse(RadhiksyorYasyaViprasnahSutra.matches(context.copy(participant = unestablished,
            allParticipants = listOf(unestablished))))
        val res = RadhiksyorYasyaViprasnahSutra.apply(context) as KarakaRuleResult.Assigned
        assertEquals(Karaka.SAMPRADANA, res.karaka)
        assertEquals("1.4.39", res.evidence.sutra)
    }

    @Test
    fun testSaptamiPancamyauKarakamadhyeSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SAPTAMI, Vibhakti.PANCHAMI),
            participant = dummyParticipant(setOf(SemanticRelation.INTERVENING_DURATION_DISTANCE,
                SemanticRelation.KARAKA_POWER_INTERVAL)),
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
            participant = dummyParticipant(setOf(SemanticRelation.SAM_JNA_OBJECT)),
        )
        org.junit.jupiter.api.Assertions.assertFalse(SamjnyoAnyatarasyamKarmaniSutra.matches(
            context.copy(participant = dummyParticipant())))
        assertTrue(SamjnyoAnyatarasyamKarmaniSutra.matches(context))
        val res = SamjnyoAnyatarasyamKarmaniSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.TRTIYA, res.vibhakti)
        assertEquals("2.3.22", res.evidence.sutra)
    }

    @Test
    fun testStokanAlpaKrcchraSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.KARANA,
            morphologicalCandidates = setOf(Vibhakti.PANCHAMI, Vibhakti.TRTIYA),
            participant = dummyParticipant(setOf(SemanticRelation.NON_SUBSTANCE_MEANING)).copy(
                expression = dev.panini.vyakaranam.ast.SubantaPada("स्तोक", dev.panini.vyakaranam.ast.MulaPratipadika("स्तोक", "स्तोक"),
                    dev.panini.vyakaranam.ast.SupPratyaya("ङसिँ", "ङसिँ"))),
        )
        assertTrue(StokanAlpaKrcchraSutra.matches(context))
        val res = StokanAlpaKrcchraSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PANCHAMI, res.vibhakti)
        assertEquals("2.3.33", res.evidence.sutra)
    }

    @Test
    fun testNaksatreCaLupiSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ADHIKARANA,
            morphologicalCandidates = setOf(Vibhakti.TRTIYA, Vibhakti.SAPTAMI),
            participant = dummyParticipant(setOf(SemanticRelation.ASTROLOGICAL_TIME,
                SemanticRelation.NAKSATRA_EXPRESSION, SemanticRelation.LOCATION)).copy(
                taddhitaLopa = dev.panini.analysis.TaddhitaLopaEvidence(dev.panini.core.LopaType.LUP, "4.2.4")),
        )
        assertTrue(NaksatreCaLupiSutra.matches(context))
        assertFalse(NaksatreCaLupiSutra.matches(context.copy(abhihita = true)))
        assertFalse(NaksatreCaLupiSutra.matches(context.copy(karaka = Karaka.ANIRDHARITA)))
        val res = NaksatreCaLupiSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.TRTIYA, res.vibhakti)
        assertEquals("2.3.45", res.evidence.sutra)
    }

    @Test
    fun testRujarthanamBhavavacananamAjvarehSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.DISEASE_PAIN_OBJECT,
                SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)),
            dhatu = DhatuIdentity("रुज्"),
            allParticipants = listOf(dummyParticipant(setOf(SemanticRelation.INDEPENDENT_AGENT,
                SemanticRelation.BHAVA_EVENT)).copy(id = "abstract-agent")),
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
            participant = dummyParticipant(setOf(SemanticRelation.BLESSING_HOPE_OBJECT,
                SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)),
            dhatu = DhatuIdentity("नाथ्"),
        )
        assertTrue(AsisiNathahSutra.matches(context))
        assertFalse(AsisiNathahSutra.matches(context.copy(abhihita = true)))
        assertFalse(AsisiNathahSutra.matches(context.copy(karaka = Karaka.KARANA)))
        assertFalse(AsisiNathahSutra.matches(context.copy(dhatu = null)))
        val res = AsisiNathahSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.55", res.evidence.sutra)
    }

    @Test
    fun testJasiniPrahanaNatakaKrathaPisarnHimvisayamSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.INJURY_VIOLENCE_OBJECT,
                SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)),
            dhatu = DhatuIdentity("पिष्"),
        )
        assertTrue(JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra.matches(context))
        assertFalse(JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra.matches(context.copy(abhihita = true)))
        assertFalse(JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra.matches(context.copy(karaka = Karaka.KARANA)))
        assertFalse(JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra.matches(context.copy(dhatu = null)))
        val res = JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.56", res.evidence.sutra)
    }

    @Test
    fun `location participle marker alone does not establish 2 3 68`() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.LOCATION_PARTICIPLE_RELATION)),
        )
        assertFalse(AdhikaranavacinacCaSutra.matches(context))
    }
}
