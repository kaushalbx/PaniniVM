package dev.sanskrit

import dev.sanskrit.derivation.*
import kotlin.test.Test

class ScratchTest {

    @Test
    fun testDerivationTrace() {
        val requests = listOf(dev.sanskrit.dhatupatha.PadaType.PARASMAIPADA, dev.sanskrit.dhatupatha.PadaType.ATMANEPADA).flatMap { pada ->
            Purusha.entries.flatMap { purusha ->
                Vacana.entries.map { vacana ->
                    "KRI LAT $pada $purusha $vacana" to TingantaDerivationRequest(
                        "डुक्रीञ्", vacana, purusha, Lakara.LAT, pada = pada,
                    )
                }
            }
        }
        requests.forEach { (label, request) ->
            val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first {
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
