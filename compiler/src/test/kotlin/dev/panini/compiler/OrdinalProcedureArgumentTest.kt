package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class OrdinalProcedureArgumentTest {
    @Test fun `truth procedure arguments remain literals despite host bindings`() {
        for ((index, word) in listOf("सत्य", "असत्य").withIndex()) {
            val source = "परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "प्रथम + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
                "$word + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
            val initial = mapOf(word to SanskritValue.Satya(word != "सत्य"))
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(if (word == "सत्य") "सत्यम्" else "असत्यम्", result.value)
            val generated = BytecodeCompiler.compileAndLoad(source, "TruthProcedureLiteral$index")
            assertEquals(result.typedValue,
                (generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>)["LastResult"])
        }
    }
    @Test fun `cardinal procedure argument is not shadowed by a same spelled host name`() {
        val source = "परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।\n" +
            "मूल्य + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
            "मूल्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥\n" +
            "एक + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
        val initial = mapOf("एक" to SanskritValue.Sankhya(99, "host"))
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
            scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "CardinalProcedureLiteral")
        assertEquals(result.typedValue,
            (generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>)["LastResult"])
    }
    @Test fun `unbound ordinal cannot satisfy cardinal parameter but named cardinal can`() {
        val source = "परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।\n" +
            "मूल्य + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
            "मूल्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥\n" +
            "प्रथम + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "OrdinalArgumentFallback")
        for ((initial, expected) in listOf(emptyMap<String, SanskritValue>() to 2L,
            mapOf("प्रथम" to SanskritValue.Sankhya(7, "host")) to 8L)) {
            if (initial.isEmpty()) {
                assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
                val failure = assertFailsWith<java.lang.reflect.InvocationTargetException> {
                    generated.getMethod("execute", Map::class.java).invoke(null, initial)
                }
                assertIs<CompiledPaniniExecutionException>(failure.cause)
                continue
            }
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }
    @Test fun `feminine ordinal is distinct from cardinal but its named referent remains live`() {
        val source = "परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।\n" +
            "मूल्य + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
            "मूल्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥\n" +
            "प्रथम + टाप् + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "FeminineOrdinalArgumentFallback")
        for ((initial, expected) in listOf(emptyMap<String, SanskritValue>() to 2L,
            mapOf("प्रथम + टाप्" to SanskritValue.Sankhya(7, "host")) to 8L)) {
            if (initial.isEmpty()) {
                assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
                val failure = assertFailsWith<java.lang.reflect.InvocationTargetException> {
                    generated.getMethod("execute", Map::class.java).invoke(null, initial)
                }
                assertIs<CompiledPaniniExecutionException>(failure.cause)
                continue
            }
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            assertEquals(result.typedValue,
                (generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>)["LastResult"])
        }
    }
    @Test fun `ordinal named collection satisfies an explicitly declared collection parameter`() {
        val source = "परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।\n" +
            "सङ्ग्रह + सुँ सूची + सुँ इति मान + सुँ ।\n" +
            "सङ्ग्रह + अम् गण + णिच् + लोट् + सिप् ॥\n" +
            "प्रथम + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
        val initial = mapOf("प्रथम" to SanskritValue.Suchi(List(3) { SanskritValue.Sankhya(1, "एक") }))
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
            scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "OrdinalCollectionArgument")
        val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
        assertEquals(result.typedValue, values["LastResult"])
    }
}
