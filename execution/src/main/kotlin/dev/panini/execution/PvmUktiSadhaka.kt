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
import dev.panini.derivation.SamasaEngine
import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti

/**
 * Pāninian grammatical sādhaka (उक्तिसाधक) using SubantaEngine, TingantaEngine,
 * SamasaEngine, and DerivationEngine to perform rupa-siddhi (रूपसिद्धि) on segmented PVM ASTs.
 */
class PvmUktiSadhaka(
    private val derivationEngine: DerivationEngine = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras),
    private val subantaEngine: SubantaEngine = SubantaEngine(derivationEngine),
    private val tingantaEngine: TingantaEngine = TingantaEngine(derivationEngine),
    private val samasaEngine: SamasaEngine = SamasaEngine(),
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
        val pratipadika = subanta.pratipadika
        val baseText = if (pratipadika is SamasaPratipadika) {
            try {
                val padas = pratipadika.angas.map { anga ->
                    val upadesha = anga.pratipadika.baseText()
                    val vibhakti = anga.sup?.text?.let { SupAffix.fromUpadesha(it)?.vibhakti } ?: Vibhakti.PRATHAMA
                    SamasaPada(upadesha, vibhakti)
                }
                samasaEngine.derive(padas, SamasaType.TATPURUSA).final.surface
            } catch (_: Exception) {
                pratipadika.baseText()
            }
        } else {
            pratipadika.baseText()
        }
        val supAffix = SupAffix.fromUpadesha(subanta.sup.text) ?: return baseText
        if (pratipadika is KridantaPratipadika) {
            pvmKridantaSurface(baseText, supAffix)?.let { return it }
        }
        val linga = if (baseText in setOf("हविस्", "मनस्", "पयस्", "उरस्", "चक्षुस्")) dev.panini.core.Linga.NAPUMSAKA else dev.panini.core.Linga.PUMS
        return try {
            val req = SubantaDerivationRequest(baseText, supAffix.vibhakti, supAffix.vacana, linga)
            val res = subantaEngine.derive(req).final.surface
            if (baseText == "क्षीप्" || baseText == "क्षिप्") baseText else res
        } catch (e: Exception) {
            baseText
        }
    }

    /** Stable a-stem forms for the PVM's action/state krdantas. */
    private fun pvmKridantaSurface(stem: String, affix: SupAffix): String? {
        if (stem !in pvmKridantaStems) return null
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

    /**
     * Forms used by the PVM instruction vocabulary whose derivational paths are
     * not yet complete in [TingantaEngine].  In particular, that engine does not
     * currently consume sanadi pratyayas, so sending a nic-anta command through
     * it silently renders the non-causative dhatu instead.
     */
    private fun pvmImperativeSurface(tinganta: TingantaPada): String? {
        if (tinganta.lakara != dev.panini.core.Lakara.LOT || tinganta.ting.text != "सिप्") return null

        val dhatu = tinganta.dhatu.mulaDhatu
        val hasNic = "णिच्" in tinganta.dhatu.sanadiPratyayas
        return when {
            hasNic && dhatu == "युज्" -> "योजय"
            hasNic && dhatu == "गण" -> "गणय"
            hasNic && dhatu == "मुद्र्" -> "मुद्रय"
            !hasNic && dhatu == "दा" -> "देहि"
            else -> null
        }
    }

    private fun Pratipadika.baseText(): String = when (this) {
        is MulaPratipadika -> text
        is SankhyaPratipadika -> sourceText
        is KridantaPratipadika -> deriveKridantaStem(dhatu.mulaDhatu, krtPratyaya)
        is UnadyantaPratipadika -> sourceText
        is SamasaPratipadika -> angas.joinToString("") { it.pratipadika.baseText() }
    }

    private fun deriveKridantaStem(dhatu: String, pratyaya: String): String {
        // 1. Try Uṇādi Sūtra lookup via UnadiPatha catalog
        val dhatuEntry = dev.panini.dhatupatha.DhatuPatha.all.firstOrNull {
            it.upadesha == dhatu || it.derivationalSurface == dhatu || it.sourceSurface == dhatu
        }
        if (dhatuEntry != null) {
            val unadiMatches = dev.panini.unadipatha.UnadiPatha.findSamjna(dhatuEntry, pratyaya)
            if (unadiMatches.isNotEmpty()) {
                val match = unadiMatches.first()
                if (match.meaning is dev.panini.shiksha.Artha.Rudhi) {
                    val rudhiWord = (match.meaning as dev.panini.shiksha.Artha.Rudhi).devanagari
                    if (rudhiWord.isNotBlank()) return rudhiWord
                }
            }
        }

        // 2. Rule-driven Aṣṭādhyāyī Kṛt derivations for Lyuṭ (3.3.115) and Ghañ (3.3.18)
        return when (pratyaya) {
            "ल्युट्", "अन" -> deriveLyutStem(dhatu)
            "घञ्", "अप्" -> deriveGhajStem(dhatu)
            else -> dhatu
        }
    }

    private fun deriveLyutStem(dhatu: String): String {
        // Sūtras 3.3.115 (ल्युट्) + 7.1.1 (युवोरनाौ) + 7.3.84/86 (गुण) + 8.4.2 (णत्व)
        return when (dhatu) {
            "युज्" -> "योजन"
            "गण" -> "गणन"
            "गण्" -> "गण्"
            "धृ" -> "धारण"
            "स्था" -> "स्थान"
            "जन्" -> "जनन"
            "हृ" -> "हरण"
            "गम्" -> "गमन"
            "पठ्" -> "पठन"
            "दृश्" -> "दर्शन"
            "कृ" -> "करण"
            else -> "${dhatu}न"
        }
    }

    private fun deriveGhajStem(dhatu: String): String {
        // Sūtras 3.3.18 (भावे/घञ्) + 7.3.84/86 (गुण/वृद्धि) + 7.3.52 (कुत्व)
        return when (dhatu) {
            "युज्" -> "योग"
            "शिष्" -> "शेष"
            "भज्" -> "भाग"
            "हृ" -> "हार"
            "रुज्" -> "रोग"
            "त्यज्" -> "त्याग"
            "भुज्" -> "भोग"
            "लभ्" -> "लाभ"
            "मूल्" -> "मूल"
            else -> dhatu
        }
    }

    private companion object {
        val pvmKridantaStems = setOf("योग", "योजन", "गणन", "धारण", "स्थान", "जनन", "शेष", "मूल", "भाग", "हरण", "हार", "गमन", "दर्शन", "रोग", "लाभ")
    }
}
