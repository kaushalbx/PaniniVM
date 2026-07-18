package dev.panini

import dev.panini.derivation.Lakara
import dev.panini.derivation.Purusha
import dev.panini.derivation.TingantaDerivationRequest
import dev.panini.derivation.TingantaEngine
import dev.panini.derivation.Vacana
import dev.panini.dhatupatha.PadaType
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
        val result = TingantaEngine().derive(request)

        println("=== CURADI LING ATMANEPADA TRACE ===")
        result.applications.forEachIndexed { index, application ->
            println("${index + 1}. ${application.sutra}: ${application.before.surface} -> ${application.after.surface}")
        }
        println("FINAL (${result.final.stage}): ${result.final.surface}")
    }
}
