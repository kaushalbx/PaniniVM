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
import dev.panini.vyakaranam.ast.UktiStructure
import dev.panini.vyakaranam.parser.PaniniParser

/**
 * Pāninian grammatical sādhaka (उक्तिसाधक) using SubantaEngine, TingantaEngine,
 * and DerivationEngine to perform rupa-siddhi (रूपसिद्धि) on segmented PVM ASTs.
 */
class PvmUktiSadhaka(
    private val derivationEngine: DerivationEngine = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras),
    private val subantaEngine: SubantaEngine = SubantaEngine(derivationEngine),
    private val tingantaEngine: TingantaEngine = TingantaEngine(derivationEngine),
    private val parser: PaniniParser = PaniniParser(),
) {

    fun sadhayaScript(scriptContent: String): String {
        return PvmScript.parse(scriptContent).joinToString("\n") { statement ->
            sadhayaLine(statement.text)
        }
    }

    fun sadhayaLine(lineText: String): String {
        val ukti = parser.parse(lineText)
        val parts = mutableListOf<String>()

        ukti.sambodhana?.let { sambodhana ->
            val header = sambodhana.suchaka?.let { "$it " } ?: ""
            val derivedSub = sadhayaSubanta(sambodhana.subanta)
            parts += "$header$derivedSub,"
        }

        fun vakyaText(index: Int): String =
            ukti.vakyas[index].padas.joinToString(" ") { pada -> sadhayaPada(pada) }

        when (val structure = ukti.structure) {
            UktiStructure.Sequence -> ukti.vakyas.indices.forEach { index ->
                parts += "${vakyaText(index)} ।"
            }
            is UktiStructure.Conditional -> {
                val alternate = if (structure.hasAlternate) " अन्यथा ${vakyaText(2)}" else ""
                parts += "यदि ${vakyaText(0)} तर्हि ${vakyaText(1)}$alternate ।"
            }
        }

        return parts.joinToString(" ")
    }

    private val sankhyaEvaluator = SankhyaEvaluator()
    private val sankhyaGenerator = SankhyaGenerator()

    fun sadhayaPada(pada: Pada): String = when (pada) {
        is SubantaPada -> sadhayaSubanta(pada)
        is SamuccitaSubanta -> pada.members.joinToString(" ") { sadhayaSubanta(it) } + " च"
        is TingantaPada -> sadhayaTinganta(pada)
        is AvyayaPada -> pada.form
        is SankhyaPada -> sadhayaSankhya(pada)
        is SankhyaPuranaPada -> sadhayaSankhyaPurana(pada)
        is SankhyaAbhyasaPada -> sadhayaSankhyaAbhyasa(pada)
        is KatapayadiPada -> pada.sourceText
        is AryabhatiyaPada -> pada.sourceText
        is BhutasamkhyaPada -> pada.sourceText
    }

    fun sadhayaSankhya(pada: SankhyaPada): String {
        return try {
            val expr = sankhyaEvaluator.evaluateStems(pada.stems)
            val baseText = sankhyaGenerator.cardinal(expr.value).final.surface
            val supAffix = SupAffix.fromUpadesha(pada.sup.text) ?: return baseText
            val stemClass = SubantaStemClass.guess(baseText)
            val req = SubantaDerivationRequest(baseText, supAffix.vibhakti, supAffix.vacana, stemClass)
            subantaEngine.derive(req).final.surface
        } catch (_: Throwable) {
            pada.sourceText
        }
    }

    fun sadhayaSankhyaPurana(pada: SankhyaPuranaPada): String {
        return try {
            val expr = sankhyaEvaluator.evaluateStems(pada.stems)
            val baseText = sankhyaGenerator.ordinal(expr.value).final.surface
            val supAffix = SupAffix.fromUpadesha(pada.sup.text) ?: return baseText
            val stemClass = SubantaStemClass.guess(baseText)
            val req = SubantaDerivationRequest(baseText, supAffix.vibhakti, supAffix.vacana, stemClass)
            subantaEngine.derive(req).final.surface
        } catch (_: Throwable) {
            pada.sourceText
        }
    }

    fun sadhayaSankhyaAbhyasa(pada: SankhyaAbhyasaPada): String {
        return try {
            val lastStem = pada.stems.lastOrNull() ?: return pada.sourceText
            val numStems = pada.stems.filter { it != "कृत्वः" && it != "कृत्वा" && it != "कृत्वसुच्" && it != "सुच्" && it != "धा" }
            val count = if (numStems.isNotEmpty()) {
                sankhyaEvaluator.evaluateStems(numStems).value
            } else {
                sankhyaEvaluator.evaluateStems(pada.stems).value
            }
            val cardinalSurface = sankhyaGenerator.cardinal(count).final.surface
            when (lastStem) {
                "कृत्वः", "कृत्वसुच्", "कृत्वा" -> "${cardinalSurface}कृत्वः"
                "सुच्" -> when (count) {
                    2L -> "द्विः"
                    3L -> "त्रिः"
                    4L -> "चतुः"
                    else -> "${cardinalSurface}कृत्वः"
                }
                "धा" -> "${cardinalSurface}धा"
                else -> "${cardinalSurface}कृत्वः"
            }
        } catch (_: Throwable) {
            pada.sourceText
        }
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
        is UnadyantaPratipadika -> {
            sourceText
        }
        is SamasaPratipadika -> angas.joinToString("-") { it.pratipadika.baseText() }
    }
}
