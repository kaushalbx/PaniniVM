package dev.panini.execution

import java.io.File

/** Explicit generator for readable Sanskrit companions to segmented `.pvm` sources. */
object PvmReadableSanskrit {
    fun renderFile(
        source: File,
        target: File = source.resolveSibling(source.nameWithoutExtension + ".txt"),
    ): File {
        require(source.isFile && source.extension == "pvm") {
            "Readable Sanskrit generation requires a .pvm source file: ${source.path}"
        }
        val rendered = PvmUktiSadhaka().sadhayaScript(source.readText()).trimEnd()
        target.parentFile?.mkdirs()
        target.writeText("$rendered\n")
        return target
    }

    fun renderPath(path: File): List<File> = when {
        path.isFile -> listOf(renderFile(path))
        path.isDirectory -> path.walkTopDown()
            .filter { it.isFile && it.extension == "pvm" }
            .sortedBy(File::getPath)
            .map(::renderFile)
            .toList()
        else -> error("PVM source path does not exist: ${path.path}")
    }

    private fun File.resolveSibling(name: String): File = File(parentFile, name)
}
