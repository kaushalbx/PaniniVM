package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.97 अचो यत्.
 * Prescribes yat kṛtya affix after vowel-ending roots.
 */
object AchoYatSutra : Sutra<String, String>(
    number = "3.1.97", text = "अचो यत्",
    hindiExplanation = "अजन्त (स्वर-अन्त) धातुओं से 'यत्' कृत्य प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310097,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("जि", "ची", "नी", "गे", "पा", "दा")
    override fun apply(context: String): String = "यत्"
}
