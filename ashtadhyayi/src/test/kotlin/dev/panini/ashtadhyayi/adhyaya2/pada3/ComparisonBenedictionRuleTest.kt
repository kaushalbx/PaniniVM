package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ComparisonBenedictionRuleTest {
    private fun context(stem: String, meaning: SemanticRelation, cases: Set<Vibhakti>) = VibhaktiRuleContext(
        Karaka.ANIRDHARITA, cases, participant = ParticipantFacts("p", AvyayaPada("p", "p"), cases,
            semanticRelations = setOf(meaning, SemanticRelation.SESHA_RELATION),
            governingNominal = SubantaPada(stem, MulaPratipadika(stem, stem), SupPratyaya("सुँ", "सुँ"))))

    @Test fun `special nonkaraka cases require their construction and do not reassign instrument or recipient`() {
        val examples = listOf(
            TulyarthairAtulopamabhyamTrtiyanatarasyamSutra to context("तुल्य", SemanticRelation.EQUAL_COMPARISON,
                setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI)),
            CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra to context("हित", SemanticRelation.BENEDICTION_WELLBEING,
                setOf(Vibhakti.CHATURTHI, Vibhakti.SASTHI)))
        for ((rule, ctx) in examples) {
            assertTrue(rule.matches(ctx))
            assertFalse(SasthiSeseSutra.matches(ctx))
            assertFalse(rule.matches(ctx.copy(abhihita = true)))
            for (role in listOf(Karaka.KARANA, Karaka.SAMPRADANA, Karaka.KARMAN)) {
                assertFalse(rule.matches(ctx.copy(karaka = role)))
            }
            val participant = requireNotNull(ctx.participant)
            for (missing in participant.semanticRelations) {
                assertFalse(rule.matches(ctx.copy(participant = participant.copy(
                    semanticRelations = participant.semanticRelations - missing))))
            }
            assertFalse(rule.matches(ctx.copy(participant = participant.copy(governingNominal = null))))
            assertFalse(rule.matches(ctx.copy(morphologicalCandidates = setOf(Vibhakti.SAPTAMI))))
        }
    }
}
