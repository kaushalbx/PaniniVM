package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.execution.sutra.ProgramSutraArthaCodec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HistoryParticipantTest {
    @Test
    fun `coexisting names retain occurrence order without aliasing ordinary objects`() {
        val ordinary = ExecutionExpression.Pada("ordinary")
        val shared = ExecutionExpression.Reference("dice")
        val value = SanskritValue.of("captured")
        val context = ExecutionContext(
            bindings = mapOf(Karaka.KARMAN to ordinary, Karaka.KARANA to shared),
            variables = mapOf("dice" to value),
            historyParticipants = listOf(
                HistoryParticipant(shared, Karaka.KARANA, setOf(Karaka.KARMAN, Karaka.KARANA)),
                HistoryParticipant(ordinary, Karaka.KARMAN),
                HistoryParticipant(shared, Karaka.KARANA, setOf(Karaka.KARMAN, Karaka.KARANA)),
            ),
        )
        val captured = context.historyParticipantValues()
        assertEquals(listOf(value, SanskritValue.of("ordinary"), value), captured[Karaka.KARMAN])
        assertEquals(listOf(value, value), captured[Karaka.KARANA])
        assertEquals(2, context.bindings.size)
    }

    @Test
    fun `history metadata survives semantic codec round trip`() {
        val occurrence = HistoryParticipant(ExecutionExpression.Reference("dice"), Karaka.KARANA,
            linkedSetOf(Karaka.KARANA, Karaka.KARMAN))
        assertEquals(occurrence, ProgramSutraArthaCodec.decodeHistoryParticipant(
            ProgramSutraArthaCodec.encodeHistoryParticipant(occurrence)))
        val ambiguous = HistoryParticipant(ExecutionExpression.Pada("dual"), null, ambiguousIndex = 2)
        assertEquals(ambiguous, ProgramSutraArthaCodec.decodeHistoryParticipant(
            ProgramSutraArthaCodec.encodeHistoryParticipant(ambiguous)))
    }

    @Test
    fun `missing and unresolved occurrences cannot be recorded as completed participants`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutionContext(historyParticipants = listOf(
                HistoryParticipant(ExecutionExpression.Reference("missing"), Karaka.KARMAN),
            )).historyParticipantValues()
        }
        assertFailsWith<IllegalArgumentException> {
            ExecutionContext(historyParticipants = listOf(
                HistoryParticipant(ExecutionExpression.Pada("dual"), null, ambiguousIndex = 0),
            )).historyParticipantValues()
        }
    }
}
