package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.20 तरप्तमपौ घः.
 * Assigns gha saṃjñā to comparative tarap and superlative tamap affixes.
 */
object TarapTamapGhahSutra : Sutra<String, String>(
    number = "1.1.20", text = "तरप्तमपौ घः",
    hindiExplanation = "तरप् तथा तमप् प्रत्ययों की 'घ' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110020,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.PRATYAYA,
    inputs = setOf(SutraInput.PRATYAYA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context == "तरप्" || context == "तमप्" || context == "तर" || context == "तम"
    override fun apply(context: String): String = "घ"
}
