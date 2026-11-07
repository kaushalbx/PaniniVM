package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class ContradictoryProcedureSignatureTest {
    @Test fun `contradictory declarations fail before procedure body effects`() {
        for (declarations in listOf(
            "परिणाम + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।\nपरिणाम + सुँ शब्द + सुँ असँ + लट् + तिप् ।",
            "मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।\nमान + सुँ नाम मान + सुँ शब्द + सुँ असँ + लट् + तिप् ।",
            "प्रथम + सुँ सङ्ख्या + त्व + सुँ न भू + लिङ् + तिप् ।\nप्रथम + सुँ शब्द + त्व + सुँ न भू + लिङ् + तिप् ।",
        )) {
            val source = """
                परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
                $declarations
                एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥
                परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
            """.trimIndent()
            val published = mutableListOf<ExecutionResult>()
            val results = PaniniVM().evalScript(source, onResult = published::add)
            assertIs<ExecutionResult.Failure>(results.last())
            assertTrue(published.none { it is ExecutionResult.Success }, published.toString())
            assertFailsWith<IllegalArgumentException> { CompilerFrontend.lower(source, "InvalidSignature") }
            assertTrue(PrakriyaScriptValidator.validate(source).any { it.severity == PrakriyaDiagnosticSeverity.ERROR })
        }
    }
}
