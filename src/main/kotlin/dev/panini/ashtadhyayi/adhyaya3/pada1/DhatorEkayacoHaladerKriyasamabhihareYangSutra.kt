package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.22 धातोरेकाचो हलादेः क्रियासमभिहारे यङ्.
 * Prescribes yaṅ intensive/frequentative pratyaya for monosyllabic consonant-initial roots.
 */
object DhatorEkayacoHaladerKriyasamabhihareYangSutra : Sutra<String, String>(
    number = "3.1.22", text = "धातोरेकाचो हलादेः क्रियासमभिहारे यङ्",
    hindiExplanation = "क्रियासमभिहार (पौणःपुण्य/भृशार्थ) में एकाच् हलादि धातु से 'यङ्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310022,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotBlank()
    override fun apply(context: String): String = "यङ्"
}
