package dev.panini.execution

import dev.panini.dhatupatha.DhatuPathaRegistration
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ExecutionArchitectureTest {
    @Test
    fun `operation catalog resolves exact upadesha only`() {
        DhatuPathaRegistration.ensureRegistered()
        val catalog = DhatuPathaRegistration.operationCatalog

        assertTrue(DhatuPathaRegistration.resolve("यु").size > 1)
        assertEquals(
            DhatuPathaRegistration.resolve("यु").flatMap { it.operations }.distinct(),
            DhatuPathaRegistration.resolveOperations("यु"),
        )
        assertTrue(catalog.resolveAll("यु").size > 1)
        assertEquals(null, catalog.resolve("यु"), "Ambiguous root lookup must not choose an arbitrary overload")
        assertTrue(catalog.resolveAll("युत").isEmpty(), "Surface aliases must not resolve")
        assertTrue(catalog.resolveAll("सङ्ख्यायोजनम्").isEmpty(), "Action names must not resolve as dhātus")
    }

    @Test
    fun `default VM scope does not preauthorize dangerous effects`() {
        val capabilities = PaniniVM().defaultScope.capabilities

        assertFalse(ExecutionEffect.NETWORK in capabilities)
        assertFalse(ExecutionEffect.EXECUTE_PROCESS in capabilities)
        assertFalse(ExecutionEffect.SEND_MESSAGE in capabilities)
        assertTrue(ExecutionEffect.PURE in capabilities)
    }

    @Test
    fun `every pvm example has a reproducible readable Sanskrit rendering`() {
        val root = File("examples")
        val sources = root.walkTopDown().filter { it.isFile && it.extension == "pvm" }.toList()
        assertTrue(sources.isNotEmpty())

        sources.forEach { source ->
            assertTrue(PvmScript.parse(source.readText()).isNotEmpty(), "No statements parsed from ${source.path}")
            val generated = File(source.parentFile, "${source.nameWithoutExtension}.txt")
            assertTrue(generated.isFile, "Missing generated artifact for ${source.path}")
            val rendered = PvmUktiSadhaka().sadhayaScript(source.readText()).trimEnd()
            assertEquals(
                generated.readText().trimEnd(),
                rendered,
                "Generated artifact is stale for ${source.path}",
            )
        }
    }

    @Test
    fun `every pvm example executes without an invalid or failed statement`() {
        val vm = PaniniVM()
        val sources = File("examples").walkTopDown()
            .filter { it.isFile && it.extension == "pvm" }
            .sortedBy(File::getPath)
            .toList()

        sources.forEach { source ->
            val results = vm.evalFile(source, sessionKey = "example:${source.path}")
            val invalid = results.filter {
                it is ExecutionResult.Failure || it is ExecutionResult.Ambiguous || it is ExecutionResult.NeedsInput
            }
            assertTrue(invalid.isEmpty(), "${source.path} produced invalid results: $invalid")
        }
    }

    @Test
    fun `corrected algorithm examples produce their documented values`() {
        val expected = linkedMapOf(
            "examples/algorithms/circle_area.pvm" to "सप्तविंशतिः",
            "examples/algorithms/fibonacci_array.pvm" to "एकविंशतिः",
            "examples/algorithms/fibonacci_loop.pvm" to "त्रयोदश",
            "examples/algorithms/fibonacci_sequence.pvm" to "अष्ट",
            "examples/algorithms/pythagorean_triplet.pvm" to "पञ्च",
            "examples/algorithms/vector_dot_product.pvm" to "त्रयोविंशतिः",
            "examples/arithmetic/cumulative_sum.pvm" to "पञ्चदश",
            "examples/arithmetic/gcd.pvm" to "षट्",
        )
        val vm = PaniniVM()

        expected.forEach { (path, value) ->
            val results = vm.evalFile(File(path), sessionKey = "golden:$path")
            val final = assertIs<ExecutionResult.Success>(results.last())
            assertEquals(value, final.value, path)
        }
    }
}
