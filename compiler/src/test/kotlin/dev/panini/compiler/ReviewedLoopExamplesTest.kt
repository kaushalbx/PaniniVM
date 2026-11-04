package dev.panini.compiler

import dev.panini.execution.*
import java.io.File
import kotlin.test.*

class ReviewedLoopExamplesTest {
    @Test fun `sequence example initializes its collection before entering the loop`() {
        val source = File("examples/control_flow/print_1_to_10.pvm").readText().substringBefore("# Step 3")
        val generated = BytecodeCompiler.compileAndLoad(source, "ReviewedLoopInitialization")
        val values = generated.getMethod("execute").invoke(null) as Map<*, *>
        assertIs<SanskritValue.Suchi>(values["क्रम"], values.toString())
    }

    @Test fun `reviewed loop source files preserve their final printed result when compiled`() {
        for ((index, name) in listOf("print_1_to_10", "punah", "foreach", "yada_tada", "local_pronouns").withIndex()) {
            val source = File("examples/control_flow/$name.pvm").readText()
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, "$name: $results")
            val printed = results.filterIsInstance<ExecutionResult.Success>().last { it.outputKind == OutputKind.CONSOLE }.value
            val generated = BytecodeCompiler.compileAndLoad(source, "ReviewedLoop$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(printed, assertIs<SanskritValue>(values["LastResult"]).toDisplayText(), name)
        }
    }
}
