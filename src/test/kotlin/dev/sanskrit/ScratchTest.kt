package dev.sanskrit

import dev.sanskrit.derivation.*
import dev.sanskrit.dhatupatha.DhatuPatha
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.dhatupatha.PadaType
import kotlin.test.Test

class ScratchTest {

    /**
     * Prints the full derivation trace for every gaṇa × {LOT, LANG, LING} paradigm.
     * Run with:  ./gradlew test --tests "dev.sanskrit.ScratchTest.traceGanaLakaraParadigms" --info
     */
    @Test
    fun traceGanaLakaraParadigms() {
        // Representative root for each gaṇa (Parasmaipada preferred)
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
        val engine  = TingantaEngine()

        for ((gana, upadesha) in ganaRoots) {
            val dhatu = DhatuPatha.all.first {
                it.gana == gana && (it.upadesha == upadesha || it.derivationalSurface == upadesha)
            }
            val pada = if (dhatu.pada == PadaType.ATMANEPADA) PadaType.ATMANEPADA else PadaType.PARASMAIPADA

            for (lakara in lakaras) {
                println("\n========================================")
                println("  $gana  |  ${dhatu.upadesha}  |  $lakara  |  $pada")
                println("========================================")
                try {
                    val paradigm = engine.deriveSupportedParadigm(dhatu.upadesha, pada, lakara)
                    paradigm.forms.forEach { (affix, result) ->
                        val sutras = result.applications.map { it.sutra }.joinToString(" -> ")
                        println("  ${affix.name.padEnd(8)} = ${result.final.surface.padEnd(20)}  [$sutras]")
                    }
                } catch (e: Exception) {
                    println("  ERROR: ${e.message}")
                }
            }
        }
    }

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
                println("${index + 1}. ${application.sutra}: ${application.before.surface} -> ${application.after.surface}")
            }
            println("FINAL (${result.final.stage}): ${result.final.surface}")
        }
    }
}
