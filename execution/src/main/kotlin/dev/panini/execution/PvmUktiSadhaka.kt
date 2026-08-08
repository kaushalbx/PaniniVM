package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
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
        return scriptContent.lines().joinToString("\n") { line ->
            val trimmed = line.trim()
            when {
                trimmed.isEmpty() -> ""
                trimmed.startsWith("#") || trimmed.startsWith("//") -> line
                else -> {
                    val commentIdx = when {
                        trimmed.contains("#") && trimmed.contains("//") -> minOf(trimmed.indexOf('#'), trimmed.indexOf("//"))
                        trimmed.contains("#") -> trimmed.indexOf('#')
                        trimmed.contains("//") -> trimmed.indexOf("//")
                        else -> -1
                    }
                    val codePart = if (commentIdx != -1) trimmed.substring(0, commentIdx).trim() else trimmed
                    val commentPart = if (commentIdx != -1) line.substring(line.indexOf(if (trimmed.contains('#')) '#' else '/')) else ""

                    if (codePart.isEmpty()) {
                        line
                    } else {
                        val hasDanda = codePart.endsWith("।") || codePart.endsWith("॥") || codePart.contains("।") || codePart.contains("॥")
                        var surface = try { sadhayaLine(codePart) } catch (_: Throwable) { codePart }
                        if (!hasDanda) {
                            surface = surface.replace("॥", "").replace("।", "").replace(Regex("\\s+"), " ").trim()
                        }
                        if (commentPart.isNotEmpty()) "$surface $commentPart" else surface
                    }
                }
            }
        }
    }

    fun sadhayaLine(lineText: String): String {
        val ukti = parser.parse(lineText)
        val parts = mutableListOf<String>()

        val dandaDelimiter = when {
            lineText.trim().endsWith("॥") -> "॥"
            else -> "।"
        }

        ukti.sambodhana?.let { sambodhana ->
            val header = sambodhana.suchaka?.let { "$it " } ?: ""
            val derivedSub = sadhayaSubanta(sambodhana.subanta)
            parts += "$header$derivedSub,"
        }

        fun vakyaText(index: Int): String =
            ukti.vakyas[index].padas.joinToString(" ") { pada -> sadhayaPada(pada) }

        when (val structure = ukti.structure) {
            UktiStructure.Sequence -> ukti.vakyas.indices.forEach { index ->
                val delim = if (index == ukti.vakyas.lastIndex) dandaDelimiter else "।"
                parts += "${vakyaText(index)} $delim"
            }
            is UktiStructure.Conditional -> {
                val alternate = if (structure.hasAlternate) " अन्यथा ${vakyaText(2)}" else ""
                parts += "यदि ${vakyaText(0)} तर्हि ${vakyaText(1)}$alternate $dandaDelimiter"
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
            val req = SubantaDerivationRequest(baseText, supAffix.vibhakti, supAffix.vacana)
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
            val req = SubantaDerivationRequest(baseText, supAffix.vibhakti, supAffix.vacana)
            subantaEngine.derive(req).final.surface
        } catch (_: Throwable) {
            pada.sourceText
        }
    }

    fun sadhayaSankhyaAbhyasa(pada: SankhyaAbhyasaPada): String {
        return try {
            val lastStem = pada.stems.lastOrNull() ?: return pada.sourceText
            val numStems = PvmAbhyasaMorphology.numericStems(pada.stems)
            val count = if (numStems.isNotEmpty()) {
                sankhyaEvaluator.evaluateStems(numStems).value
            } else {
                sankhyaEvaluator.evaluateStems(pada.stems).value
            }
            val cardinalSurface = sankhyaGenerator.cardinal(count).final.surface
            PvmAbhyasaMorphology.surface(lastStem, count, cardinalSurface)
        } catch (_: Throwable) {
            pada.sourceText
        }
    }

    fun sadhayaSubanta(subanta: SubantaPada): String {
        val baseText = subanta.pratipadika.baseText()
        val supAffix = SupAffix.fromUpadesha(subanta.sup.text) ?: return baseText
        if (subanta.pratipadika is KridantaPratipadika) {
            pvmKridantaSurface(baseText, supAffix)?.let { return it }
        }
        val linga = PvmNominalLexicon.gender(baseText)
        return try {
            val req = SubantaDerivationRequest(baseText, supAffix.vibhakti, supAffix.vacana, linga)
            val res = subantaEngine.derive(req).final.surface
            PvmNominalLexicon.surface(baseText, res)
        } catch (e: Exception) {
            baseText
        }
    }

    /** Stable a-stem forms for the PVM's action/state krdantas. */
    private fun pvmKridantaSurface(stem: String, affix: SupAffix): String? {
        if (stem !in PvmKridantaLexicon.declinableStems) return null
        return when (affix) {
            SupAffix.AM -> "${stem}म्"
            SupAffix.NGE -> "${stem}ाय"
            SupAffix.NGAS -> "${stem}स्य"
            else -> null
        }
    }

    fun sadhayaTinganta(tinganta: TingantaPada): String {
        val rawDhatu = tinganta.dhatu.mulaDhatu
        val tingAffix = TingAffix.fromUpadesha(tinganta.ting.text) ?: return rawDhatu
        pvmImperativeSurface(tinganta)?.let { return it }
        return try {
            val useSanadiEngine = tingantaEngine.supportsSanadi(
                rawDhatu,
                tinganta.dhatu.sanadiPratyayas,
                tingAffix.pada,
            )
            val req = TingantaDerivationRequest(
                dhatu = rawDhatu,
                vacana = tingAffix.vacana,
                purusha = tingAffix.purusha,
                lakara = tinganta.lakara,
                pada = tingAffix.pada.takeIf { useSanadiEngine },
                sanadiPratyayas = tinganta.dhatu.sanadiPratyayas.takeIf { useSanadiEngine }.orEmpty(),
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

    /**
     * Forms used by the PVM instruction vocabulary whose derivational paths are
     * not yet complete in [TingantaEngine]. The engine does not yet complete
     * every sanādi formation, so these proven command
     * surfaces remain a narrow fallback while sanādi support migrates into it.
     */
    private fun pvmImperativeSurface(tinganta: TingantaPada): String? {
        if (tinganta.lakara != dev.panini.core.Lakara.LOT || tinganta.ting.text != "सिप्") return null

        val dhatu = tinganta.dhatu.mulaDhatu
        val hasNic = "णिच्" in tinganta.dhatu.sanadiPratyayas
        if (hasNic && tingantaEngine.supportsSanadi(dhatu, tinganta.dhatu.sanadiPratyayas, dev.panini.core.PadaType.PARASMAIPADA)) return null
        return if (hasNic) null else PvmImperativeLexicon.surface(dhatu)
    }

    private fun Pratipadika.baseText(): String = when (this) {
        is MulaPratipadika -> text
        is SankhyaPratipadika -> sourceText
        is KridantaPratipadika -> PvmKridantaLexicon.stem(dhatu.mulaDhatu, krtPratyaya)
        is UnadyantaPratipadika -> sourceText
        is SamasaPratipadika -> angas.joinToString("") { it.pratipadika.baseText() }
    }

}
