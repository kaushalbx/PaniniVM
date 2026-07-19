package dev.panini.execution

/** Initial canonical vocabulary; it can grow independently of dhātu execution. */
object SanskritNumbers {
    private val words = listOf(
        "शून्य", "एक", "द्वि", "त्रि", "चतुर्", "पञ्च", "षट्", "सप्त", "अष्ट", "नव", "दश",
        "एकादश", "द्वादश", "त्रयोदश", "चतुर्दश", "पञ्चदश", "षोडश", "सप्तदश", "अष्टादश", "नवदश", "विंशति",
    )
    private val values = words.withIndex().associate { (value, word) -> word to value }

    fun valueOf(word: String): Int? = values[word]

    fun wordFor(value: Int): String? = words.getOrNull(value)
}
