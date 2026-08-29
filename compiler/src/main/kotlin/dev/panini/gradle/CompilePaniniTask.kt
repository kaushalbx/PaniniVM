package dev.panini.gradle

import dev.panini.compiler.BytecodeCompiler
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class CompilePaniniTask : DefaultTask() {

    @get:InputDirectory
    abstract val sourceDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun compile() {
        val srcFolder = sourceDir.orNull?.asFile ?: return
        val outFolder = outputDir.orNull?.asFile ?: return
        compilePaniniFiles(srcFolder, outFolder)
    }

    companion object {
        fun compilePaniniFiles(srcFolder: File, outFolder: File) {
            if (!srcFolder.exists()) return
            val pvmFiles = srcFolder.walkTopDown().filter { it.isFile && it.extension == "pvm" }.toList()
            if (pvmFiles.isEmpty()) return
            val moduleName = srcFolder.name.replace(Regex("[^A-Za-z0-9_]"), "_")
                .replaceFirstChar { it.uppercase() } + "Program"
            BytecodeCompiler.compileModuleFiles(pvmFiles.sortedBy(File::getPath), moduleName, outFolder)
        }
    }
}
