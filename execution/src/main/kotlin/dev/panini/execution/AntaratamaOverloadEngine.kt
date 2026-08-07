package dev.panini.execution

import dev.panini.sankhya.SankhyaEvaluator

/**
 * Pāṇinian Closest Type Match Overload Dispatch Engine based on Sūtra 1.1.50 (स्थानेऽन्तरतमः).
 *
 * Scores method overload candidates based on argument type proximity (अन्तरतमत्त्व).
 */
object AntaratamaOverloadEngine {

    private val sankhyaEvaluator = SankhyaEvaluator()

    fun calculateProximityScore(kriya: SamjnaKriya, argTerms: List<String>): Int {
        if (argTerms.isEmpty()) return 1

        val areAllArgsNumeric = argTerms.all { term ->
            term.toLongOrNull() != null || runCatching { sankhyaEvaluator.evaluateStems(listOf(term)).value }.getOrNull() != null
        }

        var score = 1
        val fullText = kriya.vidhiSentences.joinToString(" ") { it.text } + " " + kriya.nishedhaGuards.joinToString(" ") { it.text }

        val hasNumericGuard = fullText.contains("सङ्ख्या + त्व") || fullText.contains("सङ्ख्यात्व") || fullText.contains("सङ्ख्या")
        val hasTextGuard = fullText.contains("शब्द + त्व") || fullText.contains("शब्दत्व") || fullText.contains("शब्द") || fullText.contains("रूप")

        if (areAllArgsNumeric && hasNumericGuard) {
            score += 10
        } else if (!areAllArgsNumeric && hasTextGuard) {
            score += 10
        } else if (areAllArgsNumeric && hasTextGuard) {
            score -= 5 // Type mismatch penalty
        } else if (!areAllArgsNumeric && hasNumericGuard) {
            score -= 5 // Type mismatch penalty
        }

        return score
    }
}
