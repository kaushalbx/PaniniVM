package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class CopularWordOrderTest {
    @Test fun `copular comparison meaning follows cases across all nominal orders`() {
        val orders = listOf(listOf(0, 1, 2), listOf(0, 2, 1), listOf(1, 0, 2),
            listOf(1, 2, 0), listOf(2, 0, 1), listOf(2, 1, 0))
        for ((kind, nominals) in listOf(
            listOf("वाम + सुँ", "दक्षिण + टा", "सम + सुँ"),
            listOf("वाम + सुँ", "दक्षिण + ङसिँ", "न्यून + सुँ"),
        ).withIndex()) for ((index, order) in orders.withIndex()) {
            val condition = order.joinToString(" ") { nominals[it] }
            val source = "यदि $condition असँ + लट् + तिप् तर्हि " +
                "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
                "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
            val generated = BytecodeCompiler.compileAndLoad(source, "CopularOrder${kind}_$index")
            val one = SanskritValue.Sankhya(1, "एक")
            val two = SanskritValue.Sankhya(2, "द्वि")
            for ((left, right) in listOf(one to one, one to two, two to one)) {
                val initial = mapOf("वाम" to left, "दक्षिण" to right)
                val results = PaniniVM().evalScript(source,
                    scope = ExecutionScope(environment = ValueEnvironment(initial)))
                val result = assertIs<ExecutionResult.Success>(results.last(), "$condition: $results")
                val expected = if (if (kind == 0) left.value == right.value else left.value < right.value) 2L else 1L
                assertEquals(expected, assertIs<SanskritValue.Sankhya>(result.typedValue).value, condition)
                val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
                assertEquals(result.typedValue, values["LastResult"], condition)
            }
        }
    }
}
