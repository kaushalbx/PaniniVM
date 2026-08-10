package dev.panini

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.Prayoga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationResult
import dev.panini.derivation.KarakaSubantaDerivationRequest
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
import dev.panini.derivation.TingantaDerivationRequest
import dev.panini.derivation.TingantaEngine
import dev.panini.execution.ExecutionResult
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionScope
import dev.panini.execution.PaniniVM
import dev.panini.execution.OutputKind
import dev.panini.execution.Phala
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.sutra.ProgramAvastha
import dev.panini.execution.sutra.ProgramBlueprintContext
import dev.panini.execution.sutra.ProgramBlueprintGranthaEngine
import dev.panini.execution.sutra.ProgramGranthaExecution
import dev.panini.execution.sutra.ProgramGranthaValidation
import dev.panini.execution.sutra.SanskritGranthaSourceCompilation
import dev.panini.execution.sutra.SanskritGranthaSourceCompiler
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.runtime.GranthaId
import dev.panini.unadipatha.UnadiDerivationEngine
import dev.panini.unadipatha.UnadiPatha
import dev.panini.unadipatha.analysis.UnadiAnalyzer
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import dev.panini.cli.PaniniCli

fun main(args: Array<String>) {
    System.setOut(
        PrintStream(
            FileOutputStream(FileDescriptor.out),
            true,
            StandardCharsets.UTF_8,
        ),
    )
    when {
        args.isEmpty() -> PaniniCli().startRepl()
        args.first() in setOf("--eval", "--pvm", "--exec") -> {
            val filePath = args.getOrNull(1) ?: error("Usage: --eval path/to/file.pvm")
            PaniniCli().executeScriptFile(File(filePath))
        }
        else -> runCli(args).forEach(::println)
    }
}

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
    "--grantha" -> {
        val filePath = args.getOrNull(1) ?: error("Usage: --grantha path/to/file.sutra")
        val file = File(filePath)
        require(file.exists()) { "Sūtra grantha source file not found: $filePath" }
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        dev.panini.sankhya.SankhyaCountingFormRenderer.init()
        val execution = ProgramBlueprintGranthaEngine.execute(
            file.readText(),
            ProgramBlueprintContext(
                speaker = "प्रयोक्ता",
                listener = "यन्त्रम्",
                text = file.name,
            ),
            ExecutionScope(
                capabilities = setOf(ExecutionEffect.PURE),
                linguisticServices = dev.panini.derivation.LinguisticActionsInitializer.services(),
            ),
            ProgramAvastha(ValueEnvironment()),
        )
        buildList {
            add("=== Sūtra Grantha Execution: ${file.name} ===")
            when (execution) {
                is ProgramGranthaExecution.Completed -> {
                    when (val result = execution.result) {
                        is dev.panini.sutra.runtime.SutraMachineResult.Failure ->
                            add("✗ ${result.failedSutra}: ${result.message}")
                        is dev.panini.sutra.runtime.SutraMachineResult.Success -> {
                            when (val phala = result.state.lastPhala) {
                                is Phala.Siddha -> phala.values.toSortedMap().forEach { (id, value) ->
                                    add("✓ $id: $value")
                                }
                                null -> add("✗ Grantha completed without a result.")
                                else -> add("✗ $phala")
                            }
                        }
                    }
                }
                is ProgramGranthaExecution.InvalidSource -> execution.diagnostics.forEach {
                    add("✗ ${it.code}${it.position?.let { position -> " at $position" }.orEmpty()}: ${it.message}")
                }
                is ProgramGranthaExecution.InvalidBlueprint -> execution.diagnostics.forEach {
                    add("✗ ${it.code}: ${it.message}")
                }
                is ProgramGranthaExecution.InvalidRuntime -> execution.diagnostics.forEach {
                    add("✗ ${it.code}: ${it.message}")
                }
            }
        }
    }
    "--check-grantha" -> {
        val filePath = args.getOrNull(1) ?: error("Usage: --check-grantha path/to/file.sutra")
        val file = File(filePath)
        require(file.exists()) { "Sūtra grantha source file not found: $filePath" }
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        val validation = ProgramBlueprintGranthaEngine.validate(
            file.readText(),
            ProgramBlueprintContext(
                speaker = "प्रयोक्ता",
                listener = "यन्त्रम्",
                text = file.name,
            ),
        )
        when (validation) {
            is ProgramGranthaValidation.Valid -> listOf(
                "✓ ${file.name}: valid grantha '${validation.grantha.id}' " +
                    "with ${validation.grantha.sutras.size} sūtra(s).",
            )
            is ProgramGranthaValidation.InvalidSource -> validation.diagnostics.map {
                "✗ ${it.code}${it.position?.let { position -> " at $position" }.orEmpty()}: ${it.message}"
            }
            is ProgramGranthaValidation.InvalidBlueprint -> validation.diagnostics.map {
                "✗ ${it.code}: ${it.message}"
            }
            is ProgramGranthaValidation.InvalidRuntime -> validation.diagnostics.map {
                "✗ ${it.code}: ${it.message}"
            }
        }
    }
    "--emit-grantha" -> {
        val inputPath = args.getOrNull(1)
            ?: error("Usage: --emit-grantha path/to/file.pvm [path/to/output.sutra]")
        val input = File(inputPath)
        require(input.exists()) { "PaniniVM source file not found: $inputPath" }
        val programText = input.readLines()
            .map(String::trim)
            .filter { it.isNotEmpty() && !it.startsWith("#") && !it.startsWith("//") }
            .joinToString(separator = "\n")
        val compilation = SanskritGranthaSourceCompiler.compile(
            programText,
            GranthaId(input.nameWithoutExtension),
        )
        when (compilation) {
            is SanskritGranthaSourceCompilation.Invalid ->
                buildList {
                    add("✗ Could not emit ${input.name}:")
                    compilation.diagnostics.forEach { add("  $it") }
                }
            is SanskritGranthaSourceCompilation.Success -> {
                val output = args.getOrNull(2)?.let(::File)
                    ?: File(input.parentFile ?: File("."), "${input.nameWithoutExtension}.sutra")
                output.parentFile?.mkdirs()
                output.writeText(compilation.source)
                listOf(
                    "✓ Emitted ${compilation.grantha.sutras.size} sūtra(s) to ${output.path}",
                )
            }
        }
    }
    "--paradigm" -> {
        val pratipadika = args.getOrNull(1) ?: error("Usage: --paradigm राम")
        SubantaEngine().deriveSupportedParadigm(pratipadika).surfaces.map { (affix, surface) ->
            "${affix.vibhakti} ${affix.vacana}: $surface"
        }
    }
    "--derive" -> {
        val pratipadika = args.getOrNull(1) ?: error("Usage: --derive राम SASTHI BAHUVACANA")
        val vibhakti = parseVibhakti(args.getOrNull(2) ?: error("Missing vibhakti."))
        val vacana = parseVacana(args.getOrNull(3) ?: error("Missing vacana."))
        val result = SubantaEngine().derive(
            SubantaDerivationRequest(
                pratipadika,
                vibhakti,
                vacana,
            )
        )

        buildList {
            add("$vibhakti $vacana: ${result.final.surface}")
            addTrace(result, includeRole = true)
        }
    }
    "--derive-karaka" -> {
        val pratipadika = args.getOrNull(1) ?: error("Usage: --derive-karaka <pratipadika> <karaka> <vacana> <dhatu> [prayoga]")
        val karaka = parseKaraka(args.getOrNull(2) ?: error("Missing karaka."))
        val vacana = parseVacana(args.getOrNull(3) ?: error("Missing vacana."))
        val dhatu = args.getOrNull(4) ?: error("Missing dhatu.")
        val prayoga = args.getOrNull(5)?.let(::parsePrayoga) ?: Prayoga.KARTARI
        val result = SubantaEngine().deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = pratipadika,
                karaka = karaka,
                vacana = vacana,
                dhatu = dhatu,
                prayoga = prayoga
            )
        )

        buildList {
            val resolvedVibhakti = result.initial.context.rupa.vibhakti ?: Vibhakti.PRATHAMA
            add("$resolvedVibhakti $vacana: ${result.final.surface}")
            result.karakaResolution?.let { res ->
                add("Semantic Karaka Resolution Trace:")
                res.evidence.forEach { ev ->
                    add("  ${ev.sutra} — ${ev.reason} (${ev.text})")
                }
            }
            addTrace(result, includeRole = true)
        }
    }
    "--derive-unadi" -> {
        val dhatu = args.getOrNull(1) ?: error("Usage: --derive-unadi <dhatu> <pratyaya>")
        val pratyaya = args.getOrNull(2) ?: error("Usage: --derive-unadi <dhatu> <pratyaya>")
        val result = UnadiDerivationEngine.derive(dhatu, pratyaya)
        buildList {
            add("=== Uṇādi Derivation Tracing for ($dhatu + $pratyaya) ===")
            add("Initial State: ${result.initial.terms.joinToString(" + ") { it.surface }}")
            add("Final Derived Surface: ${result.final.surface}")
            addTrace(result, includeRole = true)
        }
    }
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
    "--verb" -> {
        val dhatu = args.getOrNull(1) ?: error("Usage: --verb भू [LAT|LRT|LOT|LANG|LING] [EKAVACANA|DVIVACANA|BAHUVACANA]")
        val requestedLakara = args.getOrNull(2)?.let(::findLakara)
        val lakara = requestedLakara ?: Lakara.LAT
        val vacanaIndex = if (requestedLakara == null) 2 else 3
        val vacana = args.getOrNull(vacanaIndex)?.let(::parseVacana) ?: Vacana.EKAVACANA
        val result = TingantaEngine().derive(TingantaDerivationRequest(dhatu, vacana, lakara = lakara))
        buildList {
            add("$dhatu: ${result.final.surface}")
            addTrace(result)
        }
    }
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
                addTrace(result, includeRole = true)
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

private fun parseVibhakti(value: String): Vibhakti = when (value.uppercase()) {
    "PRATHAMA", "प्रथमा" -> Vibhakti.PRATHAMA
    "DVITIYA", "द्वितीया" -> Vibhakti.DVITIYA
    "TRTIYA", "तृतीया" -> Vibhakti.TRTIYA
    "CHATURTHI", "चतुर्थी" -> Vibhakti.CHATURTHI
    "PANCHAMI", "पञ्चमी", "पंचमी" -> Vibhakti.PANCHAMI
    "SASTHI", "षष्ठी" -> Vibhakti.SASTHI
    "SAPTAMI", "सप्तमी" -> Vibhakti.SAPTAMI
    else -> error("Unknown vibhakti: $value")
}

private fun parseVacana(value: String): Vacana = when (value.uppercase()) {
    "EKAVACANA", "एकवचन" -> Vacana.EKAVACANA
    "DVIVACANA", "द्विवचन" -> Vacana.DVIVACANA
    "BAHUVACANA", "बहुवचन" -> Vacana.BAHUVACANA
    else -> error("Unknown vacana: $value")
}

private fun findLakara(value: String): Lakara? = Lakara.entries.firstOrNull {
    it.name == value.uppercase() || it.upadesha == value
}

private fun MutableList<String>.addTrace(result: DerivationResult, includeRole: Boolean = false) {
    add("----------------------------------------")
    result.applications.forEach { app ->
        val prefix = if (includeRole) " [${app.role::class.simpleName}]" else ""
        add("${app.sutra}$prefix — ${app.after.rawJoinedSurface} (${app.explanation})")
        app.conflictTrace.forEach { add("  ↳ $it") }
    }
}

private fun parseKaraka(value: String): Karaka = when (value.uppercase()) {
    "KARTR", "कर्ता" -> Karaka.KARTR
    "KARMAN", "कर्म" -> Karaka.KARMAN
    "KARANA", "करण" -> Karaka.KARANA
    "SAMPRADANA", "सम्प्रदान", "संप्रदान" -> Karaka.SAMPRADANA
    "APADANA", "अपादान" -> Karaka.APADANA
    "ADHIKARANA", "अधिकरण" -> Karaka.ADHIKARANA
    "SAMBANDHA", "सम्बन्ध", "संबंध" -> Karaka.SAMBANDHA
    "SAMBODHANA", "सम्बोधन", "संबोधन" -> Karaka.SAMBODHANA
    "ANIRDHARITA" -> Karaka.ANIRDHARITA
    else -> error("Unknown karaka: $value")
}

private fun parsePrayoga(value: String): Prayoga = when (value.uppercase()) {
    "KARTARI", "कर्तरि" -> Prayoga.KARTARI
    "KARMANI", "कर्मणि" -> Prayoga.KARMANI
    "BHAVE", "भावे" -> Prayoga.BHAVE
    "CAUSATIVE" -> Prayoga.CAUSATIVE
    "ANIRDHARITA" -> Prayoga.ANIRDHARITA
    else -> error("Unknown prayoga: $value")
}
