package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class FieldAssertionDiagnosticParityTest {
    @Test
    fun `disconnected attribute reference has interpreter compiler diagnostic parity`() {
        val source = "गुण + मतुप् + ङस् न मूल्य + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val interpreted = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).single())
        val interpretedDiagnostic = assertNotNull(interpreted.diagnostic)
        assertEquals("ATTRIBUTE_REFERENCE_DISCONNECTED", interpretedDiagnostic.code)

        val compiled = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compile(source, "DisconnectedAttributeReference")
        }
        val compiledDiagnostic = assertNotNull(compiled.diagnostic)
        assertEquals(interpretedDiagnostic.code, compiledDiagnostic.code)
        assertEquals(interpretedDiagnostic.span, compiledDiagnostic.span)
    }

    @Test
    fun `result schema frame diagnostic has interpreter compiler parity`() {
        val source = "मूल्य + सुँ फल + सुँ च अवस्था + ङस् क्षेत्र + जस् द्वि + अम् असँ + लट् + झि ।"
        val interpreted = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).single())
        val interpretedDiagnostic = assertNotNull(interpreted.diagnostic)
        assertEquals("RESULT_SCHEMA_FRAME", interpretedDiagnostic.code)

        val compiled = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compile(source, "InvalidResultSchemaFrame")
        }
        val compiledDiagnostic = assertNotNull(compiled.diagnostic)
        assertEquals(interpretedDiagnostic.code, compiledDiagnostic.code)
        assertEquals(interpretedDiagnostic.span, compiledDiagnostic.span)
    }

    @Test
    fun `field assertion copula diagnostic has interpreter compiler parity`() {
        val source = "अवस्था + मतुप् + ङस् मूल्य + सुँ द्वि + सुँ असँ + लृट् + तिप् ।"
        val interpreted = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).single())
        val interpretedDiagnostic = assertNotNull(interpreted.diagnostic)
        assertEquals("FIELD_ASSERTION_COPULA_MORPHOLOGY", interpretedDiagnostic.code)

        val compiled = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compile(source, "InvalidFieldAssertionCopula")
        }
        val compiledDiagnostic = assertNotNull(compiled.diagnostic)
        assertEquals(interpretedDiagnostic.code, compiledDiagnostic.code)
        assertEquals(interpretedDiagnostic.span, compiledDiagnostic.span)
    }
}
