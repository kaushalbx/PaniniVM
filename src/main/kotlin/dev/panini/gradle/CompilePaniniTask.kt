package dev.panini.gradle

import dev.panini.compiler.BytecodeCompiler
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class CompilePaniniTask : DefaultTask() {

    @get:InputDirectory
    abstract val sourceDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun compile() {
        val srcFolder = sourceDir.get().asFile
        val outFolder = outputDir.get().asFile

        if (!srcFolder.exists()) {
            logger.info("PāṇiniVM source folder '${srcFolder.absolutePath}' does not exist, skipping.")
            return
        }

        val pvmFiles = srcFolder.walkTopDown().filter { it.isFile && it.extension == "pvm" }.toList()
        logger.lifecycle("Compiling ${pvmFiles.size} PāṇiniVM script(s) from '${srcFolder.name}' -> '${outFolder.name}'")

        pvmFiles.forEach { file ->
            val className = file.nameWithoutExtension.replaceFirstChar { it.uppercase() } + "Program"
            try {
                BytecodeCompiler.compileFile(file, className, outFolder)
                logger.lifecycle("✓ Compiled ${file.name} to $className.class")
            } catch (e: Exception) {
                logger.error("✗ Failed to compile ${file.name}: ${e.message}")
                throw e
            }
        }
    }
}
