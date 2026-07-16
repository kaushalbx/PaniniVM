package dev.sanskrit

import dev.sanskrit.derivation.*
import kotlin.test.Test

class ScratchTest {

    @Test
    fun testDerivationTrace() {
        val requests = Purusha.entries.flatMap { purusha ->
            Vacana.entries.map { vacana ->
                "SU LAT $purusha $vacana" to TingantaDerivationRequest("षुञ्", vacana, purusha, Lakara.LAT)
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
