package dev.panini.compiler

import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ModuleCompilerTest {
    @Test
    fun `compiled multifile modules match interpreted project results`() {
        listOf(
        "projects/list_operations/samavaya_mukhya.pvm",
        "projects/multifile/mukhya.pvm",
        "projects/paninian_morphology/morph_mukhya.pvm",
        ).forEachIndexed { index, entryPath ->
            val entry = File(entryPath)
            val libraries = entry.parentFile.walkTopDown().filter { file ->
                file.isFile && file.extension == "pvm" && file != entry
            }.sortedBy(File::getName).toList()
            val descriptor = PaniniModuleDescriptor(
                "parity_$index",
                libraries.map { PaniniModuleSource(it.name, it.readText(), false) } +
                    PaniniModuleSource(entry.name, entry.readText(), true),
            )
            val artifact = BytecodeCompiler.compileModule(descriptor, "CompiledParity$index")
            val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
                .loadFromBytes(artifact.metadata.className, artifact.bytecode)
            @Suppress("UNCHECKED_CAST")
            val compiled = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
            val interpreted = PaniniVM().evalProject(entry).last { it is dev.panini.execution.ExecutionResult.Success }
                as dev.panini.execution.ExecutionResult.Success
            assertEquals(requireNotNull(interpreted.typedValue).toDisplayText(), compiled.getValue("LastResult").toDisplayText(), entryPath)
        }
    }

    @Test
    fun `metadata round trips and dependency procedures execute without source`() {
        val library = PaniniModuleDescriptor(
            "ganita_library",
                listOf(PaniniModuleSource("ganita.pvm", File("projects/multifile/ganita.pvm").readText(), false)),
        )
        val libraryArtifact = BytecodeCompiler.compileModule(library, "CompiledGanitaLibrary")
        val metadata = PaniniModuleMetadataCodec.decode(PaniniModuleMetadataCodec.encode(libraryArtifact.metadata))
        assertEquals(libraryArtifact.metadata, metadata)
        assertTrue(metadata.procedures.isNotEmpty())

        val application = PaniniModuleDescriptor(
            "ganita_application",
                listOf(PaniniModuleSource("mukhya.pvm", File("projects/multifile/mukhya.pvm").readText(), true)),
            dependencies = listOf(metadata),
        )
        val applicationArtifact = BytecodeCompiler.compileModule(application, "CompiledGanitaApplication")
        val loader = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
        loader.loadFromBytes(libraryArtifact.metadata.className, libraryArtifact.bytecode)
        val generated = loader.loadFromBytes(applicationArtifact.metadata.className, applicationArtifact.bytecode)
        @Suppress("UNCHECKED_CAST")
        val result = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(15L, (result.getValue("LastResult") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `module discovery honors entries and emits class plus metadata`() {
        val sourceDir = createTempDirectory("panini-module").toFile()
        File(sourceDir, "panini.module").writeText(
            "name=sample\nsources=library.pvm,main.pvm\nentries=main.pvm\n",
        )
        File(sourceDir, "library.pvm").writeText("# library only")
        File(sourceDir, "main.pvm").writeText("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।")
        val descriptor = PaniniModuleDescriptor.discover(sourceDir)
        assertEquals(listOf(false, true), descriptor.sources.map(PaniniModuleSource::isEntryPoint))
        val outputDir = createTempDirectory("panini-output").toFile()
        val artifact = BytecodeCompiler.compileModuleDirectory(sourceDir, outputDir)
        assertTrue(File(outputDir, "${artifact.metadata.className}.class").isFile)
        assertTrue(File(outputDir, "sample.pvmmeta").isFile)
    }

    @Test
    fun `internal symbols stay private and duplicate declarations fail analysis`() {
        val internalSource = """
            अन्तरङ्ग + सुँ इति रहस्य + ल्युट् + सुँ ।
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val internalArtifact = BytecodeCompiler.compileModule(
            PaniniModuleDescriptor("private", listOf(PaniniModuleSource("private.pvm", internalSource, false))),
            "CompiledPrivateModule",
        )
        assertTrue(internalArtifact.metadata.procedures.isEmpty())

        val declaration = """
            योग + ल्युट् + सुँ ।
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val error = assertFailsWith<IllegalStateException> {
            PaniniModuleAnalyzer.analyze(
                PaniniModuleDescriptor(
                    "duplicate",
                    listOf(
                        PaniniModuleSource("a.pvm", declaration, false),
                        PaniniModuleSource("b.pvm", declaration, false),
                    ),
                ),
            )
        }
        assertTrue(error.message.orEmpty().contains("Duplicate Panini procedure"))
    }

    @Test
    fun `generated execute overload accepts typed initial state`() {
        val source = "फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledInitialState")
        val initial = mapOf("LastResult" to SanskritValue.Sankhya(8, "अष्ट"))
        @Suppress("UNCHECKED_CAST")
        val result = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<String, SanskritValue>
        assertNotNull(result["LastResult"])
        assertEquals("अष्ट", result.getValue("LastResult").toDisplayText())
    }

    @Test
    fun `metadata validation rejects incompatible and ambiguous dependencies`() {
        assertFailsWith<IllegalArgumentException> {
            PaniniModuleMetadata(
                formatVersion = PaniniModuleMetadata.CURRENT_FORMAT_VERSION + 1,
                moduleName = "future",
                className = "FutureProgram",
                procedures = emptyList(),
            )
        }
        val procedure = PaniniExportedProcedure("योग", "samjna_yoga", null, emptyList(), null, null)
        val first = PaniniModuleMetadata(moduleName = "first", className = "First", procedures = listOf(procedure))
        val second = PaniniModuleMetadata(moduleName = "second", className = "Second", procedures = listOf(procedure))
        assertFailsWith<IllegalStateException> {
            PaniniModuleDescriptor(
                "ambiguous",
                listOf(PaniniModuleSource("main.pvm", "# empty")),
                dependencies = listOf(first, second),
            )
        }
    }

    @Test
    fun `initial value kinds participate in IR verification`() {
        val program = CompilerProgram(
            className = "TypedInitialProgram",
            entryPoint = listOf(
                CompilerInstruction.Load("आरम्भ"),
                CompilerInstruction.Constant(SanskritValue.Sankhya(2, "द्वि")),
                CompilerInstruction.Arithmetic(ArithmeticOperator.ADD),
                CompilerInstruction.Store("LastResult"),
            ),
            initialValueKinds = mapOf("आरम्भ" to CompilerValueKind.NUMBER),
        )
        CompilerProgramVerifier.verify(program)
        assertFailsWith<IllegalArgumentException> {
            CompilerProgramVerifier.verify(program.copy(initialValueKinds = mapOf("आरम्भ" to CompilerValueKind.TEXT)))
        }
    }
}
