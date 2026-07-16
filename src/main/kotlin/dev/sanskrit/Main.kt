package dev.sanskrit

import dev.sanskrit.derivation.SubantaDerivationRequest
import dev.sanskrit.derivation.SubantaEngine
import dev.sanskrit.derivation.SubantaStemClass
import dev.sanskrit.derivation.Vacana
import dev.sanskrit.derivation.Vibhakti
import dev.sanskrit.derivation.TingantaDerivationRequest
import dev.sanskrit.derivation.TingantaEngine
import dev.sanskrit.derivation.Lakara
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
            if (sutra.nimittaScope != dev.sanskrit.sutra.NimittaScope.UNKNOWN) add("nimittaScope=${sutra.nimittaScope}")
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

private fun findLakara(value: String): Lakara? = Lakara.entries.firstOrNull {
    it.name == value.uppercase() || it.upadesha == value
}

private fun MutableList<String>.addTrace(result: dev.sanskrit.derivation.DerivationResult, includeRole: Boolean = false) {
    add("----------------------------------------")
    result.applications.forEach { app ->
        val prefix = if (includeRole) " [${app.role::class.simpleName}]" else ""
        add("${app.sutra}$prefix — ${app.explanation}")
        app.conflictTrace.forEach { add("  ↳ $it") }
    }
}
