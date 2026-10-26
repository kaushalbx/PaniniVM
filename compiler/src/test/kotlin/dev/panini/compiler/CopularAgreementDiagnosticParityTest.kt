package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class CopularAgreementDiagnosticParityTest {
    @Test
    fun `copular predicate number diagnostic has interpreter compiler parity`() {
        val source = "एक + सुँ द्वि + ङसिँ न्यून + औ असँ + लट् + तिप् ।"
        val interpreted = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val interpretedDiagnostic = assertNotNull(interpreted.diagnostic)
        assertEquals("COPULAR_PREDICATE_NUMBER_AGREEMENT", interpretedDiagnostic.code)

        val compiled = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compile(source, "InvalidCopularPredicateNumber")
        }
        val compiledDiagnostic = assertNotNull(compiled.diagnostic)
        assertEquals(interpretedDiagnostic.code, compiledDiagnostic.code)
        assertEquals(interpretedDiagnostic.span, compiledDiagnostic.span)
    }
}
