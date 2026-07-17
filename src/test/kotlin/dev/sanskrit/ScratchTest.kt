package dev.sanskrit

import dev.sanskrit.derivation.*
import dev.sanskrit.dhatupatha.DhatuPatha
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.dhatupatha.PadaType
import kotlin.test.Test

class ScratchTest {

    /**
     * Surface-only summary: all 10 gaṇas × {LOT, LANG, LING}, Parasmaipada.
     *
     * Run with:
     *   ./gradlew test --tests "dev.sanskrit.ScratchTest.traceGanaLakaraParadigms" --info
     * then look at build/test-results/test/TEST-dev.sanskrit.ScratchTest.xml for stdout.
     */
    @Test
    fun traceGanaLakaraParadigms() {
        val ganaRoots: List<Pair<Gana, String>> = listOf(
            Gana.BHVADI    to "भू",
            Gana.ADADI     to "अद्",
            Gana.JUHOTYADI to "हु",
            Gana.DIVADI    to "दिव्",
            Gana.SVADI     to "षुञ्",
            Gana.TUDADI    to "तुद्",
            Gana.RUDHADI   to "रुधिँर्",
            Gana.TANADI    to "तनुँ",
            Gana.KRYADI    to "डुक्रीञ्",
            Gana.CURADI    to "चुरँ",
        )
        val lakaras = listOf(Lakara.LOT, Lakara.LANG, Lakara.LING)
        val engine = TingantaEngine()

        for ((gana, upadesha) in ganaRoots) {
            val dhatu = DhatuPatha.all.first {
                it.gana == gana && (it.upadesha == upadesha || it.derivationalSurface == upadesha)
            }
            val pada = if (dhatu.pada == PadaType.ATMANEPADA) PadaType.ATMANEPADA else PadaType.PARASMAIPADA

            for (lakara in lakaras) {
                println("\n════════════════════════════════════════════")
                println("  $gana  │  ${dhatu.upadesha}  │  $lakara  │  $pada")
                println("════════════════════════════════════════════")
                try {
                    val paradigm = engine.deriveSupportedParadigm(dhatu.upadesha, pada, lakara)
                    paradigm.forms.forEach { (affix, result) ->
                        println("  ${affix.name.padEnd(8)} = ${result.final.surface}")
                    }
                } catch (e: Exception) {
                    println("  ERROR: ${e.message}")
                }
            }
        }
    }

    /**
     * Step-by-step derivation trace for specific (root, pada, lakara, affix) targets.
     * Edit the `targets` list to focus on any form of interest.
     *
     * Run with:
     *   ./gradlew test --tests "dev.sanskrit.ScratchTest.traceStepByStep" --info
     */
    @Test
    fun traceStepByStep() {
        data class Target(
            val label: String,
            val upadesha: String,
            val pada: PadaType,
            val lakara: Lakara,
            val purusha: Purusha,
            val vacana: Vacana,
        )

        val targets = listOf(
            // Curādi LOT — the 8.4.2 false-match we just fixed
            Target("CURADI LOT TIP",  "चुरँ", PadaType.PARASMAIPADA, Lakara.LOT,  Purusha.PRATHAMA, Vacana.EKAVACANA),
            Target("CURADI LOT MIP",  "चुरँ", PadaType.PARASMAIPADA, Lakara.LOT,  Purusha.UTTAMA,   Vacana.EKAVACANA),
            Target("CURADI LANG TIP", "चुरँ", PadaType.PARASMAIPADA, Lakara.LANG, Purusha.PRATHAMA, Vacana.EKAVACANA),
            Target("CURADI LING TIP", "चुरँ", PadaType.PARASMAIPADA, Lakara.LING, Purusha.PRATHAMA, Vacana.EKAVACANA),
            // Rudhādi LOT — nasal strong/weak selection
            Target("RUDHADI LOT TIP", "रुधिँर्", PadaType.PARASMAIPADA, Lakara.LOT, Purusha.PRATHAMA, Vacana.EKAVACANA),
            Target("RUDHADI LOT MIP", "रुधिँर्", PadaType.PARASMAIPADA, Lakara.LOT, Purusha.UTTAMA,   Vacana.EKAVACANA),
            Target("RUDHADI LOT JHI", "रुधिँर्", PadaType.PARASMAIPADA, Lakara.LOT, Purusha.PRATHAMA, Vacana.BAHUVACANA),
            // Kryādi LING — was giving error for TAS
            Target("KRYADI LING TIP", "डुक्रीञ्", PadaType.PARASMAIPADA, Lakara.LING, Purusha.PRATHAMA, Vacana.EKAVACANA),
            Target("KRYADI LING TAS", "डुक्रीञ्", PadaType.PARASMAIPADA, Lakara.LING, Purusha.PRATHAMA, Vacana.DVIVACANA),
        )

        val engine = DerivationEngine()

        targets.forEach { t ->
            val dhatu = DhatuPatha.all.first {
                it.upadesha == t.upadesha || it.derivationalSurface == t.upadesha
            }
            val request = TingantaDerivationRequest(t.upadesha, t.vacana, t.purusha, t.lakara, pada = t.pada)
            println("\n╔══════════════════════════════════════════════════╗")
            println("  ${t.label}")
            println("╚══════════════════════════════════════════════════╝")
            try {
                val result = engine.derive(request.initialState(dhatu))
                result.applications.forEachIndexed { i, app ->
                    val before = app.before.surface.padEnd(20)
                    val after  = app.after.surface.padEnd(20)
                    println("  ${(i + 1).toString().padStart(3)}. ${app.sutra.padEnd(8)} $before → $after")
                }
                println("  ──────────────────────────────────────────────────")
                println("  FINAL (${result.final.stage}): ${result.final.surface}")
            } catch (e: Exception) {
                println("  ERROR: ${e.message}")
            }
        }
    }

    /**
     * Full Kryādi LOT paradigm — both Parasmaipada and Ātmanepada.
     * Kept for reference / regression.
     */
    @Test
    fun testDerivationTrace() {
        val requests = listOf(PadaType.PARASMAIPADA, PadaType.ATMANEPADA).flatMap { pada ->
            Purusha.entries.flatMap { purusha ->
                Vacana.entries.map { vacana ->
                    "KRI LOT $pada $purusha $vacana" to TingantaDerivationRequest(
                        "डुक्रीञ्", vacana, purusha, Lakara.LOT, pada = pada,
                    )
                }
            }
        }
        requests.forEach { (label, request) ->
            val dhatu = DhatuPatha.all.first {
                it.upadesha == request.dhatu || it.derivationalSurface == request.dhatu
            }
            val result = DerivationEngine().derive(request.initialState(dhatu))
            println("=== $label ===")
            result.applications.forEachIndexed { index, application ->
                println("  ${index + 1}. ${application.sutra}: ${application.before.surface} → ${application.after.surface}")
            }
            println("  FINAL (${result.final.stage}): ${result.final.surface}")
        }
    }
}
