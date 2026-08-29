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
        val fork = args.getOrNull(2)?.toIntOrNull() ?: 1
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
            "collection" to """
                परिचय + ल्युट् + सुँ ।
                एक + अम् मुद्र् + लोट् + सिप् ॥
                एक + अम् द्वि + अम् त्रि + अम् च सूची + ङे दा + लोट् + सिप् ।
                सूची + अम् गण् + लोट् + सिप् ।
            """.trimIndent(),
            "conditional" to """
                परिचय + ल्युट् + सुँ ।
                एक + अम् मुद्र् + लोट् + सिप् ॥
                यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् तर्हि त्रि + अम् मुद्र् + लोट् + सिप् अन्यथा चतुर् + अम् मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
            "recursive-procedure" to """
                हृ + ल्युट् + सुँ ।
                अवस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अवस्था + ङे ।
                गण् + ल्युट् + टा कृ + लोट् + सिप् ॥
                गण् + ल्युट् + सुँ ।
                यदि अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तर्हि हृ + ल्युट् + टा कृ + लोट् + सिप् अन्यथा वि + स्था + लोट् + सिप् ॥
                त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
                गण् + ल्युट् + टा कृ + लोट् + सिप् ।
            """.trimIndent(),
        )

        println("fork,case,phase,iterations,total_ms,ns_per_operation,runtime_boundaries")
        cases.forEach { (name, source) ->
            benchmarkInterpreter(fork, name, source, iterations, warmups)
            benchmarkCompiler(fork, name, source, iterations, warmups)
        }
    }

    private fun benchmarkInterpreter(fork: Int, name: String, source: String, iterations: Int, warmups: Int) {
        val vm = PaniniVM()
        repeat(warmups) { vm.evalScript(source) }
        val elapsed = measureNanoTime { repeat(iterations) { vm.evalScript(source) } }
        printResult(fork, name, "interpreter", iterations, elapsed)
    }

    private fun benchmarkCompiler(fork: Int, name: String, source: String, iterations: Int, warmups: Int) {
        val className = "PaniniBenchmark_${name.replace('-', '_')}"
        lateinit var program: CompilerProgram
        val lowerElapsed = measureNanoTime {
            program = CompilerFrontend.lower(source, className)
        }
        val boundary = CompilerRuntimeBoundaryReport.operations(program)
            .entries.joinToString("|") { (operation, count) -> "$operation:$count" }
            .ifEmpty { "none" }
        printResult(fork, name, "lower", 1, lowerElapsed, boundary)
        lateinit var execute: Method
        val compileElapsed = measureNanoTime {
            val bytes = CompilerProgramJvmEmitter.emit(program)
            execute = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
                .loadFromBytes(className, bytes)
                .getMethod("execute")
        }
        printResult(fork, name, "emit-load", 1, compileElapsed)
        repeat(warmups) { execute.invoke(null) }
        val elapsed = measureNanoTime { repeat(iterations) { execute.invoke(null) } }
        printResult(fork, name, "compiled", iterations, elapsed)
    }

    private fun printResult(
        fork: Int,
        name: String,
        phase: String,
        iterations: Int,
        elapsed: Long,
        runtimeBoundaries: String = "",
    ) {
        val totalMillis = elapsed / 1_000_000.0
        val nanosPerOperation = elapsed.toDouble() / iterations
        println(
            "$fork,$name,$phase,$iterations,${"%.3f".format(totalMillis)}," +
                "${"%.1f".format(nanosPerOperation)},$runtimeBoundaries",
        )
    }
}
