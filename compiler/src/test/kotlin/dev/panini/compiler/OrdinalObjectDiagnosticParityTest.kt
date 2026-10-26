package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class OrdinalObjectDiagnosticParityTest {
    @Test
    fun `derived collection selector diagnostic has interpreter compiler parity`() {
        val source = "एक + ङस् द्वि + ओस् च सूची + सुँ असँ + लट् + तिप् ।\n" +
            "सूची + ङस् प्रथम + मतुप् + अम् उद् + हृ + लोट् + सिप् ।"
        val interpreted = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val interpretedDiagnostic = assertNotNull(interpreted.diagnostic)
        assertEquals("COLLECTION_DERIVED_ORDINAL_SELECTOR", interpretedDiagnostic.code)

        val compiled = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compile(source, "InvalidDerivedCollectionSelector")
        }
        val compiledDiagnostic = assertNotNull(compiled.diagnostic)
        assertEquals(interpretedDiagnostic.code, compiledDiagnostic.code)
        assertEquals(interpretedDiagnostic.span, compiledDiagnostic.span)
    }

    @Test
    fun `ordinal object agreement diagnostic has interpreter compiler parity`() {
        val source = "एक + ङस् सूची + सुँ असँ + लट् + तिप् ।\n" +
            "सूची + ङस् प्रथम + टा मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।"
        val interpreted = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val interpretedDiagnostic = assertNotNull(interpreted.diagnostic)
        assertEquals("ORDINAL_OBJECT_CASE_NUMBER_AGREEMENT", interpretedDiagnostic.code)
        assertNotNull(interpretedDiagnostic.span)

        val compiled = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compile(source, "InvalidOrdinalObjectAgreement")
        }
        val compiledDiagnostic = assertNotNull(compiled.diagnostic)
        assertEquals(interpretedDiagnostic.code, compiledDiagnostic.code)
        assertEquals(interpretedDiagnostic.span, compiledDiagnostic.span)
    }
}
