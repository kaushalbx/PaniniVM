package dev.panini

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.OutputKind
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.unadipatha.UnadiPatha
import dev.panini.unadipatha.analysis.UnadiAnalyzer
import java.io.File
internal fun runCli(args: Array<String>): List<String> = when (args.firstOrNull()) {
    "--render-readable" -> {
        val sourcePath = args.getOrNull(1) ?: error("Usage: --render-readable path/to/file.pvm|directory")
        val generated = dev.panini.execution.PvmReadableSanskrit.renderPath(File(sourcePath))
        listOf("Generated ${generated.size} readable Sanskrit file(s).") +
            generated.map { "  ${it.path}" }
    }
    "--compile" -> {
        val filePath = args.getOrNull(1) ?: error("Usage: --compile path/to/file.pvm [ClassName] [OutputDir]")
        val className = args.getOrNull(2) ?: "CompiledProgram"
        val outputDirPath = args.getOrNull(3) ?: "build/classes/panini"
        val file = File(filePath)
        val outputDir = File(outputDirPath)
        dev.panini.compiler.BytecodeCompiler.compileFile(file, className, outputDir)
        listOf("Compiled ${file.name} to $outputDirPath/$className.class successfully.")
    }
    "--eval", "--pvm", "--exec" -> {
        val filePath = args.getOrNull(1) ?: error("Usage: --eval path/to/file.pvm")
        val file = File(filePath)
        require(file.exists()) { "PaniniVM script file not found: $filePath" }
        val vm = PaniniVM()
        val sessionKey = "session_${file.nameWithoutExtension}_${System.currentTimeMillis()}"
        val results = vm.evalFile(file, sessionKey = sessionKey)
        buildList {
            results.forEach { res ->
                when (res) {
                    is ExecutionResult.Success -> {
                        if (res.value.isNotBlank() && res.outputKind != OutputKind.INTERNAL) {
                            add(res.value)
                        }
                    }
                    is ExecutionResult.Failure -> {
                        add("Error: ${res.message}")
                    }
                    is ExecutionResult.NeedsInput -> {
                        add("Needs input: ${res.message} (missing: ${res.missingKarakas})")
                    }
                    is ExecutionResult.Ambiguous -> {
                        add("Ambiguous: ${res.message} (matches: ${res.matchingOperations})")
                    }
                    is ExecutionResult.NeedsApproval -> {
                        add("Needs approval: ID: ${res.invocationId} (effects: ${res.requiredEffects})")
                    }
                    is ExecutionResult.NeedsAcceptance -> {
                        add("Needs acceptance: ID: ${res.invocationId} (from ${res.speaker} to ${res.listener})")
                    }
                }
            }
        }
    }
    "--grantha" -> GranthaCliCommands.execute(args.drop(1))
    "--check-grantha" -> GranthaCliCommands.check(args.drop(1))
    "--emit-grantha" -> GranthaCliCommands.emit(args.drop(1))
    "--paradigm" -> DerivationCliCommands.paradigm(args.drop(1))
    "--derive" -> DerivationCliCommands.nominal(args.drop(1))
    "--derive-karaka" -> DerivationCliCommands.karaka(args.drop(1))
    "--derive-unadi" -> DerivationCliCommands.unadi(args.drop(1))
    "--sutra" -> {
        val number = args.getOrNull(1) ?: error("Usage: --sutra 7.1.54")
        val sutra = Ashtadhyayi.registry.require(number) as? Sutra<*, *>
            ?: error("$number is not represented by Sutra.")
        buildList {
            add("${sutra.number} ${sutra.text}")
            add(sutra.hindiExplanation)
            add("role=${sutra.role}; action=${sutra.action}; scope=${sutra.scope}; stage=${sutra.stage}")
            if (sutra.nimittaScope != NimittaScope.UNKNOWN) add("nimittaScope=${sutra.nimittaScope}")
            if (sutra.dependencies.isNotEmpty()) add("dependencies=${sutra.dependencies.joinToString()}")
            if (sutra.blocks.isNotEmpty()) add("blocks=${sutra.blocks.joinToString()}")
            if (sutra.restrictions.isNotEmpty()) add("restrictions=${sutra.restrictions.joinToString()}")
            if (sutra.exceptions.isNotEmpty()) add("exceptions=${sutra.exceptions.joinToString()}")
        }
    }
    "--verb" -> DerivationCliCommands.verb(args.drop(1))
    "--unadi", "--unadipatha" -> {
        val mode = args.getOrNull(1)?.lowercase() ?: "list"
        when (mode) {
            "lookup" -> {
                val word = args.getOrNull(2) ?: error("Usage: --unadi lookup <word>")
                val analysis = UnadiAnalyzer.analyzeStem(word)
                buildList {
                    add("=== Uṇādi Etymological Analysis for '$word' ===")
                    add("Classification: ${analysis.classification}")
                    if (analysis.matches.isEmpty()) {
                        add("No matching Uṇādi sūtra found in catalog.")
                    } else {
                        analysis.matches.forEach { m ->
                            add("  Sūtra ${m.sutraNumber}: ${m.sutraText}")
                            add("  Dhātu: ${m.dhatu.upadesha} (${m.dhatu.sourceSurface})")
                            add("  Pratyaya: ${m.pratyaya} (surface: ${m.pratyayaSurface})")
                            add("  Saṁjñās: ${m.samjnas}")
                        }
                    }
                }
            }
            "pair" -> {
                val dhatuText = args.getOrNull(2) ?: error("Usage: --unadi pair <dhatu> <pratyaya>")
                val pratyaya = args.getOrNull(3) ?: error("Usage: --unadi pair <dhatu> <pratyaya>")
                val dhatuObj = UnadiPatha.sutras.flatMap { it.roots }
                    .firstOrNull { it.sourceSurface == dhatuText || it.upadesha == dhatuText }
                    ?: error("Dhātu '$dhatuText' not found in Uṇādipāṭha catalog.")
                val analysis = UnadiAnalyzer.analyzePair(dhatuObj, pratyaya)
                buildList {
                    add("=== Uṇādi Pair Analysis for ($dhatuText, $pratyaya) ===")
                    if (analysis == null) {
                        add("No matching Uṇādi sūtra found for ($dhatuText, $pratyaya).")
                    } else {
                        add("Classification: ${analysis.classification}")
                        analysis.matches.forEach { m ->
                            add("  Sūtra ${m.sutraNumber}: ${m.sutraText}")
                            add("  Saṁjñās: ${m.samjnas}")
                        }
                    }
                }
            }
            "list" -> {
                buildList {
                    add("=== Uṇādipāṭha Catalog (${UnadiPatha.sutras.size} sūtras) ===")
                    UnadiPatha.sutras.forEach { s ->
                        add("  ${s.number} : ${s.text} [Pratyaya: ${s.pratyaya}]")
                    }
                }
            }
            else -> error("Unknown --unadi mode: $mode. Use lookup, pair, or list.")
        }
    }
    "--sankhya" -> {
        val valueText = args.getOrNull(1)
            ?: error("Usage: --sankhya INTEGER [cardinal|ordinal] [--variants]")
        val value = valueText.toLongOrNull()
            ?: error("Invalid integer for --sankhya: $valueText")
        require(value >= 0L) { "Sankhya must be non-negative: $value" }

        val positional = args.drop(2).filterNot { it == "--variants" }
        require(positional.size <= 1) {
            "Usage: --sankhya INTEGER [cardinal|ordinal] [--variants]"
        }
        val kind = when (positional.firstOrNull()?.lowercase() ?: "cardinal") {
            "cardinal" -> SankhyaKind.CARDINAL
            "ordinal" -> SankhyaKind.ORDINAL
            else -> error("Unknown sankhya kind: ${positional.single()}; expected cardinal or ordinal.")
        }
        val variants = "--variants" in args.drop(2)
        val generator = SankhyaGenerator()
        val results = when (kind) {
            SankhyaKind.CARDINAL -> if (variants) generator.cardinalVariants(value) else listOf(generator.cardinal(value))
            SankhyaKind.ORDINAL -> if (variants) generator.ordinalVariants(value) else listOf(generator.ordinal(value))
        }

        buildList {
            results.forEachIndexed { index, result ->
                val variant = if (results.size > 1) " [${index + 1}/${results.size}]" else ""
                add("${kind.name} $value$variant: ${result.final.surface}")
                DerivationTraceRenderer.appendTo(this, result, includeRole = true)
            }
        }
    }
    "--coverage" -> listOf(
        "loaded=${Ashtadhyayi.pathitaCount}; executable=${Ashtadhyayi.kriyavatCount}; total=${Ashtadhyayi.expectedSutraCount}; remaining=${Ashtadhyayi.remainingCount}",
        "roles=" + Ashtadhyayi.registry.sutras.groupingBy { it.role::class.simpleName }.eachCount().entries.joinToString { "${it.key}=${it.value}" },
    )
    else -> listOf("Usage: --render-readable file.pvm|directory | --eval file.pvm | --emit-grantha file.pvm [output.sutra] | --check-grantha file.sutra | --grantha file.sutra | --compile file.pvm | --paradigm राम | --derive राम SASTHI BAHUVACANA | --derive-unadi कृ उण् | --verb भू | --unadi [lookup|pair|list] | --sankhya 23 | --sutra 7.1.54 | --coverage")
}

private enum class SankhyaKind { CARDINAL, ORDINAL }
