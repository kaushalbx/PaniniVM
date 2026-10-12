package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class NaturalMembershipTypeTest {
    @Test fun `derived locative whole keeps its own collection reference identity`() {
        val source = "यदि एक + सुँ सङ्ग्रह + मतुप् + ङि असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "DerivedLocativeWhole")
        val full = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1, "एक")))
        val empty = SanskritValue.Suchi(emptyList())
        for ((whole, base, expected) in listOf(Triple(full, empty, 2L), Triple(empty, full, 1L))) {
            val initial = mapOf("सङ्ग्रह + मतुप्" to whole, "सङ्ग्रह" to base)
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }
    @Test fun `membership grammatical truth literal is not a host reference`() {
        val source = "यदि सत्य + सुँ सूची + ङि असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val initial = mapOf("सत्य" to SanskritValue.Satya(false),
            "सूची" to SanskritValue.Suchi(listOf(SanskritValue.Satya(true))))
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
            scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "LiteralMembershipSubject")
        val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
        assertEquals(result.typedValue, values["LastResult"])
    }

    @Test fun `named membership subject can itself be a collection without flattening`() {
        val source = "यदि वस्तु + सुँ सूची + ङि असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "StructuredMembershipSubject")
        val one = SanskritValue.Sankhya(1L, "एक")
        for (subject in listOf<SanskritValue>(one, SanskritValue.Suchi(listOf(one)))) {
            val initial = mapOf("वस्तु" to subject, "सूची" to SanskritValue.Suchi(listOf(subject)))
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }

    @Test fun `one compiled locative membership validates the current collection whole`() {
        val source = "यदि एक + सुँ सूची + ङि असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "TypedLocativeMembership")
        val one = SanskritValue.Sankhya(1L, "एक")
        for ((whole, expected) in listOf(
            SanskritValue.Suchi(listOf(one)) to 2L,
            SanskritValue.Gana(listOf(one)) to 2L,
            SanskritValue.Suchi(emptyList()) to 1L,
            SanskritValue.Suchi(listOf(SanskritValue.Suchi(listOf(one)))) to 1L,
        )) {
            val initial = mapOf("सूची" to whole)
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
        for (whole in listOf<SanskritValue>(one, SanskritValue.Shabda("सूची"))) {
            val initial = mapOf("सूची" to whole)
            val results = PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial)))
            assertIs<ExecutionResult.Failure>(results.last(), results.toString())
            val failure = assertFailsWith<java.lang.reflect.InvocationTargetException> {
                generated.getMethod("execute", Map::class.java).invoke(null, initial)
            }
            assertIs<CompiledPaniniExecutionException>(failure.cause)
        }
    }
}
