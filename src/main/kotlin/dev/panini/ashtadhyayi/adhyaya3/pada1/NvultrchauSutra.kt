package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.133 ण्वुल्तृचौ.
 * Prescribes ṇvul and tṛc agent affixes after roots.
 */
object NvultrchauSutra : Sutra<String, String>(
    number = "3.1.133", text = "ण्वुल्तृचौ",
    hindiExplanation = "धातु से कर्ता अर्थ में सामान्यतः 'ण्वुल्' तथा 'तृच्' प्रत्यय होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310133,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotBlank()
    override fun apply(context: String): String = "तृच्"
}
