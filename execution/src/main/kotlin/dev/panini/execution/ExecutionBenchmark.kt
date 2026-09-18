package dev.panini.execution

import kotlin.system.measureNanoTime

/** Reproducible execution benchmark; use JMH for publication-grade measurements. */
object ExecutionBenchmark {
    @JvmStatic
    fun main(args: Array<String>) {
        val iterations = args.firstOrNull()?.toIntOrNull()?.also { require(it > 0) } ?: 1_000
        val warmups = args.getOrNull(1)?.toIntOrNull()?.also { require(it >= 0) } ?: 100
        val cases = linkedMapOf(
            "range-choice" to """
                एक + ङसिँ दशन् + शस् परि + अन्त + अम् सङ्ख्या + अम् चिञ् + श्नु + लोट् + सिप् ।
            """.trimIndent(),
            "nested-prakriya" to """
                योग + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
                एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
                आवरण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
                योग + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ॥
                आवरण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
            """.trimIndent(),
            "structured-attribute-pipeline" to """
                पञ्चन् + दशत + अम् मूल्य + अम् सङ्ख्या + मतुप् + सुँ ।
                सङ्ख्या + मतुप् + ङस् मूल्य + अम् ततः मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        println("case,phase,statements,iterations,total_ms,ns_per_operation")
        cases.forEach { (name, source) ->
            val statements = PvmScript.parse(source).size
            repeat(warmups) { PvmScript.parse(source) }
            val parseElapsed = measureNanoTime { repeat(iterations) { PvmScript.parse(source) } }
            printResult(name, "parse", statements, iterations, parseElapsed)

            val vm = PaniniVM()
            repeat(warmups) { vm.evalScript(source) }
            val executionElapsed = measureNanoTime {
                repeat(iterations) { vm.evalScript(source) }
            }
            printResult(name, "interpret", statements, iterations, executionElapsed)
        }
    }

    private fun printResult(
        name: String,
        phase: String,
        statements: Int,
        iterations: Int,
        elapsed: Long,
    ) {
        println(
            "$name,$phase,$statements,$iterations,${"%.3f".format(elapsed / 1_000_000.0)}," +
                "${"%.1f".format(elapsed.toDouble() / iterations)}",
        )
    }
}
