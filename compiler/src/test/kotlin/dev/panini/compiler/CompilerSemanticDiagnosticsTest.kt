package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull

/** An ungrammatical source is an INVALID_SOURCE with the same diagnostic code in both backends. */
class CompilerSemanticDiagnosticsTest {
    private val cases = listOf(
        "त्रयोदशन् + शस् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।" to "SEGMENTED_NUMERAL",
        "एक + दशन् + अम् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।" to "NUMERAL_NUMBER_AGREEMENT",
        "द्वि + सुँ मुद्र् + णिच् + लोट् + सिप् ।" to "NUMERAL_NUMBER_AGREEMENT",
        "सङ्ख्या + अम् गणना + अम् च युज् + णिच् + लोट् + सिप् ।\nतद् + अम् मुद्र् + णिच् + लोट् + सिप् ।" to "AMBIGUOUS_ANTECEDENT",
    )

    @Test fun `the compiler and the interpreter report the same typed diagnostic`() {
        cases.forEachIndexed { index, (source, code) ->
            val compiled = assertFailsWith<CompilerUnsupportedException>(source) {
                BytecodeCompiler.compile(source, "SemanticDiagnostic$index")
            }
            assertEquals(CompilerUnsupportedKind.INVALID_SOURCE, compiled.kind, source)
            assertEquals(code, assertNotNull(compiled.diagnostic, source).code, source)

            val interpreted = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last(), source)
            assertEquals(code, interpreted.diagnostic?.code, source)
        }
    }
}
