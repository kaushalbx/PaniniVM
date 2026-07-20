package dev.panini.execution

import dev.panini.derivation.Vibhakti

/**
 * Morphological Subanta and Kāraka analyzer.
 * Uses Pāṇinian case endings (vibhakti) and semantic rules to map nominal
 * expressions to their respective Kāraka roles.
 */
object SanskritMorphologicalParser {

    data class MorphologicalToken(
        val originalText: String,
        val stem: String,
        val vibhakti: Vibhakti?,
        val inferredKaraka: Karaka,
        val samjnas: Set<ExecutionSamjna>,
    )

    fun parseToken(token: String): MorphologicalToken {
        val canonical = canonicalNumbers[token]
        if (canonical != null) {
            return MorphologicalToken(
                originalText = token,
                stem = canonical,
                vibhakti = Vibhakti.PRATHAMA,
                inferredKaraka = Karaka.KARMAN,
                samjnas = setOf(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA),
            )
        }

        val (stem, vibhakti, karaka) = analyzeVibhakti(token)
        return MorphologicalToken(
            originalText = token,
            stem = stem,
            vibhakti = vibhakti,
            inferredKaraka = karaka,
            samjnas = setOf(ExecutionSamjna.SHABDA),
        )
    }

    private fun analyzeVibhakti(word: String): Triple<String, Vibhakti?, Karaka> {
        if (word in setOf("फलम्", "फलं", "फले", "फलानि")) {
            return Triple("फल", Vibhakti.DVITIYA, Karaka.KARMAN)
        }
        if (word in setOf("पूर्वफलम्", "पूर्वफलं", "पूर्वफले", "पूर्वफलानि")) {
            return Triple("पूर्वफल", Vibhakti.DVITIYA, Karaka.KARMAN)
        }
        return when {
            // Instrumental (Tṛtīyā) -> KARTR / KARANA
            word.endsWith("ेण") || word.endsWith("ेना") || word.endsWith("ा") || word.endsWith("ैः") ->
                Triple(word.removeSuffix("ेण").removeSuffix("ेना").removeSuffix("ैः"), Vibhakti.TRTIYA, Karaka.KARTR)
            // Dative (Caturthī) -> SAMPRADANA
            word.endsWith("ाय") || word.endsWith("ये") || word.endsWith("भ्यः") || word == "मह्यम्" || word == "तुभ्यम्" ->
                Triple(word.removeSuffix("ाय").removeSuffix("ये").removeSuffix("भ्यः"), Vibhakti.CHATURTHI, Karaka.SAMPRADANA)
            // Ablative (Pañcamī) -> APADANA
            word.endsWith("ात्") || word.endsWith("ात्") ->
                Triple(word.removeSuffix("ात्"), Vibhakti.PANCHAMI, Karaka.APADANA)
            // Locative (Saptamī) -> ADHIKARANA
            word.endsWith("ेषु") ->
                Triple(word.removeSuffix("ेषु"), Vibhakti.SAPTAMI, Karaka.ADHIKARANA)
            // Accusative (Dvitīyā) -> KARMAN
            word.endsWith("म्") || word.endsWith("ं") ->
                Triple(word.removeSuffix("म्").removeSuffix("ं"), Vibhakti.DVITIYA, Karaka.KARMAN)
            // Nominative (Prathamā) -> KARMAN / KARTR
            word.endsWith("ः") || word.endsWith("ौ") || word.endsWith("ाः") ->
                Triple(word.removeSuffix("ः").removeSuffix("ौ").removeSuffix("ाः"), Vibhakti.PRATHAMA, Karaka.KARMAN)
            else -> Triple(word, null, Karaka.KARMAN)
        }
    }

    fun groupKarakas(
        tokens: List<MorphologicalToken>,
        resultReferences: Set<String>,
        conversation: SambhashanaContext?,
        yogaIdSupplier: (Int) -> String,
        clauseIndex: Int,
    ): Map<Karaka, ExecutionExpression> {
        val karakaMap = mutableMapOf<Karaka, MutableList<ExecutionExpression>>()

        tokens.forEach { token ->
            val isResultRef = token.stem in setOf("फल", "पूर्वफल") || token.originalText in resultReferences
            val expr = if (isResultRef) {
                if (clauseIndex == 0 || token.stem == "पूर्वफल" || token.originalText.startsWith("पूर्व")) {
                    val previous = conversation?.resultHistory?.lastOrNull()?.id
                        ?: conversation?.previousResults?.keys?.lastOrNull()
                    if (previous != null) ExecutionExpression.Reference(previous)
                    else ExecutionExpression.Literal(token.originalText, token.samjnas)
                } else {
                    ExecutionExpression.Reference(yogaIdSupplier(clauseIndex - 1))
                }
            } else {
                ExecutionExpression.Literal(token.stem, token.samjnas)
            }

            karakaMap.getOrPut(token.inferredKaraka) { mutableListOf() }.add(expr)
        }

        return karakaMap.mapValues { (_, exprs) ->
            if (exprs.size == 1) exprs.single() else ExecutionExpression.Coordination(exprs)
        }
    }

    private val canonicalNumbers: Map<String, String> = mapOf(
        "शून्य" to "शून्य", "शून्यम्" to "शून्य", "शून्यं" to "शून्य",
        "एक" to "एक", "एकम्" to "एक", "एकं" to "एक",
        "द्वि" to "द्वि", "द्वे" to "द्वि",
        "त्रि" to "त्रि", "त्रीणि" to "त्रि",
        "चतुर्" to "चतुर्", "चत्वारि" to "चतुर्",
        "पञ्च" to "पञ्च", "षट्" to "षट्", "सप्त" to "सप्त",
        "अष्ट" to "अष्ट", "नव" to "नव", "दश" to "दश",
    )
}
