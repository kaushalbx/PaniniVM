package dev.sanskrit

import dev.sanskrit.derivation.*
import dev.sanskrit.dhatupatha.PadaType
import kotlin.test.Test

class ScratchTest {

    @Test
    fun testDerivationTrace() {
        Lakara.entries.forEach { lakara ->
            listOf(PadaType.PARASMAIPADA, PadaType.ATMANEPADA).forEach { pada ->
                try {
                    val paradigm = TingantaEngine().deriveSupportedParadigm("स्रम्भ्", pada = pada, lakara = lakara)
                    println("=== SRAMBH $lakara $pada ===")
                    paradigm.forms.forEach { (affix, result) ->
                        println("$affix (${result.final.stage}): ${result.final.surface}")
                    }
                } catch (exception: IllegalArgumentException) {
                    println("=== SRAMBH $lakara $pada FAILED ===")
                    println(exception.message)
                }
            }
        }
        val request = TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.UTTAMA, Lakara.LING)
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.derivationalSurface == request.dhatu }
        val result = DerivationEngine().derive(request.initialState(dhatu))
        println("=== BHU LING MIP TRACE ===")
        result.applications.forEachIndexed { index, application ->
            println("${index + 1}. ${application.sutra}: ${application.before.surface} -> ${application.after.surface}")
        }
        println("FINAL (${result.final.stage}): ${result.final.surface}")
    }
}
