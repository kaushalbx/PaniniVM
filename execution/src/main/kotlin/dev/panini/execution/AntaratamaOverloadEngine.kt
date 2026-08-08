package dev.panini.execution

/**
 * Pāṇinian Closest Type Match Overload Dispatch Engine based on Sūtra 1.1.50 (स्थानेऽन्तरतमः).
 *
 * Scores method overload candidates based on argument type proximity (अन्तरतमत्त्व).
 */
object AntaratamaOverloadEngine {
    enum class TypeMatch(val rank: Int) {
        MISMATCH(0),
        UNCONSTRAINED(1),
        EXACT(2),
    }

    fun match(signature: SamjnaSignature, argTerms: List<String>): TypeMatch {
        val expected = signature.argumentType ?: return TypeMatch.UNCONSTRAINED
        if (argTerms.isEmpty()) return TypeMatch.MISMATCH
        return if (argTerms.all { SamjnaValueClassifier.classifyTerm(it) == expected }) {
            TypeMatch.EXACT
        } else {
            TypeMatch.MISMATCH
        }
    }
}
