package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertEquals

class MotionGoalVibhaktiTest {
    private val premises = setOf(SemanticRelation.MOTION_GOAL, SemanticRelation.PHYSICAL_MOTION,
        SemanticRelation.NON_PATH_DESTINATION)
    private fun context(relations: Set<SemanticRelation> = premises) = VibhaktiRuleContext(
        Karaka.KARMAN, setOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI),
        participant = ParticipantFacts("goal", AvyayaPada("ग्राम", "ग्राम"),
            setOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI), semanticRelations = relations))

    @Test fun `all stated conditions required and general accusative defers`() {
        assertTrue(GatyarthaKarmaniDvitiyaCaturthyauSutra.matches(context()))
        assertFalse(KarmaniDvitiyaSutra.matches(context()))
        premises.forEach { missing ->
            assertFalse(GatyarthaKarmaniDvitiyaCaturthyauSutra.matches(context(premises - missing)), missing.name)
            assertTrue(KarmaniDvitiyaSutra.matches(context(premises - missing)), missing.name)
        }
        assertFalse(GatyarthaKarmaniDvitiyaCaturthyauSutra.matches(context().copy(abhihita = true)))
        assertFalse(GatyarthaKarmaniDvitiyaCaturthyauSutra.matches(context().copy(karaka = Karaka.KARANA)))
    }
    @Test fun `both licensed cases retain canonical evidence`() {
        listOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI).forEach { case ->
            val c = context().copy(morphologicalCandidates = setOf(case))
            assertTrue(GatyarthaKarmaniDvitiyaCaturthyauSutra.matches(c))
            val result = GatyarthaKarmaniDvitiyaCaturthyauSutra.apply(c) as VibhaktiRuleResult.Assigned
            assertEquals(case, result.vibhakti)
            assertEquals("2.3.12", result.evidence.sutra)
        }
    }
}
