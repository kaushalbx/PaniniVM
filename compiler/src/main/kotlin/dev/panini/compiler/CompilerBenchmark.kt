package dev.panini.compiler

import dev.panini.execution.PaniniVM
import java.lang.reflect.Method
import kotlin.system.measureNanoTime

/** Small reproducible compiler benchmark; use JMH for publication-grade measurements. */
object CompilerBenchmark {
    @JvmStatic
    fun main(args: Array<String>) {
        val iterations = args.firstOrNull()?.toIntOrNull()?.also { require(it > 0) } ?: 1_000
        val warmups = args.getOrNull(1)?.toIntOrNull()?.also { require(it >= 0) } ?: 100
        val cases = linkedMapOf(
            "primitive-arithmetic" to """
                परिचय + ल्युट् + सुँ ।
                एक + अम् मुद्र् + लोट् + सिप् ॥
                एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।
            """.trimIndent(),
            "fixed-repetition" to """
                परिचय + ल्युट् + सुँ ।
                एक + अम् मुद्र् + लोट् + सिप् ॥
                दश + कृत्वः एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।
            """.trimIndent(),
        )

        println("case,engine,iterations,total_ms,ns_per_operation")
        cases.forEach { (name, source) ->
            benchmarkInterpreter(name, source, iterations, warmups)
            benchmarkCompiler(name, source, iterations, warmups)
        }
    }

    private fun benchmarkInterpreter(name: String, source: String, iterations: Int, warmups: Int) {
        val vm = PaniniVM()
        repeat(warmups) { vm.evalScript(source) }
        val elapsed = measureNanoTime { repeat(iterations) { vm.evalScript(source) } }
        printResult(name, "interpreter", iterations, elapsed)
    }

    private fun benchmarkCompiler(name: String, source: String, iterations: Int, warmups: Int) {
        val className = "PaniniBenchmark_${name.replace('-', '_')}"
        lateinit var execute: Method
        val compileElapsed = measureNanoTime {
            execute = BytecodeCompiler.compileAndLoad(source, className).getMethod("execute")
        }
        printResult(name, "compile", 1, compileElapsed)
        repeat(warmups) { execute.invoke(null) }
        val elapsed = measureNanoTime { repeat(iterations) { execute.invoke(null) } }
        printResult(name, "compiled", iterations, elapsed)
    }

    private fun printResult(name: String, engine: String, iterations: Int, elapsed: Long) {
        val totalMillis = elapsed / 1_000_000.0
        val nanosPerOperation = elapsed.toDouble() / iterations
        println("$name,$engine,$iterations,${"%.3f".format(totalMillis)},${"%.1f".format(nanosPerOperation)}")
    }
}
