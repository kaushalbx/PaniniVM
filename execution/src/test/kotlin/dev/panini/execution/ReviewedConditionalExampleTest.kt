package dev.panini.execution

import java.io.File
import kotlin.test.*

class ReviewedConditionalExampleTest {
    @Test fun `conditional example selects and prints either branch result`() {
        val source = File("examples/control_flow/conditional.pvm").readText()
        for ((program, expected) in listOf(source to 7L,
            source.replace("एक + टा सम + सुँ", "द्वि + भ्याम् सम + सुँ") to 0L)) {
            val results = PaniniVM().evalScript(program)
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            val successes = results.filterIsInstance<ExecutionResult.Success>()
            val result = successes.mapNotNull { it.typedValue as? SanskritValue.Sankhya }.last()
            assertEquals(expected, result.value)
            assertEquals(listOf(result.toDisplayText()), successes.filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
        }
    }
}
