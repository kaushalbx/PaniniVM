package dev.panini.execution

import java.nio.file.Path
import org.junit.jupiter.api.io.TempDir
import kotlin.test.*

class HistoricalParticipantSnapshotTest {
    @TempDir lateinit var directory: Path

    @Test fun `public evaluation rejects cyclic host values before binding caches`() {
        val members = mutableListOf<SanskritValue>()
        val cyclic = SanskritValue.Suchi(members)
        members += cyclic
        val vm = PaniniVM(directory.toFile())
        val result = vm.eval("सूची + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            scope = vm.defaultScope.copy(environment = ValueEnvironment(mapOf("सूची" to cyclic))))
        assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(result).error)
        assertTrue(result.message.contains("cyclic"))
        val shared = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1, "एक")))
        validateAcyclicValues(listOf(SanskritValue.Gana(listOf(shared, shared))))
    }

    @Test fun `cyclic participant payload returns a language failure before display`() {
        val members = mutableListOf<SanskritValue>()
        val cyclic = SanskritValue.Suchi(members)
        members += cyclic
        val source = "सूची + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val initial = ValueEnvironment(mapOf("सूची" to SanskritValue.Suchi(emptyList())))
        val bound = assertIs<ExecutionBindingResult.Bound>(dev.panini.execution.binding.VyakaranamExecutionAdapter.bind(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", source), SambhashanaContext("प्रयोक्ता", "यन्त्रम्"), environment = initial))
        val planning = assertIs<PlanningResult.Planned>(ExecutionPlanner.plan(ExecutionProgram(bound.ukti), initial))
        val phala = assertIs<Phala.Asiddha>(ExecutionRuntime.execute(planning, ExecutionScope(),
            environment = ValueEnvironment(mapOf("सूची" to cyclic))))
        val result = phala.result
        assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(result).error)
        assertTrue(result.message.contains("cyclic"))
    }

    @Test fun `participant values survive reassignment and session restart`() {
        val vm = PaniniVM(directory.toFile())
        val source = "एक + अम् मान + ङे दा + लोट् + सिप् ।\n" +
            "मान + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + अम् मान + ङे दा + लोट् + सिप् ।"
        assertTrue(vm.evalScript(source, sessionKey = "history").all { it is ExecutionResult.Success })
        val query = "युज् + ल्युट् + ङस् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("एक द्वि", assertIs<ExecutionResult.Success>(vm.eval(query, sessionKey = "history")).value)
        val restarted = PaniniVM(directory.toFile())
        assertEquals("एक द्वि", assertIs<ExecutionResult.Success>(restarted.eval(query, sessionKey = "history")).value)
    }

    @Test fun `history snapshots detach nested mutable payloads`() {
        val items = mutableListOf<SanskritValue>(SanskritValue.Sankhya(1, "एक"))
        val original = SanskritValue.Rupa("परीक्षण", mapOf("सूची" to SanskritValue.Suchi(items)))
        val captured = original.historySnapshot() as SanskritValue.Rupa
        items[0] = SanskritValue.Sankhya(9, "नवन्")
        assertEquals(1L, (((captured.fields.getValue("सूची") as SanskritValue.Suchi).items.single()) as SanskritValue.Sankhya).value)
    }

    @Test fun `participant snapshots survive repeated approval suspension`() {
        val vm = PaniniVM(directory.toFile())
        assertIs<ExecutionResult.Success>(vm.eval("एक + अम् मान + ङे दा + लोट् + सिप् ।", sessionKey = "paused"))
        val scope = vm.defaultScope.copy(capabilities = setOf(ExecutionEffect.PURE))
        val paused = assertIs<ExecutionResult.NeedsApproval>(vm.eval(
            "मान + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ततः वार्ता + अम् प्रेष् + णिच् + लोट् + सिप् ।",
            sessionKey = "paused", scope = scope))
        val again = assertIs<ExecutionResult.NeedsApproval>(vm.resume(paused.continuation, sessionKey = "paused", scope = scope))
        assertIs<ExecutionResult.Success>(vm.resume(again.continuation, sessionKey = "paused",
            scope = scope.copy(capabilities = scope.capabilities + paused.requiredEffects)))
        assertIs<ExecutionResult.Success>(vm.eval("नवन् + अम् मान + ङे दा + लोट् + सिप् ।", sessionKey = "paused"))
        assertEquals("एक द्वि", assertIs<ExecutionResult.Success>(vm.eval(
            "युज् + ल्युट् + ङस् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।", sessionKey = "paused")).value)
    }
}
