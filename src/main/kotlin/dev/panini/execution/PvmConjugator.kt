package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
import dev.panini.derivation.SubantaStemClass
import dev.panini.derivation.TingantaDerivationRequest
import dev.panini.derivation.TingantaEngine
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.UnadyantaPratipadika
import dev.panini.vyakaranam.parser.PaniniParser

/**
 * Uses SubantaEngine, TingantaEngine, and DerivationEngine to conjugate
 * segmented PVM utterances into fully derived surface text sentences.
 */
class PvmConjugator(
    private val derivationEngine: DerivationEngine = DerivationEngine(),
    private val subantaEngine: SubantaEngine = SubantaEngine(derivationEngine),
    private val tingantaEngine: TingantaEngine = TingantaEngine(derivationEngine),
    private val parser: PaniniParser = PaniniParser(),
) {

    fun conjugateScript(scriptContent: String): String {
        return scriptContent.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") && !it.startsWith("//") }
            .joinToString("\n") { line -> conjugateLine(line) }
    }

    fun conjugateLine(lineText: String): String {
        val ukti = parser.parse(lineText)
        val parts = mutableListOf<String>()

        ukti.sambodhana?.let { sambodhana ->
            val header = sambodhana.suchaka?.let { "$it " } ?: ""
            val conjugatedSub = conjugateSubanta(sambodhana.subanta)
            parts += "$header$conjugatedSub,"
        }

        ukti.vakyas.forEach { vakya ->
            val padasText = vakya.padas.joinToString(" ") { pada -> conjugatePada(pada) }
            parts += "$padasText ।"
        }

        return parts.joinToString(" ")
    }

    fun conjugatePada(pada: Pada): String = when (pada) {
        is SubantaPada -> conjugateSubanta(pada)
        is SamuccitaSubanta -> pada.members.joinToString(" ") { conjugateSubanta(it) } + " च"
        is TingantaPada -> conjugateTinganta(pada)
        is AvyayaPada -> pada.form
    }

    fun conjugateSubanta(subanta: SubantaPada): String {
        val baseText = subanta.pratipadika.baseText()
        val supAffix = SupAffix.fromUpadesha(subanta.sup.text) ?: return baseText
        val stemClass = SubantaStemClass.guess(baseText)
        return try {
            val req = SubantaDerivationRequest(baseText, supAffix.vibhakti, supAffix.vacana, stemClass)
            subantaEngine.derive(req).final.surface
        } catch (e: Exception) {
            baseText
        }
    }

    fun conjugateTinganta(tinganta: TingantaPada): String {
        val rawDhatu = tinganta.dhatu.mulaDhatu
        val tingAffix = TingAffix.fromUpadesha(tinganta.ting.text) ?: return rawDhatu
        return try {
            val req = TingantaDerivationRequest(
                dhatu = rawDhatu,
                vacana = tingAffix.vacana,
                purusha = tingAffix.purusha,
                lakara = tinganta.lakara,
            )
            val derived = tingantaEngine.derive(req).final.surface
            if (tinganta.upasargas.isNotEmpty()) {
                tinganta.upasargas.joinToString("") + derived
            } else {
                derived
            }
        } catch (e: Exception) {
            rawDhatu
        }
    }

    private fun Pratipadika.baseText(): String = when (this) {
        is MulaPratipadika -> text
        is KridantaPratipadika -> dhatu.mulaDhatu
        is UnadyantaPratipadika -> sourceText
        is SamasaPratipadika -> angas.joinToString("-") { it.pratipadika.baseText() }
    }
}
