package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class PriorActionDiagnosticParityTest {
    @Test
    fun `prior action agent diagnostic has interpreter compiler parity`() {
        val source = "राम + सुँ एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा " +
            "सीता + सुँ फल + अम् मुद्र् + णिच् + लट् + तिप् ।"
        val interpreted = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val interpretedDiagnostic = assertNotNull(interpreted.diagnostic)
        assertEquals("PRIOR_ACTION_AGENT_IDENTITY", interpretedDiagnostic.code)

        val compiled = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compile(source, "InvalidPriorActionAgent")
        }
        val compiledDiagnostic = assertNotNull(compiled.diagnostic)
        assertEquals(interpretedDiagnostic.code, compiledDiagnostic.code)
        assertEquals(interpretedDiagnostic.span, compiledDiagnostic.span)
    }
}
