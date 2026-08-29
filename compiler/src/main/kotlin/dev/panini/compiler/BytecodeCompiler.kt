package dev.panini.compiler

import java.io.File

object BytecodeCompiler {

    data class ModuleSource(val name: String, val content: String)
    data class CompiledModuleArtifact(val bytecode: ByteArray, val metadata: PaniniModuleMetadata)

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
        val descriptor = PaniniModuleDescriptor(
            className,
            sources.map { PaniniModuleSource(it.name, it.content) },
        )
        return compileModule(descriptor, className).bytecode
    }

    fun compileModule(
        descriptor: PaniniModuleDescriptor,
        className: String = descriptor.name.toJvmClassName(),
    ): CompiledModuleArtifact {
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        val analyzed = PaniniModuleAnalyzer.analyze(descriptor)
        val bytecode = GeneratedBytecodeVerifier.verify(
            CompilerProgramJvmEmitter.emit(CompilerFrontend.lowerModule(descriptor, className)),
        )
        val metadata = PaniniModuleMetadata(
            moduleName = descriptor.name,
            className = className,
            procedures = analyzed.procedures.filter { it.visibility == PaniniSymbolVisibility.PUBLIC }.map {
                PaniniExportedProcedure(
                    symbol = it.symbol,
                    methodName = it.methodName,
                    domain = it.domain,
                    parameters = it.signature.parameters,
                    resultType = it.signature.resultType,
                    resultSchema = it.signature.resultSchema,
                )
            },
            inheritance = analyzed.inheritance,
            schemas = analyzed.schemas,
        )
        return CompiledModuleArtifact(bytecode, metadata)
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

    fun compileModuleDirectory(sourceDir: File, outputDir: File): CompiledModuleArtifact {
        val descriptorFile = File(sourceDir, "panini.module")
        val dependencyPaths = descriptorFile.takeIf(File::isFile)?.readLines()
            ?.firstOrNull { it.substringBefore('=').trim() == "dependencies" }
            ?.substringAfter('=')?.split(',')?.map(String::trim)?.filter(String::isNotEmpty).orEmpty()
        val dependencies = dependencyPaths.map { path ->
            val file = File(sourceDir, path)
            require(file.isFile) { "Panini dependency metadata not found: ${file.absolutePath}" }
            PaniniModuleMetadataCodec.read(file)
        }
        val descriptor = PaniniModuleDescriptor.discover(sourceDir, dependencies)
        val artifact = compileModule(descriptor)
        outputDir.mkdirs()
        File(outputDir, "${artifact.metadata.className}.class").writeBytes(artifact.bytecode)
        PaniniModuleMetadataCodec.write(artifact.metadata, File(outputDir, "${descriptor.name}.pvmmeta"))
        return artifact
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

private fun String.toJvmClassName(): String = replace(Regex("[^A-Za-z0-9_]"), "_")
    .replaceFirstChar { if (it.isLowerCase()) it.uppercase() else it.toString() } + "Program"
