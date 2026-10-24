package dev.panini.compiler

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import dev.panini.execution.SanskritValue

class CompilerCoverageTest {
    @Test
    fun `compiled procedure choices observe sequential range declarations`() {
        val source = """
            चयन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            सङ्ख्या + अम् चिञ् + श्नु + लोट् + सिप् ॥
            एक + ङसिँ एक + अम् परि + अन्त + अम् इति सीमा + सुँ ।
            चयन + टा डुकृञ् + उ + लोट् + सिप् ततः पूर्व + ङे दा + लोट् + सिप् ।
            द्वि + ङसिँ द्वि + औट् परि + अन्त + अम् इति सीमा + सुँ ।
            चयन + टा डुकृञ् + उ + लोट् + सिप् ततः उत्तर + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val generated = BytecodeCompiler.compileAndLoad(source, "SequentialRangeChoice")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(1L, (values.getValue("पूर्व") as SanskritValue.Sankhya).value)
        assertEquals(2L, (values.getValue("उत्तर") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `native compiled control flow executes with the same result as the default backend`() {
        val source = File("examples/control_flow/two_counter_machine.pvm").readText()
        val name = "NativeCounterParity"
        val descriptor = PaniniModuleDescriptor(name, listOf(PaniniModuleSource("counter.pvm", source)))
        val program = CompilerFrontend.lowerModule(descriptor, name, dev.panini.execution.PvmScript::parseNative)
        val bytes = GeneratedBytecodeVerifier.verify(CompilerProgramJvmEmitter.emit(program))
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader).loadFromBytes(name, bytes)
        @Suppress("UNCHECKED_CAST")
        val native = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        val legacyClass = BytecodeCompiler.compileAndLoad(source, "LegacyCounterParity")
        @Suppress("UNCHECKED_CAST")
        val legacy = legacyClass.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(legacy, native)
        assertEquals("त्रीणि", native.getValue("LastResult").toDisplayText())
    }

    @Test
    fun `remaining examples compile with module semantics and explicit absent-field IR`() {
        val modules = listOf(
            listOf("projects/list_operations/samavaya_lib.pvm", "projects/list_operations/samavaya_mukhya.pvm"),
            listOf("projects/multifile/ganita.pvm", "projects/multifile/mukhya.pvm"),
            listOf("projects/paninian_morphology/morph_lib.pvm", "projects/paninian_morphology/morph_mukhya.pvm"),
        )
        modules.forEachIndexed { index, paths ->
            val units = paths.map { path -> CompilerFrontend.SourceUnit(path, File(path).readText()) }
            CompilerFrontend.lowerModule(units, "ModuleCoverage_$index")
        }

        val lopaPath = "projects/taddhita_inheritance/lopa_null_safety.pvm"
        val lopa = CompilerFrontend.lowerModule(
            listOf(CompilerFrontend.SourceUnit(lopaPath, File(lopaPath).readText())),
            "LopaCoverage",
        )
        assertTrue(lopa.entryPoint.any { it is CompilerInstruction.LoadFieldOrLopa })
        val generated = BytecodeCompiler.compileAndLoad(File(lopaPath).readText(), "CompiledLopaCoverage")
        @Suppress("UNCHECKED_CAST")
        val result = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals("लोपः", result.getValue("LastResult").toDisplayText())
        assertEquals(
            SanskritValue.Lopa,
            CompilerValueOperations.recordFieldOrLopa(SanskritValue.Rupa("रिक्त", emptyMap()), "अभाव"),
        )
    }

    @Test
    fun `every repository example module lowers successfully`() {
        val root = File("examples")
        val entries = root.walkTopDown()
            .filter { it.isFile && it.extension == "pvm" && !it.nameWithoutExtension.endsWith("_lib") && it.nameWithoutExtension != "ganita" }
            .sortedBy(File::getPath)
            .toList()
        val failures = entries.mapIndexedNotNull { index, entry ->
            val moduleRoot = root.listFiles().orEmpty().filter(File::isDirectory)
                .first { entry.toPath().startsWith(it.toPath()) }
            val libraries = moduleRoot.walkTopDown().filter { file ->
                file.isFile && file.extension == "pvm" && file != entry &&
                    entry.nameWithoutExtension.contains("mukhya", ignoreCase = true) &&
                    (file.nameWithoutExtension.endsWith("_lib") || file.nameWithoutExtension == "ganita")
            }.sortedBy(File::getPath).toList()
            runCatching {
                val descriptor = PaniniModuleDescriptor(
                        entry.nameWithoutExtension,
                        libraries.map { PaniniModuleSource(it.path, it.readText(), false) } +
                            PaniniModuleSource(entry.path, entry.readText(), true),
                    )
                CompilerFrontend.lowerModule(descriptor, "Coverage_$index")
                val native = CompilerFrontend.lowerModule(descriptor, "NativeCoverage_$index",
                    dev.panini.execution.PvmScript::parseNative)
                GeneratedBytecodeVerifier.verify(CompilerProgramJvmEmitter.emit(native))
            }.exceptionOrNull()?.let { entry to it }
        }

        assertTrue(failures.isEmpty(), failures.joinToString("\n") { (file, error) ->
            "${file.path}: ${error.message}"
        })
    }

    @Test
    fun `two sum position search executes with compiled duplicate parity`() {
        val ordinary = File("examples/algorithms/two_sum.pvm").readText()
        val duplicate = ordinary
            .replace(
                "एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च",
                "त्रि + ङस् त्रि + ओस् चतुर् + आम् पञ्चन् + आम् च",
            )
            .replace(
                "पञ्चन् + भिस् सम",
                "षष् + भिस् सम",
            )
        for ((index, source) in listOf(ordinary, duplicate).withIndex()) {
            val withUnreachableTail = source +
                "\nनवन् + अम् पश्चात् + ङि स्था + णिच् + लोट् + सिप् ।"
            val generated = BytecodeCompiler.compileAndLoad(withUnreachableTail, "CompiledTwoSumPosition$index")
            @Suppress("UNCHECKED_CAST")
            val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
            assertEquals(if (index == 0) "एक चतुर्" else "त्रि त्रि",
                values.getValue("LastResult").toDisplayText())
            assertTrue("लक्ष्य" !in values && "प्राप्त" !in values)
            assertTrue("पश्चात्" !in values)
        }
    }

    @Test
    fun `core deterministic examples have no generic runtime boundary`() {
        val examples = listOf(
            "examples/arithmetic/addition.pvm",
            "examples/arithmetic/average_demo.pvm",
            "examples/arithmetic/min.pvm",
            "examples/arithmetic/mod_demo.pvm",
            "examples/arithmetic/scaling.pvm",
            "examples/arithmetic/sqrt_demo.pvm",
            "examples/algorithms/pythagorean_triplet.pvm",
            "examples/control_flow/conditional.pvm",
            "projects/taddhita_inheritance/purvapara_pipeline.pvm",
        )

        examples.forEachIndexed { index, path ->
            val program = CompilerFrontend.lower(File(path).readText(), "BoundaryGate_$index")
            assertEquals(emptyMap(), CompilerRuntimeBoundaryReport.operations(program), path)
        }
    }

    @Test
    fun `counting example reports its deferred action boundary`() {
        val source = File("examples/arithmetic/count_demo.pvm").readText()
        val program = CompilerFrontend.lower(source, "CountBoundaryGate")
        assertEquals(mapOf("dynamic:गण" to 1), CompilerRuntimeBoundaryReport.operations(program))
        val generated = BytecodeCompiler.compileAndLoad(source, "CountExampleSemantics")
        val values = generated.getMethod("execute").invoke(null) as Map<*, *>
        assertEquals("त्रीणि", (values["LastResult"] as dev.panini.execution.SanskritValue).toDisplayText())
    }
}
