package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class ProcedureArityParityTest {
    private fun definition(explicit: Boolean): String = """
        परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
        ${if (explicit) "प्रथम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\nद्वितीय + सुँ सङ्ख्या + सुँ इति मान + सुँ ।" else ""}
        प्रथम + अम् द्वितीय + अम् च युज् + णिच् + लोट् + सिप् ॥
    """.trimIndent()

    @Test fun `declared and inferred positional calls reject missing and excess inputs`() {
        for (explicit in listOf(false, true)) {
            for ((arguments, count) in listOf("एक + अम्" to 1,
                "एक + अम् द्वि + औट् त्रि + शस् च" to 3)) {
                val source = definition(explicit) + "\n$arguments परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
                val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
                assertTrue(failure.message.contains("2 मानानि अपेक्षितानि, $count प्राप्तानि"), failure.message)
                val compiled = assertFailsWith<IllegalArgumentException> {
                    CompilerFrontend.lower(source, "InvalidArity${explicit}$count")
                }
                assertTrue(compiled.message.orEmpty().contains("2 मानानि अपेक्षितानि"), compiled.message)
            }
        }
    }

    @Test fun `exact positional count still executes in both backends`() {
        for (explicit in listOf(false, true)) {
            val source = definition(explicit) + "\nएक + अम् द्वि + औट् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
            assertEquals(3L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val generated = BytecodeCompiler.compileAndLoad(source, "ExactArity$explicit")
            assertEquals(result.typedValue, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
        }
    }

    @Test fun `one list is one argument irrespective of its member count`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सूची + सुँ इति मान + सुँ ।
            फल + सुँ मान + सुँ असँ + लट् + तिप् ॥
            सूची + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val generated = BytecodeCompiler.compileAndLoad(source, "ListArgumentCount")
        for (size in listOf(0, 2, 5)) {
            val list = SanskritValue.Suchi(List(size) { SanskritValue.Sankhya(it.toLong(), it.toString()) })
            val initial = mapOf("सूची" to list)
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(list, result.typedValue)
            assertEquals(list, (generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>)["LastResult"])
        }
    }
}
