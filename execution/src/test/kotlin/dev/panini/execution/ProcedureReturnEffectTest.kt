package dev.panini.execution

import dev.panini.vyakaranam.ast.WhileLoop
import kotlin.test.*

class ProcedureReturnEffectTest {
    @Test fun `return crosses reported-result loop without truth checking or loop completion`() {
        val statement = assertIs<PvmScriptStatement.Sentence>(PvmScript.parse(
            "यावत् फल + सुँ न तावत् एक + अम् मुद्र् + णिच् + लोट् + सिप् ।").single())
        val returned = ExecutionResult.Success("एक", "return", typedValue = SanskritValue.Sankhya(1L, "एक"),
            controlSignal = ExecutionControlSignal.RETURN_PROCEDURE)
        var calls = 0
        val structures = mutableMapOf<String, TaddhitaStruct>()
        val published = mutableListOf<ExecutionResult>()
        val results = PvmLoopExecutor().execute(PvmLoopExecutor.Request(
            loop = assertIs<WhileLoop>(statement.program), scope = ExecutionScope(),
            structStore = structures, structSchemas = emptyMap(), hostBudget = 2L,
            executeNode = { _, _, _ -> calls++; listOf(returned) },
            evaluateCondition = { _, _ -> error("Reported loop should not evaluate a predicate") },
            resolveCondition = { error("Reported loop should not resolve a predicate") },
            onResult = published::add,
        ))
        assertEquals(listOf(returned), results)
        assertEquals(1, calls)
        assertTrue(structures.isEmpty())
        assertTrue(published.isEmpty(), "A return must not publish a loop-completion result")
    }
}
