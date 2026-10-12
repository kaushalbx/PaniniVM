package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class DerivedReferentAgreementTest {
    @Test fun `two feminine derived parameters remain ambiguous rather than selected by recency`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + टाप् + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            द्वितीय + टाप् + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            तद् + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            नवन् + शस् द्वि + औट् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        val failure = assertIs<ExecutionResult.Failure>(results.last(), results.toString())
        assertTrue(failure.toString().contains("Ambiguous तद् antecedent"), failure.toString())
        val compileFailure = assertFailsWith<IllegalArgumentException> {
            CompilerFrontend.lower(source, "AmbiguousDerivedFeminineReferents")
        }
        assertTrue(compileFailure.message.orEmpty().contains("Ambiguous तद् antecedent"), compileFailure.toString())
    }

    @Test fun `feminine derived parameter keeps live pronoun identity in both backends`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + टाप् + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            द्वि + औट् तद् + ङे दा + लोट् + सिप् ।
            तद् + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            नवन् + शस् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals("द्वि", result.value)
        val generated = BytecodeCompiler.compileAndLoad(source, "DerivedFeminineReferent")
        val values = generated.getMethod("execute").invoke(null) as Map<*, *>
        assertEquals(result.typedValue, values["LastResult"])
    }
}
