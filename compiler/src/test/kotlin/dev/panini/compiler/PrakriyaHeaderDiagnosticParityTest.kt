package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class PrakriyaHeaderDiagnosticParityTest {
    @Test
    fun `malformed procedure header has interpreter compiler diagnostic parity`() {
        val source = """
            गणन + अम् नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val interpreted = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).single())
        val interpretedDiagnostic = assertNotNull(interpreted.diagnostic)
        assertEquals("PRAKRIYA_OPERATION_AGREEMENT", interpretedDiagnostic.code)
        val span = assertNotNull(interpretedDiagnostic.span)
        assertEquals("गणन + अम्", source.substring(
            span.start,
            span.endExclusive,
        ))

        val compiled = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compile(source, "MalformedPrakriyaHeader")
        }
        assertEquals(interpretedDiagnostic, compiled.diagnostic)
    }
}
