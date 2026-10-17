package dev.panini.compiler

import dev.panini.execution.*
import java.lang.reflect.InvocationTargetException
import kotlin.test.*

class ProcedureProductHeaderTest {
    private val header = "परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।"
    private val call = "परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"

    @Test fun `first-stage procedure call supplies typed result to following display`() {
        for ((index, objectText) in listOf("", "नव + अम् ").withIndex()) {
            val source = "$header\nएक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥\n" +
                "परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ततः ${objectText}मुद्र् + णिच् + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            val display = assertIs<ExecutionResult.Success>(results.last())
            assertEquals(if (objectText.isEmpty()) "द्वे" else "नव", display.value)
            val generated = BytecodeCompiler.compileAndLoad(source, "FirstProcedureStage$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(display.value, assertIs<SanskritValue>(values["LastResult"]).toDisplayText())
        }
    }

    @Test fun `first sentence declares numeric result in both backends`() {
        for ((index, root) in listOf("जन्", "जनीँ").withIndex()) {
            val source = header.replace("जन्", root) + "\n" +
                "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥\n" + call
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
            assertEquals(2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val ir = CompilerFrontend.lower(source, "ProductHeader$index")
            assertEquals(CompilerValueKind.NUMBER, ir.procedures.single().returnKind)
            val generated = BytecodeCompiler.compileAndLoad(source, "ProductHeader$index")
            assertEquals(result.typedValue,
                (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
        }
    }

    @Test fun `result production respects explicit causative vikarana`() {
        val predicate = "जन् + णिच् + लट् + तिप्"
        for ((index, affix) in listOf("शप्", "श्यन्", "श्ना", "यक्", "स्य").withIndex()) {
            val source = header.replace(predicate, "जन् + णिच् + $affix + लट् + तिप्") +
                "\nएक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥\n$call"
            if (affix == "शप्") {
                val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
                val generated = BytecodeCompiler.compileAndLoad(source, "ExplicitProductVikarana$index")
                assertEquals(result.typedValue,
                    (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
            } else {
                assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last(), affix)
                assertFailsWith<IllegalArgumentException>(affix) {
                    CompilerFrontend.lower(source, "InvalidProductVikarana$index")
                }
            }
        }
    }

    @Test fun `header contract rejects wrong produced type`() {
        val source = "$header\nराम + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n$call"
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val generated = BytecodeCompiler.compileAndLoad(source, "InvalidProductHeader")
        assertIs<CompiledPaniniExecutionException>(assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }.cause)
    }

    @Test fun `failed first procedure stage does not execute following display`() {
        val source = "$header\nराम + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
            "परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ततः नव + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val published = mutableListOf<ExecutionResult>()
        val results = PaniniVM().evalScript(source, onResult = published::add)
        assertIs<ExecutionResult.Failure>(results.last())
        assertTrue((results + published).filterIsInstance<ExecutionResult.Success>().none { it.value == "नव" })
        val generated = BytecodeCompiler.compileAndLoad(source, "FailedFirstProcedureStage")
        assertIs<CompiledPaniniExecutionException>(assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }.cause)
    }

    @Test fun `header cannot duplicate body result or use unrelated predicate`() {
        for (source in listOf(
            "$header\nसङ्ख्या + सुँ इति परिणाम + सुँ ।\nएक + अम् मुद्र् + णिच् + लोट् + सिप् ॥",
            header.replace("जन्", "मुद्र्") + "\nएक + अम् मुद्र् + णिच् + लोट् + सिप् ॥",
        )) assertFailsWith<IllegalArgumentException> { CompilerFrontend.lower(source, "InvalidProductDeclaration") }
    }

    @Test fun `result production requires causative jan rather than root spelling alone`() {
        for ((index, predicate) in listOf(
            "जन् + लट् + तिप्",
            "प्र + जन् + णिच् + लट् + तिप्",
            "मुद्र् + णिच् + लट् + तिप्",
        ).withIndex()) {
            val source = header.replace("जन् + णिच् + लट् + तिप्", predicate) +
                "\nएक + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n$call"
            if (predicate.startsWith("प्र +")) {
                // A prefixed declaration predicate is rejected during parsing,
                // before a procedure signature exists to validate at invocation.
                assertFailsWith<IllegalArgumentException>(predicate) { PaniniVM().evalScript(source) }
            } else assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last(), predicate)
            assertFailsWith<IllegalArgumentException>(predicate) {
                CompilerFrontend.lower(source, "InvalidProductionMorphology$index")
            }
        }
    }
}
