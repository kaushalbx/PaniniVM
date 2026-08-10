package dev.panini.cli

import dev.panini.compiler.BytecodeCompiler
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.execution.ExecutionResult
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.InputRequest
import dev.panini.execution.InputValueType
import dev.panini.execution.PaniniVM
import dev.panini.execution.toInputLongOrNull
import dev.panini.aryabhatiya.AryabhatiyaDecoder
import dev.panini.aryabhatiya.AryabhatiyaEncoder
import dev.panini.aryabhatiya.AryabhatiyaMapping
import dev.panini.katapayadi.KatapayadiDecoder
import dev.panini.katapayadi.KatapayadiEncoder
import dev.panini.katapayadi.KatapayadiMapping
import dev.panini.bhutasamkhya.BhutasamkhyaDecoder
import dev.panini.bhutasamkhya.BhutasamkhyaEncoder
import dev.panini.bhutasamkhya.BhutasamkhyaLexicon
import java.io.File
import java.io.InputStream
import java.io.PrintStream
import java.io.BufferedReader

/**
 * PaniniCli manages the interactive REPL and script file evaluations for the command line interface.
 */
class PaniniCli(
    private val vm: PaniniVM = PaniniVM(),
    private val inputStream: InputStream = System.`in`,
    private val outputStream: PrintStream = System.out,
) {
    private var showTrace = false
    private var sessionKey = "cli_session"
    private val reader: BufferedReader = inputStream.bufferedReader()

    init {
        vm.registerExternalCapability(ExecutionEffect.READ_RESOURCE) { payload, _ ->
            val request = InputRequest.decode(payload) ?: InputRequest(payload, InputValueType.TEXT)
            readInteractiveValue(request)
        }
    }

    private fun readInteractiveValue(request: InputRequest): String {
        while (true) {
            val typeHint = if (request.type == InputValueType.NUMBER) " (number)" else ""
            outputStream.println("Enter value for ${request.variableName}$typeHint:")
            outputStream.flush()
            val value = reader.readLine()
                ?: throw IllegalStateException("End of input while reading ${request.variableName}.")
            if (request.type != InputValueType.NUMBER || value.toInputLongOrNull() != null) return value
            outputStream.println("Invalid number '$value'. Enter ASCII or Devanagari digits.")
        }
    }

    fun startRepl() {
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
                    if (res.isExplicitOutput() && res.value.isNotBlank()) {
                        outputStream.println(res.value)
                    }
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
                is ExecutionResult.NeedsApproval -> {
                    outputStream.println("Line ${i + 1} Needs Approval: ID ${res.invocationId} requires effects ${res.requiredEffects}")
                }
                is ExecutionResult.NeedsAcceptance -> {
                    outputStream.println("Line ${i + 1} Needs Acceptance: ID ${res.invocationId} requires acceptance from ${res.speaker} to ${res.listener}")
                }
            }
        }
        return results
    }

    private fun ExecutionResult.Success.isExplicitOutput(): Boolean = trace.any {
        it.contains("Printed") ||
            it.contains("प्रदर्शनम्") ||
            it.contains("मुद्रणम्") ||
            it.contains("प्रेषणम्")
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
            is ReplCommand.DecodeNumeral -> {
                decodeNumeral(command.numeral)
            }
            is ReplCommand.EncodeNumber -> {
                encodeNumber(command.value, command.system)
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
            is ExecutionResult.NeedsApproval -> {
                outputStream.println("? needs approval: ID ${result.invocationId} requires effects ${result.requiredEffects}")
            }
            is ExecutionResult.NeedsAcceptance -> {
                outputStream.println("? needs acceptance: ID ${result.invocationId} requires acceptance from ${result.speaker} to ${result.listener}")
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

    private fun decodeNumeral(numeral: String) {
        if (numeral.isBlank()) {
            outputStream.println("Usage: :num <Sanskrit word> (e.g. :num माधव or :num नेत्र-वेद)")
            return
        }

        val results = mutableListOf<String>()

        // 1. Bhutasamkhya
        val parts = numeral.split("-", " ").map { it.trim() }.filter { it.isNotEmpty() }
        if (parts.isNotEmpty() && parts.all { BhutasamkhyaLexicon.isSymbol(it) }) {
            val decoded = runCatching { BhutasamkhyaDecoder().decode(numeral) }.getOrNull()
            if (decoded != null) {
                results.add("$decoded (Bhūta-saṅkhyā)")
            }
        }

        // 2. Aryabhatiya
        val isValidArya = numeral.all { AryabhatiyaMapping.isConsonant(it) || AryabhatiyaMapping.getVowelPower(it) != null || it == '्' }
        if (isValidArya) {
            val decoded = runCatching { AryabhatiyaDecoder().decode(numeral) }.getOrNull()
            if (decoded != null) {
                results.add("$decoded (Āryabhaṭīya)")
            }
        }

        // 3. Katapayadi
        val hasKataConsonant = numeral.any { KatapayadiMapping.isConsonant(it) }
        if (hasKataConsonant) {
            val decoded = runCatching { KatapayadiDecoder().decode(numeral) }.getOrNull()
            if (decoded != null) {
                results.add("$decoded (Kaṭapayādi)")
            }
        }

        if (results.isEmpty()) {
            outputStream.println("Could not decode '$numeral' using any known Sanskrit numeral system.")
        } else {
            results.forEach { outputStream.println("⇒ $it") }
        }
    }

    private fun encodeNumber(value: Long, system: String?) {
        if (value < 0L) {
            outputStream.println("Usage: :encode <positive integer> [system: katapayadi|bhutasamkhya|aryabhatiya]")
            return
        }

        val targetSystem = system ?: "katapayadi"
        val encoded = when (targetSystem) {
            "katapayadi", "कटपय" -> {
                runCatching { KatapayadiEncoder().encode(value) }.getOrNull()?.let { "$it (Kaṭapayādi)" }
            }
            "bhutasamkhya", "भूतसङ्ख्या", "भूतसंख्या" -> {
                runCatching { BhutasamkhyaEncoder().encode(value) }.getOrNull()?.let { "$it (Bhūta-saṅkhyā)" }
            }
            "aryabhatiya", "आर्यभटीय" -> {
                runCatching { AryabhatiyaEncoder().encode(value) }.getOrNull()?.let { "$it (Āryabhaṭīya)" }
            }
            else -> null
        }

        if (encoded == null) {
            outputStream.println("Error: Failed to encode $value using system '$targetSystem'.")
        } else {
            outputStream.println("⇒ $encoded")
        }
    }

    private fun printHelp() {
        outputStream.println("Available REPL Commands:")
        outputStream.println("  :help               - Show this help message")
        outputStream.println("  :dhatu <query>     - Look up Dhātupāṭha entry by upadeśa, id, or alias")
        outputStream.println("  :compile <file> [C] - Compile .pvm script to JVM bytecode class")
        outputStream.println("  :num <word>         - Decode a Sanskrit numeral into a decimal value")
        outputStream.println("  :encode <val> [sys] - Encode a decimal value into a Sanskrit numeral (sys: katapayadi|bhutasamkhya|aryabhatiya)")
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
