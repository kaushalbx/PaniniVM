package dev.panini.execution

import dev.panini.vyakaranam.ast.Conditional
import kotlin.test.*

class ConditionalEffectPropagationTest {
    @Test fun `condition failure is preserved and no branch executes from an earlier truth`() {
        val statement = assertIs<PvmScriptStatement.Sentence>(PvmScript.parse(
            "यदि एक + अम् एक + अम् च विद् + णिच् + लोट् + सिप् तर्हि राम + अम् मुद्र् + णिच् + लोट् + सिप् ।").single())
        val condition = assertIs<Conditional>(statement.program)
        val failure = ExecutionResult.Failure(ExecutionError.ACTION_FAILED, "condition failed")
        val produced = listOf(ExecutionResult.Success(operation = "condition", value = "सत्यम्",
            typedValue = SanskritValue.Satya(true)), failure)
        var branchExecuted = false
        val vm = PaniniVM()
        val result = PvmConditionalExecutor(vm, StructuredValueExecutor()).execute(
            PvmConditionalExecutor.Request(condition,
                executeNode = { _, evaluating ->
                    if (!evaluating) branchExecuted = true
                    produced
                }, sessionKey = "condition-effects", scope = vm.defaultScope,
                speaker = "प्रयोक्ता", listener = "यन्त्रम्", persistSession = false,
                structStore = emptyMap(), onResult = null))
        assertEquals(produced, result)
        assertFalse(branchExecuted)
    }
}
