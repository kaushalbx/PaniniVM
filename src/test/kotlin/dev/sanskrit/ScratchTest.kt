package dev.sanskrit

import dev.sanskrit.derivation.*
import dev.sanskrit.dhatupatha.PadaType
import kotlin.test.Test

class ScratchTest {

    @Test
    fun testDerivationTrace() {
        val requests = listOf(PadaType.PARASMAIPADA, PadaType.ATMANEPADA).flatMap { pada ->
            Purusha.entries.flatMap { purusha ->
                Vacana.entries.map { vacana ->
                    "SRAMBH LAT $pada $purusha $vacana" to
                        TingantaDerivationRequest("स्रम्भ्", vacana, purusha, Lakara.LAT, pada = pada)
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
