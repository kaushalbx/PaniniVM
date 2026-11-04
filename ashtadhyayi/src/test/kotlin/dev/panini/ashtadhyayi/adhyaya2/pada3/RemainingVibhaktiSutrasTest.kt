package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.analysis.NishedhaRuleEngine
import dev.panini.analysis.NishedhaRuleResult
import dev.panini.analysis.ParticipantFacts
import dev.panini.analysis.ProhibitionContext
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.DhatuIdentity
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.vyakaranam.ast.AvyayaPada
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RemainingVibhaktiSutrasTest {
    @Test fun `brahmana div requires inherited root sense and object premises`() {
        val relations = setOf(SemanticRelation.BRAHMANA_DIV_OBJECT, SemanticRelation.TRANSACTION_GAMBLING_OBJECT,
            SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)
        val context = VibhaktiRuleContext(Karaka.ANIRDHARITA, setOf(Vibhakti.DVITIYA),
            participant = dummyParticipant(relations), dhatu = DhatuIdentity("दिव्"))
        assertTrue(DvitiyaBrahmaneSutra.matches(context))
        for (missing in relations) {
            assertFalse(DvitiyaBrahmaneSutra.matches(context.copy(participant = dummyParticipant(relations - missing))), missing.name)
        }
        assertFalse(DvitiyaBrahmaneSutra.matches(context.copy(dhatu = DhatuIdentity("कृ"))))
        assertFalse(DvitiyaBrahmaneSutra.matches(context.copy(abhihita = true)))
        assertFalse(DvitiyaBrahmaneSutra.matches(context.copy(karaka = Karaka.KARANA)))
    }

    @Test
    fun `2 3 66 excludes competing agent and supersedes general krt genitive for object`() {
        val agent = VibhaktiRuleContext(
            karaka = Karaka.KARTR,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(
                SemanticRelation.KRT_GOVERNED_PARTICIPANT,
                SemanticRelation.KRT_DUAL_ELIGIBLE_AGENT,
            )),
        )
        val target = agent.copy(
            karaka = Karaka.KARMAN,
            participant = dummyParticipant(setOf(
                SemanticRelation.KRT_GOVERNED_PARTICIPANT,
                SemanticRelation.KRT_DUAL_ELIGIBLE_OBJECT,
            )),
        )
        assertFalse(KartrKarmanohKrtiSutra.matches(agent))
        assertFalse(UbhayapraptauKarmaniSutra.matches(agent))
        assertFalse(KartrKarmanohKrtiSutra.matches(target))
        assertTrue(UbhayapraptauKarmaniSutra.matches(target))
        assertEquals("2.3.66", (UbhayapraptauKarmaniSutra.apply(target) as VibhaktiRuleResult.Assigned).evidence.sutra)
    }

    @Test
    fun `2 3 65 requires krt governance for both agent and object`() {
        listOf(Karaka.KARTR, Karaka.KARMAN).forEach { karaka ->
            val finiteClause = VibhaktiRuleContext(
                karaka = karaka,
                morphologicalCandidates = setOf(Vibhakti.SASTHI),
                participant = dummyParticipant(),
            )
            assertFalse(KartrKarmanohKrtiSutra.matches(finiteClause))
            val governed = finiteClause.copy(participant = dummyParticipant(
                setOf(SemanticRelation.KRT_GOVERNED_PARTICIPANT),
            ))
            assertTrue(KartrKarmanohKrtiSutra.matches(governed))
            assertFalse(KartrKarmanohKrtiSutra.matches(governed.copy(abhihita = true)))
            assertFalse(KartrKarmanohKrtiSutra.matches(governed.copy(karaka = Karaka.KARANA)))
            assertEquals("2.3.65", (KartrKarmanohKrtiSutra.apply(governed) as VibhaktiRuleResult.Assigned).evidence.sutra)
        }
    }

    @Test
    fun `2 3 3 is restricted to explicitly Vedic hu objects`() {
        val positive = VibhaktiRuleContext(
            karaka = Karaka.KARMAN,
            morphologicalCandidates = setOf(Vibhakti.TRTIYA),
            participant = dummyParticipant(setOf(SemanticRelation.VEDIC_HU_OBJECT)),
        )
        val negative = positive.copy(participant = dummyParticipant())
        assertTrue(TrtiyaCaHosChandasiSutra.matches(positive))
        assertFalse(TrtiyaCaHosChandasiSutra.matches(negative))
        val result = TrtiyaCaHosChandasiSutra.apply(positive) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.TRTIYA, result.vibhakti)
        assertEquals("2.3.3", result.evidence.sutra)
        assertTrue(TrtiyaCaHosChandasiSutra.optional)
    }

    @Test
    fun `2 3 6 requires completion limit semantics`() {
        val positive = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.TRTIYA),
            participant = dummyParticipant(setOf(SemanticRelation.COMPLETION_LIMIT,
                SemanticRelation.CONTINUOUS_TIME_DISTANCE_EXTENT)),
        )
        assertTrue(ApavargeTrtiyaSutra.matches(positive))
        assertFalse(ApavargeTrtiyaSutra.matches(positive.copy(participant = dummyParticipant())))
        assertEquals("2.3.6", (ApavargeTrtiyaSutra.apply(positive) as VibhaktiRuleResult.Assigned).evidence.sutra)
    }

    @Test
    fun `2 3 9 requires superiority reference semantics`() {
        val positive = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SAPTAMI),
            participant = dummyParticipant(setOf(SemanticRelation.SUPERIORITY_REFERENCE,
                SemanticRelation.KARMAPRAVACANIYA_GOVERNANCE, SemanticRelation.UPA_SUPERIORITY_KARMAPRAVACANIYA_LINK)),
        )
        assertTrue(YasmadAdhikamYasyaCesvaravacanamTatraSaptamiSutra.matches(positive))
        assertFalse(YasmadAdhikamYasyaCesvaravacanamTatraSaptamiSutra.matches(positive.copy(participant = dummyParticipant())))
        val result = YasmadAdhikamYasyaCesvaravacanamTatraSaptamiSutra.apply(positive) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SAPTAMI, result.vibhakti)
        assertEquals("2.3.9", result.evidence.sutra)
    }

    @Test
    fun `2 3 24 requires a non agent debt reference`() {
        val positive = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.PANCHAMI),
            participant = dummyParticipant(setOf(SemanticRelation.NON_AGENT_DEBT_REFERENCE)),
        )
        assertTrue(AkartaryRnePancamiSutra.matches(positive))
        assertFalse(AkartaryRnePancamiSutra.matches(positive.copy(participant = dummyParticipant())))
        assertFalse(AkartaryRnePancamiSutra.matches(positive.copy(karaka = Karaka.KARTR)))
        assertFalse(AkartaryRnePancamiSutra.matches(positive.copy(abhihita = true)))
        val instrumental = positive.copy(morphologicalCandidates = setOf(Vibhakti.TRTIYA),
            participant = dummyParticipant(setOf(SemanticRelation.NON_AGENT_DEBT_REFERENCE, SemanticRelation.CAUSE_HETU)))
        assertFalse(AkartaryRnePancamiSutra.matches(instrumental))
        assertFalse(HetauSutra.matches(instrumental))
        val result = AkartaryRnePancamiSutra.apply(positive) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PANCHAMI, result.vibhakti)
        assertEquals("2.3.24", result.evidence.sutra)
    }

    @Test
    fun `2 3 66 requires its exact construction semantics`() {
        data class Case(
            val rule: dev.panini.sutra.Sutra<VibhaktiRuleContext, VibhaktiRuleResult>,
            val relation: SemanticRelation,
            val vibhakti: Vibhakti,
        )
        listOf(
            Case(UbhayapraptauKarmaniSutra, SemanticRelation.KRT_DUAL_ELIGIBLE_OBJECT, Vibhakti.SASTHI),
        ).forEach { case ->
            val positive = VibhaktiRuleContext(
                karaka = Karaka.KARMAN,
                morphologicalCandidates = setOf(case.vibhakti),
                participant = dummyParticipant(setOf(case.relation, SemanticRelation.KRT_GOVERNED_PARTICIPANT)),
            )
            assertTrue(case.rule.matches(positive), case.rule.number)
            assertFalse(case.rule.matches(positive.copy(karaka = Karaka.KARANA)))
            assertFalse(case.rule.matches(positive.copy(participant = dummyParticipant(setOf(case.relation)))))
            assertFalse(case.rule.matches(positive.copy(participant = dummyParticipant())), case.rule.number)
            val result = case.rule.apply(positive) as VibhaktiRuleResult.Assigned
            assertEquals(case.vibhakti, result.vibhakti, case.rule.number)
            assertEquals(case.rule.number, result.evidence.sutra)
        }
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
    fun testAntarantarenaYukteSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.DVITIYA),
            participant = dummyParticipant(setOf(SemanticRelation.BETWEEN_OR_WITHOUT, SemanticRelation.ANTARA_NIPATA_LINK)),
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
            participant = dummyParticipant(setOf(SemanticRelation.ENAPA_SUFFIX)).copy(
                governingAvyaya = dev.panini.vyakaranam.ast.AvyayaPada("दक्षिणेन", "दक्षिणेन",
                    dev.panini.vyakaranam.ast.AvyayaTaddhitaDerivation("दक्षिण", "एनप्"))),
        )
        assertTrue(EnapaDvitiyaSutra.matches(context))
        val res = EnapaDvitiyaSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.DVITIYA, res.vibhakti)
        assertEquals("2.3.31", res.evidence.sutra)
    }

    @Test
    fun testPancamyApangParibhihSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.PANCHAMI),
            participant = dummyParticipant(setOf(SemanticRelation.EXCLUSION_LIMIT,
                SemanticRelation.KARMAPRAVACANIYA_GOVERNANCE, SemanticRelation.AP_ANG_PARI_KARMAPRAVACANIYA_LINK)),
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
            participant = dummyParticipant(setOf(SemanticRelation.REPRESENTATIVE_EXCHANGE,
                SemanticRelation.REPRESENTATIVE_EXCHANGE_REFERENCE, SemanticRelation.KARMAPRAVACANIYA_GOVERNANCE,
                SemanticRelation.PRATI_KARMAPRAVACANIYA_LINK)),
        )
        assertTrue(PratinidhiPratidaneCaYasmatSutra.matches(context))
        val res = PratinidhiPratidaneCaYasmatSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PANCHAMI, res.vibhakti)
        assertEquals("2.3.11", res.evidence.sutra)
    }

    @Test
    fun testKriyarthopapadasyaCaKarmaniSthaninahSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.KARMAN,
            morphologicalCandidates = setOf(Vibhakti.CHATURTHI),
            participant = dummyParticipant(setOf(SemanticRelation.IMPLIED_PURPOSE_OBJECT)).copy(
                purposeGovernor = dev.panini.analysis.PurposeActionGovernor.Implied("gather",
                    dev.panini.vyakaranam.ast.DhatuPrakriti("हृ", "हृ"), listOf("आङ्"))),
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
            participant = dummyParticipant(setOf(SemanticRelation.PURPOSE_ACTION, SemanticRelation.ACTION_NOUN_DENOTATION)).copy(
                expression = dev.panini.vyakaranam.ast.SubantaPada("पाकाय",
                    dev.panini.vyakaranam.ast.KridantaPratipadika("पाक", emptyList(),
                        dev.panini.vyakaranam.ast.DhatuPrakriti("पच्", "पच्"), "घञ्"),
                    dev.panini.vyakaranam.ast.SupPratyaya("ङे", "ङे"))),
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
            participant = dummyParticipant(setOf(SemanticRelation.EXPLICIT_HETU_USE, SemanticRelation.CAUSE_HETU)),
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
            participant = dummyParticipant(setOf(SemanticRelation.PRONOMINAL_HETU,
                SemanticRelation.EXPLICIT_HETU_USE, SemanticRelation.CAUSE_HETU)),
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
            participant = dummyParticipant(setOf(SemanticRelation.SPATIAL_DIRECTION)).copy(
                governingAvyaya = dev.panini.vyakaranam.ast.AvyayaPada("दक्षिणतः", "दक्षिणतः",
                    dev.panini.vyakaranam.ast.AvyayaTaddhitaDerivation("दक्षिण", "अतसुच्"))),
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
            participant = dummyParticipant(setOf(SemanticRelation.DISTANCE_OR_PROXIMITY, SemanticRelation.NON_SUBSTANCE_MEANING)).copy(
                expression = dev.panini.vyakaranam.ast.SubantaPada("दूर", dev.panini.vyakaranam.ast.MulaPratipadika("दूर", "दूर"),
                    dev.panini.vyakaranam.ast.SupPratyaya("ङसिँ", "ङसिँ"))),
        )
        assertTrue(DurantikarthebhyoDvitiyaCaSutra.matches(context))
        val res = DurantikarthebhyoDvitiyaCaSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PANCHAMI, res.vibhakti)
        assertEquals("2.3.35", res.evidence.sutra)
    }

    @Test
    fun testDurantikarthaihSasthiAnyatarasyamSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.DISTANCE_OR_PROXIMITY)).copy(
                governingNominal = dev.panini.vyakaranam.ast.SubantaPada("दूर", dev.panini.vyakaranam.ast.MulaPratipadika("दूर", "दूर"),
                    dev.panini.vyakaranam.ast.SupPratyaya("अम्", "अम्"))),
        )
        assertTrue(DurantikarthaihSasthiAnyatarasyamSutra.matches(context))
        val res = DurantikarthaihSasthiAnyatarasyamSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.34", res.evidence.sutra)
        assertTrue(DurantikarthaihSasthiAnyatarasyamSutra.optional)
    }

    @Test
    fun testAyuktaKusalabhyamCaSevayamSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI, Vibhakti.SAPTAMI),
            participant = dummyParticipant(setOf(SemanticRelation.ENGROSSED_ATTACHMENT)).copy(
                governingNominal = dev.panini.vyakaranam.ast.SubantaPada("कुशल",
                    dev.panini.vyakaranam.ast.MulaPratipadika("कुशल", "कुशल"),
                    dev.panini.vyakaranam.ast.SupPratyaya("सुँ", "सुँ"))),
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
            participant = dummyParticipant(setOf(SemanticRelation.COMPARATIVE_DISTINCTION,
                SemanticRelation.NIRDHARANA_REFERENCE, SemanticRelation.SEPARATE_COMPARISON_GROUPS)),
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
            participant = dummyParticipant(setOf(SemanticRelation.JNA_NON_KNOWLEDGE_SENSE,
                SemanticRelation.INSTRUMENT, SemanticRelation.SESHA_RELATION)),
            dhatu = DhatuIdentity("ज्ञा"),
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
        assertEquals("2.3.69", blocked.blockerSutraNumber)
        assertEquals("2.3.65", blocked.blockedTargetSutraNumber)
    }

    @Test
    fun testKrtyanamKartariVaSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.KARTR,
            morphologicalCandidates = setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.KRTYA_GOVERNED_AGENT)),
        )
        assertFalse(KrtyanamKartariVaSutra.matches(context.copy(participant = dummyParticipant())))
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
            participant = dummyParticipant(setOf(SemanticRelation.EQUAL_COMPARISON, SemanticRelation.SESHA_RELATION)).copy(
                governingNominal = dev.panini.vyakaranam.ast.SubantaPada("तुल्यः",
                    dev.panini.vyakaranam.ast.MulaPratipadika("तुल्य", "तुल्य"), dev.panini.vyakaranam.ast.SupPratyaya("सुँ", "सुँ"))),
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
            participant = dummyParticipant(setOf(SemanticRelation.BENEDICTION_WELLBEING, SemanticRelation.SESHA_RELATION)).copy(
                governingNominal = dev.panini.vyakaranam.ast.SubantaPada("आयुष्यम्",
                    dev.panini.vyakaranam.ast.MulaPratipadika("आयुष्य", "आयुष्य"), dev.panini.vyakaranam.ast.SupPratyaya("सुँ", "सुँ"))),
        )
        assertTrue(CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra.matches(context))
        val res = CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.CHATURTHI, res.vibhakti)
        assertEquals("2.3.73", res.evidence.sutra)
    }
}
