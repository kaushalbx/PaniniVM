package dev.panini.execution

import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.parser.PaniniParser

/**
 * 1.1.50 अन्तरङ्ग-बहिरङ्गयोः अन्तरङ्गं बलीयः
 * Pāṇinian Dynamic Scope Engine: Evaluates `अन्तर + अम् अङ्ग + ङसिँ` (internal scope directive)
 * prioritizing immediate local struct methods and attributes over parent/global fallbacks.
 */
object AntarangaScopeEngine {

    private val parser = PaniniParser()

    const val ANTARANGA_DIRECTIVE = "अन्तर + अम् अङ्ग + ङसिँ"

    /**
     * Checks if a sentence text contains the pure Subanta case-marked internal scope directive: "अन्तर + अम् अङ्ग + ङसिँ".
     */
    fun detectAntaranga(sentenceText: String, preParsedUkti: Ukti? = null): Boolean {
        val trimmed = sentenceText.trim()
        if (trimmed.contains(ANTARANGA_DIRECTIVE)) return true

        // Morphological fallback check for padas: "अन्तर + अम्" and "अङ्ग + ङसिँ"
        return trimmed.contains("अन्तर + अम्") && trimmed.contains("अङ्ग + ङसिँ")
    }

    /**
     * Strips the `अन्तर + अम् अङ्ग + ङसिँ` directive from sentence text for method matching.
     */
    fun stripAntarangaDirective(sentenceText: String): String {
        return sentenceText.replace(ANTARANGA_DIRECTIVE, "")
            .replace("अन्तर + अम् अङ्ग + ङसिँ", "")
            .replace(Regex("""\s+"""), " ")
            .trim()
    }
}
