package dev.panini.compiler

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CompilerCoverageTest {
    @Test
    fun `unsupported example count cannot regress and every failure is categorized`() {
        val examples = File("examples").walkTopDown()
            .filter { it.isFile && it.extension == "pvm" }
            .sortedBy(File::getPath)
            .toList()
        val failures = examples.mapIndexedNotNull { index, file ->
            runCatching {
                CompilerFrontend.lower(file.readText(), "Coverage_$index")
            }.exceptionOrNull()?.let { file to it }
        }

        assertTrue(failures.size <= 4, failures.joinToString("\n") { (file, error) ->
            "${file.path}: ${error.message}"
        })
        assertTrue(failures.none { (_, error) ->
            CompilerFailureClassifier.classify(error) == CompilerUnsupportedKind.UNKNOWN
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
            "examples/taddhita_inheritance/purvapara_pipeline.pvm",
        )

        examples.forEachIndexed { index, path ->
            val program = CompilerFrontend.lower(File(path).readText(), "BoundaryGate_$index")
            assertEquals(emptyMap(), CompilerRuntimeBoundaryReport.operations(program), path)
        }
    }
}
