package dev.panini.derivation

import dev.panini.core.*
import java.io.File
import kotlin.test.Test

/** Scores every person and number against standard paradigms, and fails on any wrong form. */
class TingantaParadigmReportTest {
    @Test fun `report verb paradigms`() {
        val engine = TingantaEngine()
        val sb = StringBuilder(); var right = 0; var wrong = 0; var errors = 0
        requireNotNull(javaClass.getResourceAsStream("/tinganta_paradigms.txt")).bufferedReader(Charsets.UTF_8).readLines()
            .filter { it.isNotBlank() && !it.startsWith("#") }.forEach { line ->
                val (root, gana, pada, lakara, forms) = line.split("|")
                val want = forms.split(" ").iterator(); var ok = 0; val bad = StringBuilder()
                for (p in listOf(Purusha.PRATHAMA, Purusha.MADHYAMA, Purusha.UTTAMA)) for (n in listOf(Vacana.EKAVACANA, Vacana.DVIVACANA, Vacana.BAHUVACANA)) {
                    val w = want.next()
                    val got = try {
                        engine.derive(TingantaDerivationRequest(dhatu = root, vacana = n, purusha = p, lakara = Lakara.valueOf(lakara),
                            pada = if (pada == "P") PadaType.PARASMAIPADA else PadaType.ATMANEPADA, gana = DhatuGana.valueOf(gana))).final.surface
                    } catch (e: Throwable) { "ERR" }
                    when { got in w.split("/") -> { right++; ok++ }
                        got == "ERR" -> { errors++; bad.append(" $w=ERR") }
                        else -> { wrong++; bad.append(" $w=$got") } }
                }
                sb.append("$root $lakara $ok/9$bad\n")
            }
        File("build").mkdirs()
        File("build/paradigm.txt").writeText("right $right wrong $wrong errors $errors\n$sb", Charsets.UTF_8)
        kotlin.test.assertEquals(0, wrong + errors, sb.toString())
    }
}
