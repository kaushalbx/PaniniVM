package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vacana
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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class FinalVibhaktiSutrasTest {

    private fun dummyParticipant(
        relations: Set<SemanticRelation> = emptySet(),
        vacana: Vacana = Vacana.EKAVACANA,
        possibleVibhaktis: Set<Vibhakti> = Vibhakti.values().toSet(),
    ) = ParticipantFacts(
        id = "test-participant",
        expression = AvyayaPada(sourceText = "test", form = "test"),
        vacana = vacana,
        possibleVibhaktis = possibleVibhaktis,
        semanticRelations = relations,
    )

    @Test
    fun testSamantriteSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.SAMBODHANA,
            morphologicalCandidates = setOf(Vibhakti.PRATHAMA),
        )
        assertTrue(SamantriteSutra.matches(context))
        val res = SamantriteSutra.apply(context)
        assertEquals(dev.panini.analysis.NominalDesignation.AMANTRITA, res.designation)
        assertEquals("2.3.48", res.evidence.sutra)
    }

    @Test
    fun testEkavacanamSambuddhihSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.SAMBODHANA,
            morphologicalCandidates = setOf(Vibhakti.PRATHAMA),
            participant = dummyParticipant(vacana = Vacana.EKAVACANA),
        )
        assertTrue(EkavacanamSambuddhihSutra.matches(context))
        val res = EkavacanamSambuddhihSutra.apply(context)
        assertEquals(dev.panini.analysis.NominalDesignation.SAMBUDDHI, res.designation)
        assertEquals("2.3.49", res.evidence.sutra)
    }

    @Test
    fun testKrjahPratiyatneSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.TRANSFORMATION_ENDOWMENT,
                SemanticRelation.EXISTING_SUBSTRATE, SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)),
            dhatu = dev.panini.analysis.DhatuIdentity("कृ"),
        )
        assertTrue(KrjahPratiyatneSutra.matches(context))
        val res = KrjahPratiyatneSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.53", res.evidence.sutra)
    }

    @Test
    fun testVyavahruPanohSamarthayohSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.TRANSACTION_GAMBLING_OBJECT,
                SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)),
            dhatu = dev.panini.analysis.DhatuIdentity("पण्"),
        )
        assertTrue(VyavahruPanohSamarthayohSutra.matches(context))
        val res = VyavahruPanohSamarthayohSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.57", res.evidence.sutra)
    }

    @Test
    fun testDivasTadarthasyaSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.TRANSACTION_GAMBLING_OBJECT,
                SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)),
            dhatu = dev.panini.analysis.DhatuIdentity("दिव्"),
        )
        assertTrue(DivasTadarthasyaSutra.matches(context))
        val res = DivasTadarthasyaSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.58", res.evidence.sutra)
    }

    @Test
    fun testVibhasopasargeSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI, Vibhakti.DVITIYA),
            participant = dummyParticipant(setOf(SemanticRelation.TRANSACTION_GAMBLING_OBJECT,
                SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)),
            dhatu = dev.panini.analysis.DhatuIdentity("दिव्"),
            verbNode = dev.panini.vyakaranam.ast.TingantaPada("प्रतिदीव्यति", listOf("प्रति"),
                dev.panini.vyakaranam.ast.DhatuPrakriti("दिव्", "दिव्"), dev.panini.core.Lakara.LAT,
                dev.panini.vyakaranam.ast.TingPratyaya("तिप्", "तिप्")),
        )
        assertTrue(VibhasopasargeSutra.matches(context))
        assertFalse(VibhasopasargeSutra.matches(context.copy(abhihita = true)))
        assertFalse(VibhasopasargeSutra.matches(context.copy(karaka = Karaka.KARANA)))
        assertFalse(VibhasopasargeSutra.matches(context.copy(verbNode = null)))
        val res = VibhasopasargeSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.59", res.evidence.sutra)
    }

    @Test
    fun testAkasyaCaBhavisyadadamarnyayohSutraProhibition() {
        val ctx = ProhibitionContext(
            targetSutraNumber = "2.3.65",
            isAkaFutureOrDebtAffix = true,
        )
        val res = NishedhaRuleEngine.evaluateProhibition(ctx) as NishedhaRuleResult.Blocked
        assertEquals("2.3.70", res.blockerSutraNumber)
        assertEquals("अकेनोर्भविष्यदाधमर्ण्ययोः", res.blockerSutraText)
    }
}
