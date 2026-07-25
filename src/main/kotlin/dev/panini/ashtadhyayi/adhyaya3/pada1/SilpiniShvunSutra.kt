package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.145 शिल्पिनि ष्वुन्.
 * Prescribes ṣvun affix for artisans/craftsmen.
 */
object SilpiniShvunSutra : Sutra<String, String>(
    number = "3.1.145", text = "शिल्पिनि ष्वुन्",
    hindiExplanation = "शिल्पी (कलाकार/कारीगर) कर्ता अर्थ में धातु से 'ष्वुन्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310145,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("नृत्", "रञ्ज", "खन")
    override fun apply(context: String): String = "ष्वुन्"
}
