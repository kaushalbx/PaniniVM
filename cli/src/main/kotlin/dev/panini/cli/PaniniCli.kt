package dev.panini.cli

import dev.panini.compiler.BytecodeCompiler
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import java.io.File
import java.io.InputStream
import java.io.PrintStream

class PaniniCli(
    private val vm: PaniniVM = PaniniVM(),
    private val inputStream: InputStream = System.`in`,
    private val outputStream: PrintStream = System.out,
) {
    private var showTrace = false
    private var sessionKey = "cli_session"

    fun startRepl() {
        val reader = inputStream.bufferedReader()
        outputStream.println("═══════════════════════════════════════════════════════════")
        outputStream.println("              PāṇiniVM Interactive REPL (pvm-cli)          ")
        outputStream.println("  Type Sanskrit utterances or REPL commands (:help for usage)")
        outputStream.println("═══════════════════════════════════════════════════════════")

        while (true) {
            outputStream.print("pvm> ")
            outputStream.flush()
            val line = reader.readLine() ?: break
            val command = ReplCommand.parse(line)
            if (!processCommand(command)) break
        }
    }

    fun executeScriptFile(file: File): List<ExecutionResult> {
        outputStream.println("[PaniniVM CLI] Executing file: ${file.name}")
        val results = vm.evalFile(file, sessionKey = sessionKey)
        results.forEachIndexed { i, res ->
            when (res) {
                is ExecutionResult.Success -> {
                    outputStream.println("Line ${i + 1}: ${res.value}")
                    if (showTrace) {
                        res.trace.forEach { outputStream.println("  ├─► $it") }
                    }
                }
                is ExecutionResult.Failure -> {
                    outputStream.println("Line ${i + 1} Error: ${res.message}")
                }
                is ExecutionResult.Ambiguous -> {
                    outputStream.println("Line ${i + 1} Ambiguous: ${res.matchingOperations.joinToString()}")
                }
                is ExecutionResult.NeedsInput -> {
                    outputStream.println("Line ${i + 1} Needs Input: ${res.missingKarakas.joinToString()}")
                }
            }
        }
        return results
    }

    fun processCommand(command: ReplCommand): Boolean {
        when (command) {
            is ReplCommand.Exit -> {
                outputStream.println("शुभमस्तु! (Exiting PāṇiniVM REPL)")
                return false
            }
            is ReplCommand.Help -> {
                printHelp()
            }
            is ReplCommand.ToggleTrace -> {
                showTrace = !showTrace
                outputStream.println("Derivation trace log: ${if (showTrace) "ENABLED" else "DISABLED"}")
            }
            is ReplCommand.LookupDhatu -> {
                lookupDhatu(command.query)
            }
            is ReplCommand.CompileScript -> {
                compileScript(command.filePath, command.className)
            }
            is ReplCommand.EvalUtterance -> {
                if (command.utterance.isNotBlank()) {
                    evalSingle(command.utterance)
                }
            }
        }
        return true
    }

    private fun evalSingle(utterance: String) {
        val result = vm.eval(utterance, sessionKey = sessionKey)
        when (result) {
            is ExecutionResult.Success -> {
                outputStream.println("⇒ ${result.value}")
                if (showTrace) {
                    result.trace.forEach { outputStream.println("  ├─► $it") }
                }
            }
            is ExecutionResult.Failure -> {
                outputStream.println("✗ error: ${result.message}")
            }
            is ExecutionResult.Ambiguous -> {
                outputStream.println("? ambiguous: ${result.matchingOperations.joinToString()}")
            }
            is ExecutionResult.NeedsInput -> {
                outputStream.println("? needs input for: ${result.missingKarakas.joinToString()}")
            }
        }
    }

    private fun lookupDhatu(query: String) {
        if (query.isBlank()) {
            outputStream.println("Usage: :dhatu <upadesha|id|alias> (e.g. :dhatu युजिँर् or :dhatu 07.0007)")
            return
        }
        val dhatus = DhatuPatha.all.filter {
            it.upadesha.contains(query) || it.id == query || it.sourceSurface.contains(query) || it.surfaceAliases.contains(query)
        }
        if (dhatus.isEmpty()) {
            outputStream.println("No dhātu found matching '$query'.")
        } else {
            dhatus.forEach { d ->
                outputStream.println("Dhātu ${d.id} | ${d.upadesha} (${d.sourceSurface}) | Gaṇa: ${d.gana}")
                outputStream.println("  Meaning: ${d.artha} (${d.arthaHindi})")
                outputStream.println("  Operations: ${d.operations.size} registered")
            }
        }
    }

    private fun compileScript(filePath: String, className: String) {
        if (filePath.isBlank()) {
            outputStream.println("Usage: :compile <file.pvm> [ClassName]")
            return
        }
        val file = File(filePath)
        if (!file.exists()) {
            outputStream.println("File not found: $filePath")
            return
        }
        val targetClass = if (className.isBlank()) "GeneratedProgram" else className
        val outputDir = File("build/compiled_classes")
        BytecodeCompiler.compileFile(file, targetClass, outputDir)
        val compiledFile = File(outputDir, "$targetClass.class")
        outputStream.println("✓ Compiled ${file.name} -> $targetClass.class (${compiledFile.length()} bytes)")
    }

    private fun printHelp() {
        outputStream.println("Available REPL Commands:")
        outputStream.println("  :help               - Show this help message")
        outputStream.println("  :dhatu <query>     - Look up Dhātupāṭha entry by upadeśa, id, or alias")
        outputStream.println("  :compile <file> [C] - Compile .pvm script to JVM bytecode class")
        outputStream.println("  :trace              - Toggle displaying full step-by-step Sūtra trace log")
        outputStream.println("  :exit               - Exit the REPL session")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val cli = PaniniCli()
            if (args.isEmpty()) {
                cli.startRepl()
            } else {
                val scriptFile = File(args[0])
                if (scriptFile.exists()) {
                    cli.executeScriptFile(scriptFile)
                } else {
                    println("Script file not found: ${args[0]}")
                }
            }
        }
    }
}
