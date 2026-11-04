package dev.panini.compiler

import dev.panini.execution.*
import java.io.File
import kotlin.test.*

class ReviewedConditionalExampleTest {
    @Test fun `conditional example branches have interpreter compiler parity`() {
        val source = File("examples/control_flow/conditional.pvm").readText()
        for ((index, program) in listOf(source, source.replace("एक + टा सम", "द्वि + भ्याम् सम")).withIndex()) {
            val interpreted = PaniniVM().evalScript(program)
            assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
            val printed = interpreted.filterIsInstance<ExecutionResult.Success>().single { it.outputKind == OutputKind.CONSOLE }.value
            val generated = BytecodeCompiler.compileAndLoad(program, "ReviewedConditional$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(printed, assertIs<SanskritValue>(values["LastResult"]).toDisplayText())
        }
    }
}
