package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class NumeralLiteralIdentityTest {
    @Test fun `copular comparisons keep cardinal subject and standard literal despite host names`() {
        for ((index, relation) in listOf("ङसिँ न्यून" to 2L, "टा सम" to 1L).withIndex()) {
            val source = "यदि एक + सुँ द्वि + ${relation.first} + सुँ असँ + लट् + तिप् तर्हि " +
                "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
                "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
            val initial = mapOf("एक" to SanskritValue.Sankhya(99, "host"),
                "द्वि" to SanskritValue.Sankhya(99, "host"))
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(relation.second, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val generated = BytecodeCompiler.compileAndLoad(source, "CopularCardinalLiteral$index")
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }
    @Test fun `nominative numeral in membership retains its literal identity`() {
        val source = "यदि एक + सुँ सूची + ङि असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val initial = mapOf("एक" to SanskritValue.Sankhya(9L, "नव"),
            "सूची" to SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1L, "एक"))))
        val results = PaniniVM().evalScript(source,
            scope = ExecutionScope(environment = ValueEnvironment(initial)))
        val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "NominativeNumeralLiteral")
        val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
        assertEquals(result.typedValue, values["LastResult"])
    }

    @Test fun `host names cannot replace morphological numeral operands`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।"
        val initial = mapOf("एक" to SanskritValue.Sankhya(9L, "नव"),
            "द्वि" to SanskritValue.Suchi(emptyList()))
        val results = PaniniVM().evalScript(source,
            scope = ExecutionScope(environment = ValueEnvironment(initial)))
        val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "NumeralLiteralIdentity")
        val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
        assertEquals(result.typedValue, values["LastResult"])
    }
}
