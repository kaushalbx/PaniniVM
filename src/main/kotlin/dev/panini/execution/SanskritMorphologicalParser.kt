package dev.panini.execution

import dev.panini.derivation.Vibhakti

/**
 * Morphological Subanta and Kāraka analyzer.
 * Uses Pāṇinian case endings (vibhakti), voice awareness (prayoga), and semantic rules
 * to map nominal expressions to their respective Kāraka roles.
 */
object SanskritMorphologicalParser {

    enum class Prayoga { KARTARI, KARMANI, BHAVE }

    data class MorphologicalToken(
        val originalText: String,
        val stem: String,
        val vibhakti: Vibhakti?,
        val inferredKaraka: Karaka,
        val samjnas: Set<ExecutionSamjna>,
    )

    fun inferPrayoga(verbText: String): Prayoga {
        val clean = verbText.trim().replace(Regex("[।॥,.!?+]"), "")
        return when {
            clean.endsWith("यते") || clean.endsWith("यन्ते") || clean.endsWith("यसे") || clean.endsWith("ये") -> Prayoga.KARMANI
            clean.contains("+कर्मणि") -> Prayoga.KARMANI
            clean.contains("+भावे") -> Prayoga.BHAVE
            else -> Prayoga.KARTARI
        }
    }

    fun parseToken(token: String, prayoga: Prayoga = Prayoga.KARTARI): MorphologicalToken {
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

        val (stem, vibhakti, karaka) = analyzeVibhakti(token, prayoga)
        return MorphologicalToken(
            originalText = token,
            stem = stem,
            vibhakti = vibhakti,
            inferredKaraka = karaka,
            samjnas = setOf(ExecutionSamjna.SHABDA),
        )
    }

    private fun analyzeVibhakti(word: String, prayoga: Prayoga): Triple<String, Vibhakti?, Karaka> {
        if (word in setOf("फलम्", "फलं", "फले", "फलानि")) {
            return Triple("फल", Vibhakti.DVITIYA, Karaka.KARMAN)
        }
        if (word in setOf("पूर्वफलम्", "पूर्वफलं", "पूर्वफले", "पूर्वफलानि")) {
            return Triple("पूर्वफल", Vibhakti.DVITIYA, Karaka.KARMAN)
        }

        return when {
            // Instrumental (Tṛtīyā) -> KARTR in Passive, KARANA in Active
            word.endsWith("ेण") || word.endsWith("ेना") || word.endsWith("ैः") -> {
                val stem = word.removeSuffix("ेण").removeSuffix("ेना").removeSuffix("ैः")
                val karaka = if (prayoga == Prayoga.KARMANI) Karaka.KARTR else Karaka.KARANA
                Triple(stem, Vibhakti.TRTIYA, karaka)
            }
            // Dative (Caturthī) -> SAMPRADANA
            word.endsWith("ाय") || word.endsWith("ये") || word.endsWith("भ्यः") || word == "मह्यम्" || word == "तुभ्यम्" -> {
                val stem = word.removeSuffix("ाय").removeSuffix("ये").removeSuffix("भ्यः")
                Triple(stem, Vibhakti.CHATURTHI, Karaka.SAMPRADANA)
            }
            // Ablative (Pañcamī) -> APADANA
            word.endsWith("ात्") -> {
                val stem = word.removeSuffix("ात्")
                Triple(stem, Vibhakti.PANCHAMI, Karaka.APADANA)
            }
            // Genitive (Ṣaṣṭhī) -> Relational / KARMAN fallback
            word.endsWith("स्य") || word.endsWith("योः") || word.endsWith("नाम्") || word.endsWith("णाम्") -> {
                val stem = word.removeSuffix("स्य").removeSuffix("योः").removeSuffix("नाम्").removeSuffix("णाम्")
                Triple(stem, Vibhakti.SASTHI, Karaka.KARMAN)
            }
            // Locative (Saptamī) -> ADHIKARANA
            word.endsWith("ेषु") || word.endsWith("े") -> {
                val stem = word.removeSuffix("ेषु")
                Triple(stem, Vibhakti.SAPTAMI, Karaka.ADHIKARANA)
            }
            // Accusative (Dvitīyā) -> KARMAN
            word.endsWith("म्") || word.endsWith("ं") -> {
                val stem = word.removeSuffix("म्").removeSuffix("ं")
                Triple(stem, Vibhakti.DVITIYA, Karaka.KARMAN)
            }
            // Nominative (Prathamā) -> KARMAN in Passive/Math, KARTR in Active
            word.endsWith("ः") || word.endsWith("ौ") || word.endsWith("ाः") -> {
                val stem = word.removeSuffix("ः").removeSuffix("ौ").removeSuffix("ाः")
                val karaka = if (prayoga == Prayoga.KARMANI) Karaka.KARMAN else Karaka.KARMAN
                Triple(stem, Vibhakti.PRATHAMA, karaka)
            }
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
