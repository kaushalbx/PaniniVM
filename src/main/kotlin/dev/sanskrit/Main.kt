package dev.sanskrit

import dev.sanskrit.derivation.SubantaDerivationRequest
import dev.sanskrit.derivation.SubantaEngine
import dev.sanskrit.derivation.Vacana
import dev.sanskrit.derivation.Vibhakti
import dev.sanskrit.derivation.TingantaDerivationRequest
import dev.sanskrit.derivation.TingantaEngine
import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.sutra.Sutra

fun main(args: Array<String>) {
    runCli(args).forEach(::println)
}

internal fun runCli(args: Array<String>): List<String> = when (args.firstOrNull()) {
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
        val result = SubantaEngine().derive(SubantaDerivationRequest(pratipadika, vibhakti, vacana))
        
        buildList {
            add("$vibhakti $vacana: ${result.final.surface}")
            add("----------------------------------------")
            result.applications.forEach { app ->
                add("${app.sutra} [${app.role::class.simpleName}] — ${app.explanation}")
                if (app.conflictTrace.isNotEmpty()) {
                    app.conflictTrace.forEach { add("  ↳ $it") }
                }
            }
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
            if (sutra.nimittaScope != dev.sanskrit.sutra.NimittaScope.UNKNOWN) add("nimittaScope=${sutra.nimittaScope}")
            if (sutra.dependencies.isNotEmpty()) add("dependencies=${sutra.dependencies.joinToString()}")
            if (sutra.blocks.isNotEmpty()) add("blocks=${sutra.blocks.joinToString()}")
            if (sutra.restrictions.isNotEmpty()) add("restrictions=${sutra.restrictions.joinToString()}")
            if (sutra.exceptions.isNotEmpty()) add("exceptions=${sutra.exceptions.joinToString()}")
        }
    }
    "--verb" -> {
        val dhatu = args.getOrNull(1) ?: error("Usage: --verb भू [EKAVACANA|DVIVACANA|BAHUVACANA]")
        val vacana = args.getOrNull(2)?.let(::parseVacana) ?: Vacana.EKAVACANA
        val result = TingantaEngine().derive(TingantaDerivationRequest(dhatu, vacana))
        buildList {
            add("$dhatu: ${result.final.surface}")
            add("----------------------------------------")
            result.applications.forEach { app ->
                add("${app.sutra} — ${app.explanation}")
                if (app.conflictTrace.isNotEmpty()) {
                    app.conflictTrace.forEach { add("  ↳ $it") }
                }
            }
        }
    }
    "--coverage" -> listOf(
        "loaded=${Ashtadhyayi.pathitaCount}; executable=${Ashtadhyayi.kriyavatCount}; total=${Ashtadhyayi.expectedSutraCount}; remaining=${Ashtadhyayi.remainingCount}",
        "roles=" + Ashtadhyayi.registry.sutras.groupingBy { it.role::class.simpleName }.eachCount().entries.joinToString { "${it.key}=${it.value}" },
    )
    else -> listOf("Usage: --paradigm राम | --derive राम SASTHI BAHUVACANA | --verb भू | --sutra 7.1.54 | --coverage")
}

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
