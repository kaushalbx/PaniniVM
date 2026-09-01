package dev.panini.compiler

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import dev.panini.execution.SanskritValue

class CompilerCoverageTest {
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
                CompilerFrontend.lowerModule(
                    PaniniModuleDescriptor(
                        entry.nameWithoutExtension,
                        libraries.map { PaniniModuleSource(it.path, it.readText(), false) } +
                            PaniniModuleSource(entry.path, entry.readText(), true),
                    ),
                    "Coverage_$index",
                )
            }.exceptionOrNull()?.let { entry to it }
        }

        assertTrue(failures.isEmpty(), failures.joinToString("\n") { (file, error) ->
            "${file.path}: ${error.message}"
        })
    }

    @Test
    fun `core deterministic examples have no generic runtime boundary`() {
        val examples = listOf(
            "examples/arithmetic/addition.pvm",
            "examples/arithmetic/average_demo.pvm",
            "examples/arithmetic/count_demo.pvm",
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
}
