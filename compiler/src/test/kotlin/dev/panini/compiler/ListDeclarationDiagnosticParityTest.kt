package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class ListDeclarationDiagnosticParityTest {
    @Test
    fun `list declaration agreement diagnostic has interpreter compiler parity`() {
        val source = "एक + ङस् सूची + सुँ असँ + लट् + तस् ।"
        val interpreted = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val interpretedDiagnostic = assertNotNull(interpreted.diagnostic)
        assertEquals("LIST_DECLARATION_COPULA_AGREEMENT", interpretedDiagnostic.code)
        assertNotNull(interpretedDiagnostic.span)

        val compiled = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compile(source, "InvalidListDeclarationAgreement")
        }
        val compiledDiagnostic = assertNotNull(compiled.diagnostic)
        assertEquals(interpretedDiagnostic.code, compiledDiagnostic.code)
        assertEquals(interpretedDiagnostic.span, compiledDiagnostic.span)
    }
}
