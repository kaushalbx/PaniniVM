package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.22 तति सङ्ख्या.
 * Assigns saṅkhyā saṃjñā to words ending in the suffix dati (e.g. kati).
 */
object TatiSankhyaSutra : Sutra<String, String>(
    number = "1.1.22", text = "तति सङ्ख्या",
    hindiExplanation = "डति प्रत्ययान्त शब्दों (जैसे कति) की 'सङ्ख्या' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110022,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
    inputs = setOf(SutraInput.PRATIPADIKA, SutraInput.PRATYAYA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.endsWith("ति") || context == "कति"
    override fun apply(context: String): String = "सङ्ख्या"
}
