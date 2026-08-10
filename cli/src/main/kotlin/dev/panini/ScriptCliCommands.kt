package dev.panini

import dev.panini.compiler.BytecodeCompiler
import dev.panini.execution.ExecutionResult
import dev.panini.execution.OutputKind
import dev.panini.execution.PaniniVM
import dev.panini.execution.PvmReadableSanskrit
import java.io.File

internal object ScriptCliCommands {
    fun renderReadable(args: List<String>): List<String> {
        val sourcePath = args.getOrNull(0) ?: error("Usage: --render-readable path/to/file.pvm|directory")
        val generated = PvmReadableSanskrit.renderPath(File(sourcePath))
        return listOf("Generated ${generated.size} readable Sanskrit file(s).") + generated.map { "  ${it.path}" }
    }

    fun compile(args: List<String>): List<String> {
        val filePath = args.getOrNull(0) ?: error("Usage: --compile path/to/file.pvm [ClassName] [OutputDir]")
        val className = args.getOrNull(1) ?: "CompiledProgram"
        val outputPath = args.getOrNull(2) ?: "build/classes/panini"
        val file = File(filePath)
        BytecodeCompiler.compileFile(file, className, File(outputPath))
        return listOf("Compiled ${file.name} to $outputPath/$className.class successfully.")
    }

    fun evaluate(args: List<String>): List<String> {
        val filePath = args.getOrNull(0) ?: error("Usage: --eval path/to/file.pvm")
        val file = File(filePath)
        require(file.exists()) { "PaniniVM script file not found: $filePath" }
        val results = PaniniVM().evalFile(
            file,
            sessionKey = "session_${file.nameWithoutExtension}_${System.currentTimeMillis()}",
        )
        return buildList {
            results.forEach { result ->
                when (result) {
                    is ExecutionResult.Success -> if (result.value.isNotBlank() && result.outputKind != OutputKind.INTERNAL) add(result.value)
                    is ExecutionResult.Failure -> add("Error: ${result.message}")
                    is ExecutionResult.NeedsInput -> add("Needs input: ${result.message} (missing: ${result.missingKarakas})")
                    is ExecutionResult.Ambiguous -> add("Ambiguous: ${result.message} (matches: ${result.matchingOperations})")
                    is ExecutionResult.NeedsApproval -> add("Needs approval: ID: ${result.invocationId} (effects: ${result.requiredEffects})")
                    is ExecutionResult.NeedsAcceptance -> add("Needs acceptance: ID: ${result.invocationId} (from ${result.speaker} to ${result.listener})")
                }
            }
        }
    }
}
