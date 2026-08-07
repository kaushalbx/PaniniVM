package dev.panini.execution

import dev.panini.core.SupAffix
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

    /**
     * Extracts Karma (Accusative parameter terms) from text using AST parse or fallback.
     * e.g. "द्वि + अम् त्रि + अम् च" -> ["द्वि", "त्रि"]
     */
    fun extractKarmaTerms(karmaText: String, preParsedUkti: dev.panini.vyakaranam.ast.Ukti? = null): List<String> {
        val trimmed = karmaText.trim()
        if (trimmed.isEmpty()) return emptyList()

        val ukti = preParsedUkti ?: runCatching { parser.parse(trimmed) }.getOrNull()
        if (ukti != null) {
            val karmaStems = mutableListOf<String>()
            for (vakya in ukti.vakyas) {
                for (pada in vakya.padas) {
                    if (pada is SubantaPada && pada.sup.text == "अम्") {
                        val stem = pada.pratipadika.sourceText.trim()
                        if (stem.isNotEmpty()) {
                            karmaStems.add(stem)
                        }
                    }
                }
            }
            if (karmaStems.isNotEmpty()) return karmaStems
        }

        // Regex fallback for unparsed fragments
        val terms = mutableListOf<String>()
        val matches = Regex("""(\S+)\s*\+\s*अम्""").findAll(karmaText)
        for (match in matches) {
            terms += match.groupValues[1]
        }
        return terms
    }

    /**
     * Checks if text contains a Tritīyā Instrumental suffix (e.g. "+ टा").
     */
    fun hasTritiyaInstrumental(text: String, preParsedUkti: dev.panini.vyakaranam.ast.Ukti? = null): Boolean {
        val ukti = preParsedUkti ?: runCatching { parser.parse(text) }.getOrNull()
        if (ukti != null) {
            val hasTa = ukti.vakyas.flatMap { it.padas }
                .filterIsInstance<SubantaPada>()
                .any { it.sup.text == "टा" }
            if (hasTa) return true
        }
        return text.contains("+ टा")
    }
}

/**
 * Dynamic Niṣedha (Prohibition Sūtra) Condition Evaluator via PaniniParser AST.
 */
object DynamicNishedhaEvaluator {

    private val sankhyaEvaluator = SankhyaEvaluator()
    private val parser = PaniniParser()

    fun evaluateProhibition(guardText: String, argTerms: List<String>, preParsedUkti: dev.panini.vyakaranam.ast.Ukti? = null): Boolean {
        val trimmed = guardText.trim()

        val ukti = preParsedUkti ?: runCatching { parser.parse(trimmed) }.getOrNull()
        val isNishedhaSentence = ukti?.vakyas?.any { vakya ->
            vakya.padas.filterIsInstance<AvyayaPada>().any { it.sourceText == "न" || it.sourceText == "मा" }
        } ?: trimmed.startsWith("न ")

        if (!isNishedhaSentence && !trimmed.contains("शून्य")) return false

        // Evaluates prohibition guard conditions
        val evaluatedValues = argTerms.map { term ->
            term.toLongOrNull()
                ?: runCatching { sankhyaEvaluator.evaluateStems(listOf(term)).value }.getOrNull()
                ?: -1L
        }

        // Rule 1: Zero parameter prohibition check (0 / शून्य)
        if (trimmed.contains("शून्य") || trimmed.contains("०")) {
            if (evaluatedValues.any { it == 0L } || argTerms.any { it == "शून्य" || it == "०" }) {
                return true
            }
        }

        // Rule 2: Dynamic equality prohibition check
        if (evaluatedValues.size >= 2 && evaluatedValues[0] != -1L && evaluatedValues[1] != -1L) {
            if (trimmed.contains("तुल्य") || trimmed.contains("समान")) {
                if (evaluatedValues[0] == evaluatedValues[1]) return true
            }
        }

        return false
    }
}
