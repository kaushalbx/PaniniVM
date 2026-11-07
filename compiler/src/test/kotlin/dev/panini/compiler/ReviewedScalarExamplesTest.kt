package dev.panini.compiler

import dev.panini.execution.SanskritValue
import java.io.File
import kotlin.test.*

class ReviewedScalarExamplesTest {
    @Test fun `reviewed scalar PVM files compile and retain their stated results`() {
        for ((index, example) in listOf("examples/arithmetic/scaling.pvm" to "दश",
            "examples/arithmetic/comparison.pvm" to "सत्यम्",
            "examples/arithmetic/min.pvm" to "त्रीणि",
            "examples/arithmetic/mod_demo.pvm" to "एकम्",
            "examples/arithmetic/gcd.pvm" to "षट्",
            "examples/arithmetic/cumulative_sum.pvm" to "पञ्चदश",
            "examples/arithmetic/factorial.pvm" to "विंशत्यधिकशतम्",
            "examples/arithmetic/sum_iterative.pvm" to "पञ्चदश",
            "examples/arithmetic/addition.pvm" to "त्रीणि",
            "examples/arithmetic/addition_phala_demo.pvm" to "त्रीणि",
            "examples/arithmetic/average_demo.pvm" to "विंशतिः",
            "examples/arithmetic/sqrt_demo.pvm" to "त्रीणि",
            "examples/arithmetic/count_demo.pvm" to "त्रीणि",
            "examples/arithmetic/power_demo.pvm" to "एकाशीतिः",
            "examples/arithmetic/geometric_progression.pvm" to "चतुःपञ्चाशत्",
            "examples/algorithms/min_subset_sum_diff.pvm" to "एकम्",
            "examples/algorithms/double_danda_problems.pvm" to "पञ्च",
            "examples/arithmetic/subtraction.pvm" to "सप्त").withIndex()) {
            val generated = BytecodeCompiler.compileAndLoad(File(example.first).readText(), "ReviewedScalarExample$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(example.second, assertIs<SanskritValue>(values["LastResult"]).toDisplayText(), example.first)
        }
    }
}
