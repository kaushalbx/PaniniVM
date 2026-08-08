package dev.panini.sankhya

/** Shared lexical classification for segmented arithmetic expressions. */
object SankhyaOperationMarkers {
    private val multiplication = setOf("गुणित", "हते")
    private val division = setOf("भक्त", "हृत")
    private val bindingPrefixes = setOf("वर्ग", "घन", "मूल")

    fun isMultiplication(stem: String): Boolean = stem in multiplication

    fun isDivision(stem: String): Boolean = stem in division

    fun isSquare(stem: String): Boolean = stem == "वर्ग"

    fun isCube(stem: String): Boolean = stem == "घन"

    fun isSquareRoot(stem: String): Boolean = stem == "मूल" || stem == "पद"

    fun isConstruction(stem: String): Boolean = stem == "कृत"

    fun isBindingPrefix(stem: String): Boolean = stem in bindingPrefixes

    fun needsFollowingOperand(stems: List<String>): Boolean =
        stems.lastOrNull()?.let { isMultiplication(it) || isDivision(it) || isConstruction(it) } == true ||
            stems.firstOrNull()?.let(::isBindingPrefix) == true

    fun acceptsFollowingOperand(stems: List<String>): Boolean = stems.size <= 2
}
