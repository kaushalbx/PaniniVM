package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class DynamicNumericDispatchTest {
    @Test fun `one compiled coordinated call selects meanings from actual named operand types`() {
        val source = "पूर्व + अम् उत्तर + अम् च गण + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "DynamicNamedNumericDispatch")
        val two = SanskritValue.Sankhya(2L, "द्वि")
        val three = SanskritValue.Sankhya(3L, "त्रि")
        for ((index, initial) in listOf(
            mapOf("पूर्व" to two, "उत्तर" to three),
            mapOf("पूर्व" to SanskritValue.Suchi(listOf(two)), "उत्तर" to SanskritValue.Suchi(listOf(three)))
        ).withIndex()) {
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(if (index == 0) 6L else 2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }

    @Test fun `one named object can be a scalar or a collection when counted`() {
        val source = "वस्तु + अम् गण + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "DynamicNamedObjectCounting")
        val two = SanskritValue.Sankhya(2L, "द्वि")
        for ((index, value) in listOf<SanskritValue>(two, SanskritValue.Suchi(listOf(two, two))).withIndex()) {
            val initial = mapOf("वस्तु" to value)
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(if (index == 0) 1L else 2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }
}
