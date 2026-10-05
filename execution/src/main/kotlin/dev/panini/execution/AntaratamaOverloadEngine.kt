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

    fun matchTypes(signature: PrakriyaSignature, argumentTypes: List<PrakriyaValueType>): TypeMatch {
        if (signature.parameters.isNotEmpty()) {
            if (signature.parameters.size != argumentTypes.size) return TypeMatch.MISMATCH
            return if (signature.parameters.zip(argumentTypes).all { (parameter, actual) ->
                    actual == parameter.type
                }
            ) TypeMatch.EXACT else TypeMatch.MISMATCH
        }
        val expected = signature.argumentType ?: return TypeMatch.UNCONSTRAINED
        if (argumentTypes.isEmpty()) return TypeMatch.MISMATCH
        return if (argumentTypes.all { it == expected }) {
            TypeMatch.EXACT
        } else {
            TypeMatch.MISMATCH
        }
    }
}
