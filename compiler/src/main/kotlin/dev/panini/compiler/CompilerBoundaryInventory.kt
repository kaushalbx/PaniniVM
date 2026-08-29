package dev.panini.compiler

import java.io.File

/** Reports generic action-runtime boundaries for every compilable example module. */
object CompilerBoundaryInventory {
    @JvmStatic
    fun main(args: Array<String>) {
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        val root = File(args.firstOrNull() ?: "examples")
        require(root.isDirectory) { "Compiler inventory directory not found: ${root.absolutePath}" }
        println("module,status,category,runtime_boundaries_or_diagnostic")
        val entries = root.walkTopDown().filter { file ->
            file.isFile && file.extension == "pvm" && !file.nameWithoutExtension.endsWith("_lib") &&
                file.nameWithoutExtension !in setOf("ganita", "samavaya_lib", "morph_lib", "inheritance_lib")
        }.sortedBy { it.relativeTo(root).invariantSeparatorsPath }.toList()
        entries.forEachIndexed { index, entry ->
                val moduleRoot = root.listFiles().orEmpty().filter(File::isDirectory)
                    .first { entry.toPath().startsWith(it.toPath()) }
                val relative = entry.relativeTo(root).invariantSeparatorsPath
                val result = runCatching {
                    val libraries = moduleRoot.walkTopDown().filter { file ->
                        file.isFile && file.extension == "pvm" && file != entry &&
                            entry.nameWithoutExtension.contains("mukhya", ignoreCase = true) &&
                            (file.nameWithoutExtension.endsWith("_lib") || file.nameWithoutExtension == "ganita")
                    }.sortedBy(File::getPath).toList()
                    val descriptor = PaniniModuleDescriptor(
                        entry.nameWithoutExtension,
                        libraries.map { PaniniModuleSource(it.path, it.readText(), false) } +
                            PaniniModuleSource(entry.path, entry.readText(), true),
                    )
                    val program = CompilerFrontend.lowerModule(descriptor, "BoundaryInventory_$index")
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
