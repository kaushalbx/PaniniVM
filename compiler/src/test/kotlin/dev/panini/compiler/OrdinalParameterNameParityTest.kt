package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class OrdinalParameterNameParityTest {
    @Test
    fun `division resolves one ordinary operation in both backends`() {
        val source = "दशन् + शस् द्वि + औट् च वि + भज् + णिच् + लोट् + सिप् ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        assertEquals(5L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "OrdinaryDivisionParity")
        assertEquals(result.typedValue, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }

    @Test
    fun `declared ordinal names take precedence over their lexical positions`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            द्वितीय + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            प्रथम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            प्रथम + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            द्वि + औट् दशन् + शस् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals(11L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "OrdinalParameterNameParity")
        assertEquals(result.typedValue, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }
}
