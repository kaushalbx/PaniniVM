package dev.panini

import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionScope
import dev.panini.execution.Phala
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.sutra.*
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SutraMachineResult
import java.io.File

internal object GranthaCliCommands {
    fun execute(args: List<String>): List<String> {
        val file = sourceFile(args, "--grantha")
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        dev.panini.sankhya.SankhyaCountingFormRenderer.init()
        val execution = ProgramBlueprintGranthaEngine.execute(
            file.readText(), context(file),
            ExecutionScope(
                capabilities = setOf(ExecutionEffect.PURE),
                linguisticServices = dev.panini.derivation.LinguisticActionsInitializer.services(),
            ),
            ProgramAvastha(ValueEnvironment()),
        )
        return buildList {
            add("=== Sūtra Grantha Execution: ${file.name} ===")
            when (execution) {
                is ProgramGranthaExecution.Completed -> when (val result = execution.result) {
                    is SutraMachineResult.Failure -> add("✗ ${result.failedSutra}: ${result.message}")
                    is SutraMachineResult.Success -> when (val phala = result.state.lastPhala) {
                        is Phala.Siddha -> phala.values.toSortedMap().forEach { (id, value) -> add("✓ $id: $value") }
                        null -> add("✗ Grantha completed without a result.")
                        else -> add("✗ $phala")
                    }
                }
                is ProgramGranthaExecution.InvalidSource -> execution.diagnostics.forEach { add(sourceDiagnostic(it.code.toString(), it.position, it.message)) }
                is ProgramGranthaExecution.InvalidBlueprint -> execution.diagnostics.forEach { add("✗ ${it.code}: ${it.message}") }
                is ProgramGranthaExecution.InvalidRuntime -> execution.diagnostics.forEach { add("✗ ${it.code}: ${it.message}") }
            }
        }
    }

    fun check(args: List<String>): List<String> {
        val file = sourceFile(args, "--check-grantha")
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        return when (val validation = ProgramBlueprintGranthaEngine.validate(file.readText(), context(file))) {
            is ProgramGranthaValidation.Valid -> listOf("✓ ${file.name}: valid grantha '${validation.grantha.id}' with ${validation.grantha.sutras.size} sūtra(s).")
            is ProgramGranthaValidation.InvalidSource -> validation.diagnostics.map { sourceDiagnostic(it.code.toString(), it.position, it.message) }
            is ProgramGranthaValidation.InvalidBlueprint -> validation.diagnostics.map { "✗ ${it.code}: ${it.message}" }
            is ProgramGranthaValidation.InvalidRuntime -> validation.diagnostics.map { "✗ ${it.code}: ${it.message}" }
        }
    }

    fun emit(args: List<String>): List<String> {
        val inputPath = args.getOrNull(0) ?: error("Usage: --emit-grantha path/to/file.pvm [path/to/output.sutra]")
        val input = File(inputPath)
        require(input.exists()) { "PaniniVM source file not found: $inputPath" }
        val text = input.readLines().map(String::trim)
            .filter { it.isNotEmpty() && !it.startsWith("#") && !it.startsWith("//") }.joinToString("\n")
        return when (val compilation = SanskritGranthaSourceCompiler.compile(text, GranthaId(input.nameWithoutExtension))) {
            is SanskritGranthaSourceCompilation.Invalid -> buildList {
                add("✗ Could not emit ${input.name}:")
                compilation.diagnostics.forEach { add("  $it") }
            }
            is SanskritGranthaSourceCompilation.Success -> {
                val output = args.getOrNull(1)?.let(::File) ?: File(input.parentFile ?: File("."), "${input.nameWithoutExtension}.sutra")
                output.parentFile?.mkdirs()
                output.writeText(compilation.source)
                listOf("✓ Emitted ${compilation.grantha.sutras.size} sūtra(s) to ${output.path}")
            }
        }
    }

    private fun sourceFile(args: List<String>, command: String): File {
        val path = args.getOrNull(0) ?: error("Usage: $command path/to/file.sutra")
        return File(path).also { require(it.exists()) { "Sūtra grantha source file not found: $path" } }
    }

    private fun context(file: File) = ProgramBlueprintContext("प्रयोक्ता", "यन्त्रम्", file.name)

    private fun sourceDiagnostic(code: String, position: Any?, message: String) =
        "✗ $code${position?.let { " at $it" }.orEmpty()}: $message"
}
