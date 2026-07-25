package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.96 तव्यत्तव्यानीयरः.
 * Prescribes tavyat, tavya, and anīyar kṛtya affixes after a root.
 */
object TavyattavyanIyarahSutra : Sutra<String, String>(
    number = "3.1.96", text = "तव्यत्तव्यानीयरः",
    hindiExplanation = "धातु से 'तव्यत्', 'तव्य' तथा 'अनीयर' प्रत्यय होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310096,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotBlank()
    override fun apply(context: String): String = "तव्यत्"
}
