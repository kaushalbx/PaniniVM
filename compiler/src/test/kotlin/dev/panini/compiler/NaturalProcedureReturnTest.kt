package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class NaturalProcedureReturnTest {
    private val one = "फल + सुँ एक + सुँ असँ + लट् + तिप्"
    private val nine = "फल + सुँ शून्य + सुँ असँ + लट् + तिप्"

    @Test fun `qualified historical procedure arguments use the same identity as ordinary operands`() {
        val program = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            फल + सुँ मान + सुँ असँ + लट् + तिप् ॥
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
            द्वि + औट् त्रि + शस् च युज् + लोट् + सिप् ।
            युज् + णिच् + ल्युट् + ङस् फल + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(program).last())
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(program, "QualifiedHistoryReturnArgument")
        assertEquals(result.typedValue, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }
    private fun source(body: String) = """
        परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
        $body
        $nine ॥
        परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
    """.trimIndent()

    @Test fun `natural return exits sentences sequences branches repetition and while`() {
        val bodies = listOf("$one ।", "$one ततः $nine ।",
            "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तर्हि $one अन्यथा $nine ।",
            "द्वि + कृत्वसुच् $one ।",
            "यावत् एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तावत् $one ।",
            "एक + सुँ द्वि + औ च + इति सूची + सुँ असँ + लट् + तिप् । सूची + ङस् सदस्य + अम् प्रति फल + सुँ सदस्य + सुँ असँ + लट् + तिप् ।")
        for ((index, body) in bodies.withIndex()) {
            val program = source(body)
            val interpreted = PaniniVM().evalScript(program)
            assertTrue(interpreted.all { it is ExecutionResult.Success }, "$body: $interpreted")
            val result = assertIs<ExecutionResult.Success>(interpreted.last())
            assertEquals(1L, assertIs<SanskritValue.Sankhya>(result.typedValue).value, body)
            assertNull(result.controlSignal)
            val generated = BytecodeCompiler.compileAndLoad(program, "NaturalReturn$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"], body)
            val budgeted = generated.getMethod("execute", Long::class.javaPrimitiveType).invoke(null, 1L) as Map<*, *>
            assertEquals(result.typedValue, budgeted["LastResult"], body)
        }
    }

    @Test fun `early return preserves collection payloads and resumes its caller`() {
        val program = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ सूची + अम् जन् + णिच् + लट् + तिप् ।
            मान + सुँ सूची + सुँ इति मान + सुँ ।
            फल + सुँ मान + सुँ असँ + लट् + तिप् ।
            नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ॥
            सूची + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val generated = BytecodeCompiler.compileAndLoad(program, "CollectionNaturalReturn")
        for (value in listOf(SanskritValue.Suchi(emptyList()),
            SanskritValue.Suchi(listOf(SanskritValue.Shabda("राम"), SanskritValue.Sankhya(2L, "द्वि"))))) {
            val initial = mapOf("सूची" to value)
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(program,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(value, result.typedValue)
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(value, values["LastResult"])
        }
        val resumed = program + "\nनवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।"
        val initial = mapOf("सूची" to SanskritValue.Suchi(emptyList()))
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(resumed,
            scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
        val caller = BytecodeCompiler.compileAndLoad(resumed, "ResumedNaturalReturn")
        assertEquals(result.typedValue, (caller.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>)["LastResult"])
        assertEquals("नवन्", result.value)
    }

    @Test fun `early return cannot bypass the declared result contract`() {
        val program = source("फल + सुँ राम + सुँ असँ + लट् + तिप् ।")
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(program).last())
        assertFails { BytecodeCompiler.compileAndLoad(program, "InvalidNaturalReturn").getMethod("execute").invoke(null) }
    }

    @Test fun `nested return belongs to the callee and outer execution continues`() {
        val inner = """
            अन्तर + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
            $one ॥
        """.trimIndent()
        val program = inner + "\n" + source(
            "अन्तर + टा डुकृञ् + उ + लोट् + सिप् । द्वि + औट् मान + ङे दा + लोट् + सिप् । फल + सुँ मान + सुँ असँ + लट् + तिप् ।")
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(program).last())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(program, "NestedNaturalReturn")
        assertEquals(result.typedValue, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }

    @Test fun `a callers phala participant cannot shadow the callees result role`() {
        val program = """
            अन्तर + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
            $one ।
            शून्य + अम् शून्य + अम् च युज् + णिच् + लोट् + सिप् ॥
            परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
            फल + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            अन्तर + टा डुकृञ् + उ + लोट् + सिप् ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            एक + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(program)
        val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(program, "CallerPhalaDoesNotShadowResult")
        assertEquals(result.typedValue, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }

    @Test fun `implicit branch results satisfy the same declared procedure contract`() {
        val program = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
            यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तर्हि एक + सुँ अन्यथा द्वि + औ ॥
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(program).last())
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(program, "ImplicitBranchReturnContract")
        assertEquals(result.typedValue, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }

    @Test fun `result statements are independent of nominal word order and include zero`() {
        for ((index, phrase) in listOf(
            "फल + सुँ शून्य + सुँ असँ + लट् + तिप्",
            "शून्य + सुँ फल + सुँ असँ + लट् + तिप्",
            "फल + सुँ असँ + लट् + तिप् शून्य + सुँ").withIndex()) {
            val program = source("$phrase ।").replace("$nine ॥", "$one ॥")
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(program).last())
            assertEquals(0L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val generated = BytecodeCompiler.compileAndLoad(program, "ZeroResultStatement$index")
            assertEquals(result.typedValue, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
        }
    }

    @Test fun `assignment to a participant named phala does not exit the procedure`() {
        val program = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
            फल + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            शून्य + अम् फल + ङे दा + लोट् + सिप् ।
            द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ॥
            एक + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(program).last())
        assertEquals(5L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(program, "PhalaParticipantAssignment")
        assertEquals(result.typedValue, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }

    @Test fun `a comparison about the current result is not a result declaration`() {
        val program = source("""
            एक + अम् शून्य + अम् च युज् + णिच् + लोट् + सिप् ।
            यदि फल + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तर्हि $nine अन्यथा $one ।
        """.trimIndent()).replace("$nine ॥", "$one ॥")
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(program).last())
        assertEquals(0L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(program, "PhalaComparisonNotReturn")
        assertEquals(result.typedValue, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }

    @Test fun `return without a procedure owner fails in both backends`() {
        val program = "$one ।"
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(program).last())
        assertFails { BytecodeCompiler.compileAndLoad(program, "UnownedNaturalReturn").getMethod("execute").invoke(null) }
    }
}
