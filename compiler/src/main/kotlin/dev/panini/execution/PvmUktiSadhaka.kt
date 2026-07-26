package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
import dev.panini.derivation.SubantaStemClass
import dev.panini.derivation.TingantaDerivationRequest
import dev.panini.derivation.TingantaEngine
import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.UnadyantaPratipadika
import dev.panini.vyakaranam.parser.PaniniParser

/**
 * Pāninian grammatical sādhaka (उक्तिसाधक) using SubantaEngine, TingantaEngine,
 * and DerivationEngine to perform rupa-siddhi (रूपसिद्धि) on segmented PVM ASTs.
 */
class PvmUktiSadhaka(
    private val derivationEngine: DerivationEngine = DerivationEngine(),
    private val subantaEngine: SubantaEngine = SubantaEngine(derivationEngine),
    private val tingantaEngine: TingantaEngine = TingantaEngine(derivationEngine),
    private val parser: PaniniParser = PaniniParser(),
) {

    fun sadhayaScript(scriptContent: String): String {
        return scriptContent.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") && !it.startsWith("//") }
            .joinToString("\n") { line -> sadhayaLine(line) }
    }

    fun sadhayaLine(lineText: String): String {
        val ukti = parser.parse(lineText)
        val parts = mutableListOf<String>()

        ukti.sambodhana?.let { sambodhana ->
            val header = sambodhana.suchaka?.let { "$it " } ?: ""
            val derivedSub = sadhayaSubanta(sambodhana.subanta)
            parts += "$header$derivedSub,"
        }

        ukti.vakyas.forEach { vakya ->
            val padasText = vakya.padas.joinToString(" ") { pada -> sadhayaPada(pada) }
            parts += "$padasText ।"
        }

        return parts.joinToString(" ")
    }

    fun sadhayaPada(pada: Pada): String = when (pada) {
        is SubantaPada -> sadhayaSubanta(pada)
        is SamuccitaSubanta -> pada.members.joinToString(" ") { sadhayaSubanta(it) } + " च"
        is TingantaPada -> sadhayaTinganta(pada)
        is AvyayaPada -> pada.form
        is SankhyaPada -> pada.sourceText
        is SankhyaPuranaPada -> pada.sourceText
        is SankhyaAbhyasaPada -> pada.sourceText
        is KatapayadiPada -> pada.sourceText
        is AryabhatiyaPada -> pada.sourceText
        is BhutasamkhyaPada -> pada.sourceText
    }

    fun sadhayaSubanta(subanta: SubantaPada): String {
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

    fun sadhayaTinganta(tinganta: TingantaPada): String {
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
        is SankhyaPratipadika -> sourceText
        is KridantaPratipadika -> dhatu.mulaDhatu
        is UnadyantaPratipadika -> sourceText
        is SamasaPratipadika -> angas.joinToString("-") { it.pratipadika.baseText() }
    }
}
