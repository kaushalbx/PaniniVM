package dev.panini.analysis

import dev.panini.core.Vibhakti
import kotlin.test.*

class CoordinatedValencyTest {
    private val shared = setOf(Vibhakti.TRTIYA, Vibhakti.CHATURTHI, Vibhakti.PANCHAMI)
    private val recipient = setOf(SemanticRelation.RECIPIENT)

    @Test fun `explicit homogeneous coordination occupies one recipient position`() {
        val cases = listOf(setOf(Vibhakti.DVITIYA), shared, shared)
        assertEquals(listOf(emptySet(), recipient, recipient),
            ParticipantRelationInferrer.distributeDhatuRelations(cases, recipient, listOf(setOf(1, 2))))
        assertEquals(listOf(emptySet(), emptySet(), emptySet()),
            ParticipantRelationInferrer.distributeDhatuRelations(cases, recipient))
    }

    @Test fun `another eligible participant or mixed morphology retains uncertainty`() {
        assertTrue(ParticipantRelationInferrer.distributeDhatuRelations(
            listOf(shared, shared, shared), recipient, listOf(setOf(0, 1)),
        ).all { it.isEmpty() })
        assertTrue(ParticipantRelationInferrer.distributeDhatuRelations(
            listOf(shared, setOf(Vibhakti.CHATURTHI, Vibhakti.PANCHAMI)), recipient, listOf(setOf(0, 1)),
        ).all { it.isEmpty() })
    }

    @Test fun `coordination indices must be valid and nonoverlapping`() {
        assertFailsWith<IllegalArgumentException> {
            ParticipantRelationInferrer.distributeDhatuRelations(listOf(shared), recipient, listOf(setOf(1)))
        }
        assertFailsWith<IllegalArgumentException> {
            ParticipantRelationInferrer.distributeDhatuRelations(listOf(shared), recipient, listOf(setOf(0), setOf(0)))
        }
    }
}
