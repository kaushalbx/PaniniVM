package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.29 ऋतीयीयाङ्भ्यः क्यङ्.
 * Prescribes kyaṅ pratyaya for ṛtī, īyā, āṅ roots.
 */
object RtIyIyAnehKyanSutra : Sutra<String, String>(
    number = "3.1.29", text = "ऋतीयीयाङ्भ्यः क्यङ्",
    hindiExplanation = "ऋतीय, ईयास् तथा आङ् से 'क्यङ्' प्रत्यय स्वार्थ में होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310029,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("ऋतीय", "ईयास्", "आङ्")
    override fun apply(context: String): String = "क्यङ्"
}
