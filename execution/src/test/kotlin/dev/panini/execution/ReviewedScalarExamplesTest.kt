package dev.panini.execution

import java.io.File
import kotlin.test.*

class ReviewedScalarExamplesTest {
    @Test fun `subset sum examples print their independently stated algorithm results`() {
        for ((name, expected) in mapOf(
            "min_subset_sum_diff" to listOf("एकम्"),
            "double_danda_problems" to listOf("एकम्", "त्रयोविंशतिः", "द्वे", "सप्तविंशतिः", "पञ्च"))) {
            val results = PaniniVM().evalScript(File("examples/algorithms/$name.pvm").readText())
            assertTrue(results.all { it is ExecutionResult.Success }, "$name: $results")
            val successes = results.filterIsInstance<ExecutionResult.Success>()
            assertEquals(expected, successes.filter { it.outputKind == OutputKind.CONSOLE }.map { it.value }, name)
            assertEquals(if (name == "min_subset_sum_diff") 1L else 5L,
                successes.mapNotNull { it.typedValue as? SanskritValue.Sankhya }.last().value, name)
        }
    }

    @Test fun `PVM numerals above nineteen follow their quantity noun heads`() {
        for ((source, expected) in listOf(
            "विंशति + अम्" to "विंशतिं",
            "त्रि + विंशति + अम्" to "त्रयोविंशतिं",
            "पञ्चन् + त्रिंशत् + अम्" to "पञ्चत्रिंशतं",
            "शत + अम्" to "शतं",
            "द्वि + शत + अम्" to "द्विशतं",
            "सहस्र + अम्" to "सहस्रं")) {
            val rendered = PvmUktiSadhaka().sadhayaLine("$source मुद्र् + णिच् + लोट् + सिप् ।")
            assertTrue(rendered.startsWith("$expected "), "$source: $rendered")
        }
    }

    @Test fun `GCD source renders teen cardinal objects without ordinary neuter plural endings`() {
        val source = File("examples/arithmetic/gcd.pvm").readText()
        val rendered = PvmUktiSadhaka().sadhayaScript(source)
        assertTrue("अष्टादश द्वादश च शेषय ।" in rendered, rendered)
        assertFalse("अष्टादशानि" in rendered || "द्वादशानि" in rendered, rendered)
    }

    @Test fun `reviewed stateful arithmetic sources preserve their final numerical results`() {
        for ((name, expected) in listOf("count_demo" to 3L, "power_demo" to 81L,
            "geometric_progression" to 54L, "factorial" to 120L, "sum_iterative" to 15L)) {
            val vm = PaniniVM()
            val results = vm.evalScript(File("examples/arithmetic/$name.pvm").readText(), sessionKey = name)
            assertTrue(results.all { it is ExecutionResult.Success }, "$name: $results")
            val successes = results.filterIsInstance<ExecutionResult.Success>()
            val printed = successes.single { it.outputKind == OutputKind.CONSOLE }
            val outputState = mapOf("power_demo" to "घात", "geometric_progression" to "पदसङ्ख्या",
                "factorial" to "सङ्ख्या", "sum_iterative" to "योग")[name]
            val computed = if (outputState == null)
                successes.mapNotNull { it.typedValue as? SanskritValue.Sankhya }.last()
            else assertIs<SanskritValue.Sankhya>(vm.runtimeValue(name, outputState), name)
            assertEquals(expected, computed.value, name)
            assertEquals(computed.toDisplayText(), printed.value, name)
        }
    }

    @Test fun `reviewed scalar examples execute their stated values and print the results`() {
        for ((path, expected) in listOf("examples/arithmetic/scaling.pvm" to 10L,
            "examples/arithmetic/comparison.pvm" to true,
            "examples/arithmetic/min.pvm" to 3L,
            "examples/arithmetic/mod_demo.pvm" to 1L,
            "examples/arithmetic/gcd.pvm" to 6L,
            "examples/arithmetic/cumulative_sum.pvm" to 15L,
            "examples/arithmetic/addition.pvm" to 3L,
            "examples/arithmetic/addition_phala_demo.pvm" to 3L,
            "examples/arithmetic/average_demo.pvm" to 20L,
            "examples/arithmetic/sqrt_demo.pvm" to 3L,
            "examples/arithmetic/subtraction.pvm" to 7L)) {
            val results = PaniniVM().evalScript(File(path).readText())
            assertTrue(results.all { it is ExecutionResult.Success }, "$path: $results")
            val successes = results.filterIsInstance<ExecutionResult.Success>()
            val computed = successes.first { it.outputKind != OutputKind.CONSOLE }
            when (expected) {
                is Long -> assertEquals(expected, assertIs<SanskritValue.Sankhya>(computed.typedValue).value, path)
                is Boolean -> assertEquals(expected, assertIs<SanskritValue.Satya>(computed.typedValue).boolean, path)
            }
            assertEquals(listOf(requireNotNull(computed.typedValue).toDisplayText()),
                successes.filter { it.outputKind == OutputKind.CONSOLE }.map { it.value }, path)
        }
    }
}
