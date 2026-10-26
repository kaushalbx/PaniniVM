package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class ProcedureResultStatementTest {
    private fun statement(source: String) = PaniniParser().parse(source).body as Invocation

    @Test fun `result meaning requires the complete nominative present copular relation`() {
        val valid = statement("फल + सुँ शून्य + सुँ असँ + लट् + तिप् ।")
        assertNotNull(ProcedureResultStatement.resolve(valid))
        assertNull(ProcedureResultStatement.resolve(valid, roleShadowed = true))
        for (source in listOf(
            "शून्य + अम् फल + ङे दा + लोट् + सिप् ।",
            "फल + सुँ शून्य + टा सम + सुँ असँ + लट् + तिप् ।",
            "परिणाम + सुँ शून्य + सुँ असँ + लट् + तिप् ।",
            "फल + सुँ शून्य + सुँ असँ + लृट् + तिप् ।",
            "फल + अम् शून्य + सुँ असँ + लट् + तिप् ।",
            "फल + सुँ शून्य + अम् असँ + लट् + तिप् ।",
        )) assertNull(ProcedureResultStatement.resolve(statement(source)), source)
    }

    @Test fun `ambiguous results and incompatible finite number cannot finalize a procedure`() {
        val cases = listOf(
            "फल + सुँ शून्य + सुँ मान + सुँ असँ + लट् + तिप् ।" to "PROCEDURE_RESULT_CARDINALITY",
            "फल + सुँ शून्य + सुँ असँ + लट् + तस् ।" to "PROCEDURE_RESULT_AGREEMENT",
        )
        for ((source, code) in cases) {
            val invalid = assertIs<ProcedureResultStatement.Analysis.Invalid>(
                ProcedureResultStatement.analyze(statement(source)), source)
            assertEquals(code, invalid.diagnostic.code)
            assertNotNull(invalid.diagnostic.span)
            assertFailsWith<IllegalArgumentException>(source) {
                ProcedureResultStatement.resolve(statement(source))
            }
        }
    }

    @Test fun `old give-back command is not a procedure return alias`() {
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript("""
            परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
            एक + अम् प्रति + ऋ + णिच् + लोट् + सिप् ॥
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()).last())
    }
}
