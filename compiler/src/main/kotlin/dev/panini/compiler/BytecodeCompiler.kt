package dev.panini.compiler

import java.io.File

object BytecodeCompiler {

    data class ModuleSource(val name: String, val content: String)

    fun compileFile(file: File, className: String): ByteArray {
        require(file.exists()) { "PaniniVM script file not found: ${file.absolutePath}" }
        val scriptContent = file.readText()
        return compile(scriptContent, className)
    }

    /**
     * Compiles a PaniniVM script content into JVM bytecode.
     */
    fun compile(scriptContent: String, className: String): ByteArray {
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        return GeneratedBytecodeVerifier.verify(
            CompilerFrontend.compile(scriptContent, className),
        )
    }

    /** Compiles source files as one module with module-wide declaration and symbol analysis. */
    fun compileModule(sources: List<ModuleSource>, className: String): ByteArray {
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        val program = CompilerFrontend.lowerModule(
            sources.map { CompilerFrontend.SourceUnit(it.name, it.content) },
            className,
        )
        return GeneratedBytecodeVerifier.verify(CompilerProgramJvmEmitter.emit(program))
    }

    fun compileModuleFiles(files: List<File>, className: String): ByteArray {
        files.forEach { require(it.exists()) { "PaniniVM script file not found: ${it.absolutePath}" } }
        return compileModule(files.map { ModuleSource(it.path, it.readText()) }, className)
    }

    fun compileModuleFiles(files: List<File>, className: String, outputDir: File) {
        val bytecode = compileModuleFiles(files, className)
        outputDir.mkdirs()
        File(outputDir, "$className.class").writeBytes(bytecode)
    }

    class PaniniClassLoader(parent: ClassLoader) : ClassLoader(parent) {
        fun loadFromBytes(name: String, bytes: ByteArray): Class<*> =
            defineClass(name, bytes, 0, bytes.size)
    }

    fun compileAndLoad(scriptContent: String, className: String, parentLoader: ClassLoader = this.javaClass.classLoader): Class<*> {
        val bytecode = compile(scriptContent, className)
        val loader = PaniniClassLoader(parentLoader)
        return loader.loadFromBytes(className, bytecode)
    }

    /**
     * Compiles a .pvm file and writes the generated .class file to the specified output folder.
     */
    fun compileFile(file: File, className: String, outputDir: File) {
        require(file.exists()) { "Script file not found: ${file.absolutePath}" }
        val bytecode = compile(file.readText(), className)
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val classFile = File(outputDir, "$className.class")
        classFile.writeBytes(bytecode)
        println("Successfully compiled '${file.name}' to ${classFile.absolutePath}")
    }
}
