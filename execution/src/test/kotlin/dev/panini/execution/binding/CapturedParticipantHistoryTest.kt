package dev.panini.execution.binding

import dev.panini.core.Karaka
import dev.panini.execution.*
import dev.panini.execution.memory.KriyaMemory
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class CapturedParticipantHistoryTest {
    @Test
    fun `a grammatical frame without captured values cannot fabricate value history`() {
        val vm = PaniniVM()
        assertIs<ExecutionResult.Success>(vm.eval(
            "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "captured"))
        val remembered = vm.kriyaMemory("captured").entries.last()
        val padas = PaniniParser().parse(
            "युज् + ल्युट् + ङस् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
            .grammaticalVakyas().single().padas
        val context = BindingContext(null, 0, assertNotNull(remembered.frame.kriya?.dhatu),
            remembered.frame, KriyaMemory(listOf(remembered)), emptyList())
        val resolved = KarakaReferenceResolver.resolve(padas, padas.filterIsInstance<SubantaPada>(), context)
        assertEquals(1, resolved.expressions.size)
        val missing = context.copy(memory = KriyaMemory(listOf(remembered.copy(participants = emptyMap()))))
        val error = assertFailsWith<MissingActionResultException> {
            KarakaReferenceResolver.resolve(padas, padas.filterIsInstance<SubantaPada>(), missing)
        }
        assertTrue(error.message.orEmpty().contains("captured participant values"))
    }

    @Test
    fun `relation queries include an established coexisting name exactly once`() {
        val vm = PaniniVM()
        assertIs<ExecutionResult.Success>(vm.eval(
            "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "relations"))
        val remembered = vm.kriyaMemory("relations").entries.last()
        val objectRelation = remembered.frame.relations.first {
            (it.resolution as? dev.panini.analysis.FrameKarakaResolution.Resolved)?.karaka == Karaka.KARMAN
        }
        // This unit fixture exercises storage of an already established relation;
        // grammatical licensing is tested separately by the canonical rule tests.
        val shared = objectRelation.copy(coexistingKarakas = setOf(Karaka.KARMAN, Karaka.KARANA))
        val memory = KriyaMemory(listOf(remembered.copy(frame = remembered.frame.copy(relations = listOf(shared)))))
        assertEquals(listOf(shared), memory.latestKarakaRelations(Karaka.KARMAN, 2))
        assertEquals(listOf(shared), memory.latestKarakaRelations(Karaka.KARANA, 2))
        assertTrue(memory.latestKarakaRelations(Karaka.APADANA).isEmpty())
    }
}
