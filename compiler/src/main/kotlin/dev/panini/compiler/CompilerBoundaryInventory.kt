package dev.panini.compiler

import java.io.File

/** Reports generic action-runtime boundaries for every compilable example program. */
object CompilerBoundaryInventory {
    @JvmStatic
    fun main(args: Array<String>) {
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        val root = File(args.firstOrNull() ?: "examples")
        require(root.isDirectory) { "Compiler inventory directory not found: ${root.absolutePath}" }
        println("program,status,category,runtime_boundaries_or_diagnostic")
        root.walkTopDown()
            .filter { it.isFile && it.extension == "pvm" }
            .sortedBy { it.relativeTo(root).invariantSeparatorsPath }
            .forEachIndexed { index, file ->
                val relative = file.relativeTo(root).invariantSeparatorsPath
                val result = runCatching {
                    val program = CompilerFrontend.lower(file.readText(), "BoundaryInventory_$index")
                    CompilerRuntimeBoundaryReport.operations(program)
                        .entries.joinToString("|") { (operation, count) -> "$operation:$count" }
                        .ifEmpty { "none" }
                }
                if (result.isSuccess) {
                    println("${csv(relative)},compiled,NONE,${csv(result.getOrThrow())}")
                } else {
                    val error = requireNotNull(result.exceptionOrNull())
                    println(
                        "${csv(relative)},unsupported,${CompilerFailureClassifier.classify(error)}," +
                            csv(error.message.orEmpty()),
                    )
                }
            }
    }

    private fun csv(value: String): String = "\"${value.replace("\"", "\"\"")}\""
}
