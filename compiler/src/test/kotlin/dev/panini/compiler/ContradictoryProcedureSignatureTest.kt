package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class ContradictoryProcedureSignatureTest {
    @Test fun `contradictory declarations fail before procedure body effects`() {
        for (declarations in listOf(
            "सङ्ख्या + सुँ इति परिणाम + सुँ ।\nशब्द + सुँ इति परिणाम + सुँ ।",
            "मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\nमान + सुँ शब्द + सुँ इति मान + सुँ ।",
            "न प्रथम + अम् सङ्ख्या + त्व + अम् ।\nन प्रथम + अम् शब्द + त्व + अम् ।",
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
