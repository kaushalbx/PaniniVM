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
        val res = SamantriteSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PRATHAMA, res.vibhakti)
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
        val res = EkavacanamSambuddhihSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.PRATHAMA, res.vibhakti)
        assertEquals("2.3.49", res.evidence.sutra)
    }

    @Test
    fun testKrjahPratiyatneSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.TRANSFORMATION_ENDOWMENT)),
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
            participant = dummyParticipant(setOf(SemanticRelation.TRANSACTION_GAMBLING_OBJECT)),
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
            participant = dummyParticipant(setOf(SemanticRelation.GAMBLING_INSTRUMENT)),
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
            participant = dummyParticipant(setOf(SemanticRelation.TRANSACTION_GAMBLING_OBJECT)),
        )
        assertTrue(VibhasopasargeSutra.matches(context))
        val res = VibhasopasargeSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.59", res.evidence.sutra)
    }

    @Test
    fun testDvisahKarmaniSutra() {
        val context = VibhaktiRuleContext(
            karaka = Karaka.ANIRDHARITA,
            morphologicalCandidates = setOf(Vibhakti.SASTHI),
            participant = dummyParticipant(setOf(SemanticRelation.HATRED_PARTICIPLE_OBJECT)),
        )
        assertTrue(DvisahKarmaniSutra.matches(context))
        val res = DvisahKarmaniSutra.apply(context) as VibhaktiRuleResult.Assigned
        assertEquals(Vibhakti.SASTHI, res.vibhakti)
        assertEquals("2.3.62", res.evidence.sutra)
    }

    @Test
    fun testAkasyaCaBhavisyadadamarnyayohSutraProhibition() {
        val ctx = ProhibitionContext(
            targetSutraNumber = "2.3.65",
            isAkaFutureOrDebtAffix = true,
        )
        val res = NishedhaRuleEngine.evaluateProhibition(ctx) as NishedhaRuleResult.Blocked
        assertEquals("2.3.70", res.blockerSutraNumber)
        assertEquals("अकस्य च भविष्यदाधमर्ण्ययोः", res.blockerSutraText)
    }
}
