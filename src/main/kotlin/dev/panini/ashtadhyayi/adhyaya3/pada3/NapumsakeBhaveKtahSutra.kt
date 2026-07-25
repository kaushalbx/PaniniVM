package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.3.114 नपुंसके भावे क्तः.
 * Prescribes kta neuter action affix.
 */
object NapumsakeBhaveKtahSutra : Sutra<String, String>(
    number = "3.3.114", text = "नपुंसके भावे क्तः",
    hindiExplanation = "नपुंसकलिङ्ग भाव अर्थ में धातु से 'क्त' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330114,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotBlank()
    override fun apply(context: String): String = "क्त"
}
