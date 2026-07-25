package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.144 गेहे कः.
 * Prescribes ka affix for domestic terms.
 */
object GeheKahSutra : Sutra<String, String>(
    number = "3.1.144", text = "गेहे कः",
    hindiExplanation = "गेह (गृह) अर्थ में धातु से 'क' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310144,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("ग्रह्", "गेह")
    override fun apply(context: String): String = "क"
}
