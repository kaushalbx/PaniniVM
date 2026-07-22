package dev.panini

import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
import dev.panini.derivation.SubantaStemClass
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.TingantaDerivationRequest
import dev.panini.derivation.TingantaEngine
import dev.panini.core.Lakara
import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.sutra.Sutra
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.sankhya.SankhyaGenerator
import java.io.File
import java.math.BigInteger

fun main(args: Array<String>) {
    runCli(args).forEach(::println)
}

internal fun runCli(args: Array<String>): List<String> = when (args.firstOrNull()) {
    "--eval", "--pvm", "--exec" -> {
        val filePath = args.getOrNull(1) ?: error("Usage: --eval path/to/file.pvm")
        val file = File(filePath)
        require(file.exists()) { "PaniniVM script file not found: $filePath" }
        val vm = PaniniVM()
        val results = vm.evalFile(file)
        buildList {
            add("=== PaniniVM Script Execution: ${file.name} ===")
            results.forEachIndexed { index, res ->
                add("Line ${index + 1}:")
                when (res) {
                    is ExecutionResult.Success -> {
                        add("  ✓ Result: ${res.value}")
                        add("  ↳ Operation: ${res.operation}")
                    }
                    is ExecutionResult.Failure -> {
                        add("  ✗ Error: ${res.error} - ${res.message}")
                    }
                    is ExecutionResult.NeedsInput -> {
                        add("  ? Needs input: ${res.message} (missing: ${res.missingKarakas})")
                    }
                    is ExecutionResult.Ambiguous -> {
                        add("  ? Ambiguous: ${res.message} (matches: ${res.matchingOperations})")
                    }
                }
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
                SubantaStemClass.guess(pratipadika)
            )
        )

        buildList {
            add("$vibhakti $vacana: ${result.final.surface}")
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
            if (sutra.nimittaScope != dev.panini.sutra.NimittaScope.UNKNOWN) add("nimittaScope=${sutra.nimittaScope}")
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
    "--sankhya" -> {
        val valueText = args.getOrNull(1)
            ?: error("Usage: --sankhya INTEGER [cardinal|ordinal] [--variants]")
        val value = valueText.toBigIntegerOrNull()
            ?: error("Invalid integer for --sankhya: $valueText")
        require(value >= BigInteger.ZERO) { "Sankhya must be non-negative: $value" }

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
    else -> listOf("Usage: --eval file.pvm | --paradigm राम | --derive राम SASTHI BAHUVACANA | --verb भू | --sankhya 23 [cardinal|ordinal] [--variants] | --sutra 7.1.54 | --coverage")
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

private fun MutableList<String>.addTrace(result: dev.panini.derivation.DerivationResult, includeRole: Boolean = false) {
    add("----------------------------------------")
    result.applications.forEach { app ->
        val prefix = if (includeRole) " [${app.role::class.simpleName}]" else ""
        add("${app.sutra}$prefix — ${app.explanation}")
        app.conflictTrace.forEach { add("  ↳ $it") }
    }
}
