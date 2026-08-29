package dev.panini.compiler

import java.io.File

/** Launches isolated JVM forks of [CompilerBenchmark] for stable macro measurements. */
object CompilerMacrobenchmark {
    @JvmStatic
    fun main(args: Array<String>) {
        val forks = args.firstOrNull()?.toIntOrNull()?.also { require(it > 0) } ?: 5
        val iterations = args.getOrNull(1)?.toIntOrNull()?.also { require(it > 0) } ?: 1_000
        val warmups = args.getOrNull(2)?.toIntOrNull()?.also { require(it >= 0) } ?: 100
        val java = File(System.getProperty("java.home"), "bin/java").absolutePath
        val classpath = System.getProperty("java.class.path")
        repeat(forks) { fork ->
            val process = ProcessBuilder(
                java,
                "-XX:+UseG1GC",
                "-cp",
                classpath,
                CompilerBenchmark::class.java.name,
                iterations.toString(),
                warmups.toString(),
                (fork + 1).toString(),
            ).inheritIO().start()
            check(process.waitFor() == 0) { "Compiler benchmark fork ${fork + 1} failed." }
        }
    }
}
