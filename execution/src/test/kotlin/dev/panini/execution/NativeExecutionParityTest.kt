package dev.panini.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NativeExecutionParityTest {
    @Test
    fun `native documents execute deterministic arithmetic collections and control flow identically`() {
        val paths = listOf(
            "examples/arithmetic/addition.pvm",
            "examples/arithmetic/sum_recursive.pvm",
            "examples/algorithms/double_danda_problems.pvm",
            "examples/control_flow/two_counter_machine.pvm",
            "examples/collections/list_demo.pvm",
            "examples/collections/ordinal_index.pvm",
            "examples/arithmetic/factorial.pvm",
            "examples/algorithms/fibonacci.pvm",
            "projects/taddhita_inheritance/nested_genitive_struct.pvm",
            "projects/taddhita_inheritance/purvapara_pipeline.pvm",
            "projects/taddhita_inheritance/apavada_override.pvm",
        )
        paths.forEach { path ->
            val source = File(path).readText()
            fun execute(native: Boolean): List<ExecutionResult> {
                val vm = PaniniVM()
                return PvmScriptExecutor(vm).evalScript(
                    source, scope = vm.defaultScope, speaker = "प्रयोक्ता", listener = "यन्त्रम्",
                    persistSession = false,
                    parsedStatements = if (native) PvmScript.parseNative(source) else PvmScript.parseLegacy(source),
                )
            }
            val legacy = execute(false)
            val native = execute(true)
            assertTrue(legacy.isNotEmpty(), path)
            assertTrue(legacy.all { it is ExecutionResult.Success }, "$path: $legacy")
            // Shared linguistic caches can omit already established derivation traces.
            // Compare every observable success field while excluding that diagnostic history.
            fun observable(results: List<ExecutionResult>) = results.map {
                if (it is ExecutionResult.Success) it.copy(trace = emptyList()) else it
            }
            assertEquals(observable(legacy), observable(native), path)
        }
    }
}
