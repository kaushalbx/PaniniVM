package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.60 चिण् कर्मणि च.
 * Prescribes ciṇ aorist vikaraṇa substitution in Karmaṇi passive and Bhāve Luṅ.
 */
object ChinKarmaniChaSutra : Sutra<String, String>(
    number = "3.1.60", text = "चिण् कर्मणि च",
    hindiExplanation = "कर्मवाच्य तथा भाववाच्य के त-प्रत्यय (त-स्थान) पर लुङ् लकार में 'चिण्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310060,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("कर्मणि", "भाववाच्य", "कर्मवाच्य", "लुङ्")
    override fun apply(context: String): String = "चिण्"
}
