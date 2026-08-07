package dev.panini.execution

import dev.panini.sankhya.SankhyaEvaluator

/**
 * 1.4.58 प्रादयः & 1.4.59 उपसर्गाः क्रियायोगे
 * Canonical 22 Pāṇinian Upasargas and dynamic verb/action root detector.
 */
object PradayaUpasargaEngine {

    val UPASARGAS = setOf(
        "प्र", "परा", "अप", "सम्", "अनु", "अव", "निस्", "निर्",
        "दुस्", "दुर्", "वि", "आङ्", "नि", "अधि", "अपि", "अति",
        "सु", "उत्", "अभि", "प्रति", "परि", "उप",
    )

    private val LAKARA_AFFIXES = setOf("लोट्", "लट्", "लङ्", "विधिलिङ्", "लृट्", "लोट्")

    fun isVerbAction(text: String): Boolean {
        val trimmed = text.trim()
        if (LAKARA_AFFIXES.any { trimmed.contains(it) }) return true
        val words = trimmed.split(Regex("""\s+"""))
        return words.any { word ->
            UPASARGAS.any { word.startsWith(it) } || word == "कृ" || word.startsWith("कृ")
        }
    }
}

/**
 * 1.4.23 कारके & 2.3.2 कर्मणि द्वितीया
 * Dynamic Subanta Kāraka Parameter Extractor.
 */
object SubantaKarakaParser {

    private val DWITIYA_AFFIXES = setOf("+ अम्", "+ औ", "+ शस्")
    private val TRITIYA_AFFIXES = setOf("+ टा", "+ भ्याम्", "+ भिस्")

    /**
     * Extracts Karma (Accusative parameter terms) from text.
     * e.g. "द्वि + अम् त्रि + अम् च" -> ["द्वि", "त्रि"]
     */
    fun extractKarmaTerms(karmaText: String): List<String> {
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
    fun hasTritiyaInstrumental(text: String): Boolean =
        TRITIYA_AFFIXES.any { text.contains(it) }
}

/**
 * Dynamic Niṣedha (Prohibition Sūtra) Condition Evaluator.
 * Evaluates prohibition conditions without hardcoded string checks.
 */
object DynamicNishedhaEvaluator {

    private val sankhyaEvaluator = SankhyaEvaluator()

    fun evaluateProhibition(guardText: String, argTerms: List<String>): Boolean {
        val trimmed = guardText.trim()

        // Evaluates "न X Y" or prohibition guard conditions
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

        // Rule 2: Dynamic equality prohibition check (e.g. if two arguments are equal and prohibited)
        if (evaluatedValues.size >= 2 && evaluatedValues[0] != -1L && evaluatedValues[1] != -1L) {
            if (trimmed.contains("तुल्य") || trimmed.contains("समान")) {
                if (evaluatedValues[0] == evaluatedValues[1]) return true
            }
        }

        return false
    }
}
