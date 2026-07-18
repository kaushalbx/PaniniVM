package dev.sanskrit

import dev.sanskrit.derivation.DerivationEngine
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.Purusha
import dev.sanskrit.derivation.TingantaDerivationRequest
import dev.sanskrit.derivation.Vacana
import dev.sanskrit.dhatupatha.PadaType
import kotlin.test.Test

class ScratchTest {
    @Test
    fun testDerivationTrace() {
        val request = TingantaDerivationRequest(
            "चुरँ",
            Vacana.EKAVACANA,
            Purusha.PRATHAMA,
            Lakara.LING,
            pada = PadaType.ATMANEPADA,
        )
        val result = DerivationEngine().derive(request.initialState())

        println("=== CURADI LING ATMANEPADA TRACE ===")
        result.applications.forEachIndexed { index, application ->
            println("${index + 1}. ${application.sutra}: ${application.before.surface} -> ${application.after.surface}")
        }
        println("FINAL (${result.final.stage}): ${result.final.surface}")
    }
}
