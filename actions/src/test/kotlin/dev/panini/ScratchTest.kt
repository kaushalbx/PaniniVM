package dev.panini

import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.Purusha
import dev.panini.core.Vacana
import dev.panini.derivation.TingantaDerivationRequest
import dev.panini.derivation.TingantaEngine
import kotlin.test.Test

class ScratchTest {
    @Test
    fun testDerivationTrace() {
        println("=== DIS LUNG TRACE ===")
        val disReq = TingantaDerivationRequest(
            "दिशँ",
            Vacana.EKAVACANA,
            Purusha.PRATHAMA,
            Lakara.LUNG,
            pada = PadaType.PARASMAIPADA,
        )
        val disResult = TingantaEngine().derive(disReq)
        disResult.applications.forEachIndexed { index, app ->
            println("${index + 1}. [${app.sutra}] ${app.explanation}")
        }
        println("FINAL: ${disResult.final.surface}")
    }
}
