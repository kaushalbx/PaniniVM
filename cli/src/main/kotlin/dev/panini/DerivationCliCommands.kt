package dev.panini

import dev.panini.core.Lakara
import dev.panini.core.Prayoga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.*
import dev.panini.unadipatha.UnadiDerivationEngine

internal object DerivationCliCommands {
    fun paradigm(args: List<String>): List<String> {
        val stem = args.getOrNull(0) ?: error("Usage: --paradigm राम")
        return SubantaEngine().deriveSupportedParadigm(stem).surfaces.map { (affix, surface) ->
            "${affix.vibhakti} ${affix.vacana}: $surface"
        }
    }

    fun nominal(args: List<String>): List<String> {
        val stem = args.getOrNull(0) ?: error("Usage: --derive राम SASTHI BAHUVACANA")
        val vibhakti = CliArgumentParsers.vibhakti(args.getOrNull(1) ?: error("Missing vibhakti."))
        val vacana = CliArgumentParsers.vacana(args.getOrNull(2) ?: error("Missing vacana."))
        val result = SubantaEngine().derive(SubantaDerivationRequest(stem, vibhakti, vacana))
        return buildList {
            add("$vibhakti $vacana: ${result.final.surface}")
            DerivationTraceRenderer.appendTo(this, result, includeRole = true)
        }
    }

    fun karaka(args: List<String>): List<String> {
        val stem = args.getOrNull(0) ?: error("Usage: --derive-karaka <pratipadika> <karaka> <vacana> <dhatu> [prayoga]")
        val karaka = CliArgumentParsers.karaka(args.getOrNull(1) ?: error("Missing karaka."))
        val vacana = CliArgumentParsers.vacana(args.getOrNull(2) ?: error("Missing vacana."))
        val dhatu = args.getOrNull(3) ?: error("Missing dhatu.")
        val prayoga = args.getOrNull(4)?.let(CliArgumentParsers::prayoga) ?: Prayoga.KARTARI
        val result = SubantaEngine().deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = stem,
                karaka = karaka,
                vacana = vacana,
                dhatu = dhatu,
                prayoga = prayoga,
            ),
        )
        return buildList {
            add("${result.initial.context.rupa.vibhakti ?: Vibhakti.PRATHAMA} $vacana: ${result.final.surface}")
            result.karakaResolution?.let { resolution ->
                add("Semantic Karaka Resolution Trace:")
                resolution.evidence.forEach { add("  ${it.sutra} — ${it.reason} (${it.text})") }
            }
            DerivationTraceRenderer.appendTo(this, result, includeRole = true)
        }
    }

    fun unadi(args: List<String>): List<String> {
        val dhatu = args.getOrNull(0) ?: error("Usage: --derive-unadi <dhatu> <pratyaya>")
        val pratyaya = args.getOrNull(1) ?: error("Usage: --derive-unadi <dhatu> <pratyaya>")
        val result = UnadiDerivationEngine.derive(dhatu, pratyaya)
        return buildList {
            add("=== Uṇādi Derivation Tracing for ($dhatu + $pratyaya) ===")
            add("Initial State: ${result.initial.terms.joinToString(" + ") { it.surface }}")
            add("Final Derived Surface: ${result.final.surface}")
            DerivationTraceRenderer.appendTo(this, result, includeRole = true)
        }
    }

    fun verb(args: List<String>): List<String> {
        val dhatu = args.getOrNull(0) ?: error("Usage: --verb भू [LAT|LRT|LOT|LANG|LING] [EKAVACANA|DVIVACANA|BAHUVACANA]")
        val requestedLakara = args.getOrNull(1)?.let(CliArgumentParsers::lakaraOrNull)
        val lakara = requestedLakara ?: Lakara.LAT
        val vacana = args.getOrNull(if (requestedLakara == null) 1 else 2)?.let(CliArgumentParsers::vacana) ?: Vacana.EKAVACANA
        val result = TingantaEngine().derive(TingantaDerivationRequest(dhatu, vacana, lakara = lakara))
        return buildList {
            add("$dhatu: ${result.final.surface}")
            DerivationTraceRenderer.appendTo(this, result)
        }
    }
}
