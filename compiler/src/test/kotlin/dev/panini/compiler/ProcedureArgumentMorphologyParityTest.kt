package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class ProcedureArgumentMorphologyParityTest {
    @Test fun `both backends reject plural procedure and scope references rather than execute once`() {
        val calls = listOf("परीक्षण + भ्याम्", "परीक्षण + भिस्",
            "परीक्षण + टा गणित + ओस्", "परीक्षण + टा गणित + आम्")
        for ((index, call) in calls.withIndex()) {
            val source = """
                परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
                मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
                मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
                पञ्च + अम् $call डुकृञ् + उ + लोट् + सिप् ।
            """.trimIndent()
            val results = PaniniVM().evalScript(source)
            assertIs<ExecutionResult.Failure>(results.last(), "$call: $results")
            assertFails(call) { BytecodeCompiler.compileAndLoad(source, "InvalidProcedureNumber$index") }
        }
    }

    @Test fun `unsupported procedure operand cases are not silently dropped by either backend`() {
        for ((index, ending) in listOf("सुँ", "टा", "ङे", "ङसिँ").withIndex()) {
            val source = """
                परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
                मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
                मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
                राम + $ending पञ्च + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
            """.trimIndent()
            val interpreted = PaniniVM().evalScript(source)
            assertIs<ExecutionResult.Failure>(interpreted.last(), "$ending: $interpreted")
            assertFails("$ending must not compile as if राम were absent") {
                BytecodeCompiler.compileAndLoad(source, "InvalidProcedureOperand$index")
            }
        }
    }
}
