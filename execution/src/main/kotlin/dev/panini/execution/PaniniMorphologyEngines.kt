package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.parser.PaniniParser

import dev.panini.ganapatha.GanaPatha

/**
 * 1.4.58 प्रादयः & 1.4.59 उपसर्गाः क्रियायोगे
 * AST-driven Upasarga and Verb Action Engine using PaniniParser and GanaPatha.
 */
object PradayaUpasargaEngine {

    private val parser = PaniniParser()

    fun isVerbAction(text: String, preParsedUkti: dev.panini.vyakaranam.ast.Ukti? = null): Boolean {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return false

        val ukti = preParsedUkti ?: runCatching { parser.parse(trimmed) }.getOrNull()
        if (ukti != null) {
            val hasTinganta = ukti.vakyas.flatMap { it.padas }.any { it is TingantaPada }
            if (hasTinganta) return true
        }

        val words = trimmed.split(Regex("""\s+"""))
        return words.any { word ->
            GanaPatha.isEligibleMember(4, word, emptySet()) || word == "कृ" || word.startsWith("कृ") || word.contains("लोट्")
        }
    }
}

/**
 * 1.4.23 कारके & 2.3.2 कर्मणि द्वितीया
 * AST-driven Subanta Kāraka Parameter Extractor using PaniniParser.
 */
object SubantaKarakaParser {

    private val parser = PaniniParser()

    /** Extracts accusative parameter stems from parsed subantas. */
    fun extractKarmaTerms(karmaText: String, preParsedUkti: dev.panini.vyakaranam.ast.Ukti? = null): List<String> {
        val trimmed = karmaText.trim()
        if (trimmed.isEmpty()) return emptyList()

        val ukti = preParsedUkti ?: parser.parseOrNull(trimmed) ?: return emptyList()
        return ukti.vakyas.asSequence()
            .flatMap { it.padas.asSequence() }
            .filterIsInstance<SubantaPada>()
            .filter { SupAffix.fromUpadesha(it.sup.text)?.vibhakti == Vibhakti.DVITIYA }
            .map { it.pratipadika.sourceText.trim() }
            .filter(String::isNotEmpty)
            .toList()
    }

    /**
     * Checks if text contains a Tritīyā Instrumental suffix (e.g. "+ टा").
     */
    fun hasTritiyaInstrumental(text: String, preParsedUkti: dev.panini.vyakaranam.ast.Ukti? = null): Boolean {
        val ukti = preParsedUkti ?: parser.parseOrNull(text) ?: return false
        return ukti.vakyas.asSequence()
            .flatMap { it.padas.asSequence() }
            .filterIsInstance<SubantaPada>()
            .any { SupAffix.fromUpadesha(it.sup.text)?.vibhakti == Vibhakti.TRTIYA }
    }
}

/**
 * Dynamic Niṣedha (Prohibition Sūtra) Condition Evaluator via PaniniParser AST.
 */
object DynamicNishedhaEvaluator {

    private val sankhyaEvaluator = SankhyaEvaluator()
    private val parser = PaniniParser()

    fun evaluateProhibition(guardText: String): Boolean {
        // Guards containing ordinal parameters are rewritten before evaluation, so the
        // rewritten sentence must be parsed instead of reusing its original AST.
        val ukti = parser.parseOrNull(guardText.trim()) ?: return false
        val isNishedhaSentence = ukti.vakyas.any { vakya ->
            vakya.padas.filterIsInstance<AvyayaPada>().any { it.form in NISHEDHA_MARKERS }
        }
        if (!isNishedhaSentence) return false

        val operands = SubantaKarakaParser.extractKarmaTerms(guardText, ukti)
            .mapNotNull(::evaluateNumber)
        return operands.size == 2 && operands[0] == operands[1]
    }

    private fun evaluateNumber(term: String): Long? =
        term.toLongOrNull()
            ?: runCatching { sankhyaEvaluator.evaluateStems(listOf(term)).value }.getOrNull()

    private val NISHEDHA_MARKERS = setOf("न", "मा")
}
