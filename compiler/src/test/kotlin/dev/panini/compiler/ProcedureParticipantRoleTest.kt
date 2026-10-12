package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class ProcedureParticipantRoleTest {
    @Test fun `extracted text cannot acquire numeric meaning through unknown static kind`() {
        val source = """
            राम + ङस् सूची + सुँ असँ + लट् + तिप् ।
            सूची + ङस् प्रथम + अम् उद् + हृ + लोट् + सिप् ।
            फल + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val generated = BytecodeCompiler.compileAndLoad(source, "InvalidMemberArithmetic")
        val failure = assertFailsWith<java.lang.reflect.InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }
        assertIs<CompiledPaniniExecutionException>(failure.cause)
    }

    @Test fun `extracted member composes with arithmetic using its runtime type`() {
        val source = """
            एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।
            सूची + ङस् प्रथम + अम् उद् + हृ + लोट् + सिप् ।
            फल + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        assertParity(source, "MemberArithmeticComposition", 2L)
    }

    private fun assertParity(source: String, name: String, expected: Long) {
        val results = PaniniVM().evalScript(source)
        val interpreted = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals(expected, assertIs<SanskritValue.Sankhya>(interpreted.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, name)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(interpreted.typedValue, values["LastResult"])
    }

    @Test fun `member ordinal does not invent a procedure parameter`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            सूची + ङस् प्रथम + अम् उद् + हृ + लोट् + सिप् ॥
            एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertTrue(CompilerFrontend.lower(source, "MemberRoleSignature").procedures.single().parameterNames.isEmpty())
        assertParity(source, "MemberRoleCall", 1L)
    }

    @Test fun `ordinal procedure name is not a positional participant`() {
        val source = """
            प्रथम + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + टा डुकृञ् + उ + लोट् + सिप् ॥
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertTrue(CompilerFrontend.lower(source, "CallRoleSignature").procedures.all { it.parameterNames.isEmpty() })
        assertParity(source, "CallRoleCall", 2L)
    }

    @Test fun `bound arguments do not rewrite an ordinal procedure name`() {
        val source = """
            प्रथम + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            प्रथम + टा डुकृञ् + उ + लोट् + सिप् ॥
            पञ्च + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertParity(source, "BoundCallRole", 2L)
    }
}
